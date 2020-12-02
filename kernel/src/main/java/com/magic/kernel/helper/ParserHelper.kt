package com.magic.kernel.helper

import com.magic.kernel.utils.ParallelUtil.parallelForEach
import java.io.Closeable
import java.io.File
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * 参考了：https://github.com/Gh0u1L5/WechatSpellbook, 在此基础上修复了当packageName==""时搜索结果错误的问题
 */
object ParserHelper {

    class ApkFile(apkFile: File) : Closeable {

        private companion object {
            private const val DEX_FILE = "classes.dex"
            private const val DEX_ADDITIONAL = "classes%d.dex"
        }

        constructor(path: String) : this(File(path))

        private val zipFile: ZipFile = ZipFile(apkFile)

        private fun readEntry(entry: ZipEntry): ByteArray =
            zipFile.getInputStream(entry).use { it.readBytes() }

        override fun close() = zipFile.close()

        private fun getDexFilePath(idx: Int) =
            if (idx == 1) DEX_FILE else String.format(DEX_ADDITIONAL, idx)

        private fun isDexFileExist(idx: Int): Boolean {
            val path = getDexFilePath(idx)
            return zipFile.getEntry(path) != null
        }

        private fun readDexFile(idx: Int): ByteArray {
            val path = getDexFilePath(idx)
            return readEntry(zipFile.getEntry(path))
        }

        val classTypes: ClassTrie by lazy {
            var end = 2
            while (isDexFileExist(end)) end++

            val ret = ClassTrie()
            (1 until end).parallelForEach { idx ->
                val data = readDexFile(idx)
                val buffer = ByteBuffer.wrap(data)
                val parser =
                    DexParser(buffer)
                ret += parser.parseClassTypes()
            }
            return@lazy ret.apply { mutable = false }
        }
    }


    data class DexHeader(
        var version: Int = 0,
        var checksum: UInt = 0u,
        var signature: ByteArray = ByteArray(kSHA1DigestLen),
        var fileSize: UInt = 0u,
        var headerSize: UInt = 0u,
        var endianTag: UInt = 0u,
        var linkSize: UInt = 0u,
        var linkOff: UInt = 0u,
        var mapOff: UInt = 0u,
        var stringIdsSize: Int = 0,
        var stringIdsOff: UInt = 0u,
        var typeIdsSize: Int = 0,
        var typeIdsOff: UInt = 0u,
        var protoIdsSize: Int = 0,
        var protoIdsOff: UInt = 0u,
        var fieldIdsSize: Int = 0,
        var fieldIdsOff: UInt = 0u,
        var methodIdsSize: Int = 0,
        var methodIdsOff: UInt = 0u,
        var classDefsSize: Int = 0,
        var classDefsOff: UInt = 0u,
        var dataSize: Int = 0,
        var dataOff: UInt = 0u
    ) {
        companion object {
            const val kSHA1DigestLen = 20
        }
    }


    class ClassTrie {

        companion object {
            private fun convertJVMTypeToClassName(type: String) =
                type.substring(1, type.length - 1).replace('/', '.')
        }

        @Volatile
        var mutable = true

        private val root: TrieNode = TrieNode()

        operator fun plusAssign(type: String) {
            if (mutable) {
                root.add(convertJVMTypeToClassName(type))
            }
        }

        operator fun plusAssign(types: Array<String>) = types.forEach { this += it }

        fun search(packageName: String, depth: Int): List<String> {
            if (mutable) return emptyList()
            return root.search(packageName, depth)
        }

        private class TrieNode {
            val classes: MutableList<String> = ArrayList(50)

            val children: MutableMap<String, TrieNode> = ConcurrentHashMap()

            fun add(className: String) {
                add(className, 0)
            }

            private fun add(className: String, pos: Int) {
                val delimiterAt = className.indexOf('.', pos)
                if (delimiterAt == -1) {
                    synchronized(this) {
                        classes.add(className)
                    }
                    return
                }
                val pkg = className.substring(pos, delimiterAt)
                if (pkg !in children) {
                    children[pkg] =
                        TrieNode()
                }
                children[pkg]!!.add(className, delimiterAt + 1)
            }

            fun get(depth: Int = 0): List<String> {
                if (depth == 0) return classes
                return children.flatMap { it.value.get(depth - 1) }
            }

            fun search(packageName: String, depth: Int): List<String> {
                return search(packageName, depth, 0)
            }

            private fun search(packageName: String, depth: Int, pos: Int): List<String> {
                val delimiterAt = packageName.indexOf('.', pos)
                if (delimiterAt == -1) {
                    return when (packageName.isEmpty()) {
                        true -> classes
                        false -> children[packageName]?.get(depth) ?: emptyList()
                    }
                }
                val pkg = packageName.substring(pos, delimiterAt)
                val next = children[pkg] ?: return emptyList()
                return next.search(packageName, depth, delimiterAt + 1)
            }
        }
    }

    class DexParser(buffer: ByteBuffer) {
        private val buffer: ByteBuffer = buffer.duplicate().apply {
            order(ByteOrder.LITTLE_ENDIAN)
        }

        private fun ByteBuffer.readBytes(size: Int) = ByteArray(size).also { get(it) }

        fun parseClassTypes(): Array<String> {
            val magic = String(buffer.readBytes(8))
            if (!magic.startsWith("dex\n")) {
                return arrayOf()
            }
            val version = Integer.parseInt(magic.substring(4, 7))
            if (version < 35) {
                throw Exception("Dex file version: $version is not supported")
            }

            val header = readDexHeader()
            header.version = version

            // read string offsets
            val stringOffsets = readStringOffsets(header.stringIdsOff, header.stringIdsSize)

            // read type ids
            val typeIds = readTypeIds(header.typeIdsOff, header.typeIdsSize)

            // read class ids
            val classIds = readClassIds(header.classDefsOff, header.classDefsSize)


            // read class types
            return classIds.map {
                val typeId = typeIds[it]
                val offset = stringOffsets[typeId]
                readStringAtOffset(offset)
            }.toTypedArray()
        }

        private fun readDexHeader() = DexHeader().apply {
            checksum = buffer.int.toUInt()

            buffer.get(signature)

            fileSize = buffer.int.toUInt()
            headerSize = buffer.int.toUInt()

            endianTag = buffer.int.toUInt()

            linkSize = buffer.int.toUInt()
            linkOff = buffer.int.toUInt()

            mapOff = buffer.int.toUInt()

            stringIdsSize = buffer.int
            stringIdsOff = buffer.int.toUInt()

            typeIdsSize = buffer.int
            typeIdsOff = buffer.int.toUInt()

            protoIdsSize = buffer.int
            protoIdsOff = buffer.int.toUInt()

            fieldIdsSize = buffer.int
            fieldIdsOff = buffer.int.toUInt()

            methodIdsSize = buffer.int
            methodIdsOff = buffer.int.toUInt()

            classDefsSize = buffer.int
            classDefsOff = buffer.int.toUInt()

            dataSize = buffer.int
            dataOff = buffer.int.toUInt()
        }

        private fun readStringOffsets(stringIdsOff: UInt, stringIdsSize: Int): IntArray {
            (buffer as Buffer).position(stringIdsOff.toInt())
            return IntArray(stringIdsSize) {
                buffer.int
            }
        }

        private fun readTypeIds(typeIdsOff: UInt, typeIdsSize: Int): IntArray {
            (buffer as Buffer).position(typeIdsOff.toInt())
            return IntArray(typeIdsSize) {
                buffer.int
            }
        }

        private fun readClassIds(classDefsOff: UInt, classDefsSize: Int): Array<Int> {
            (buffer as Buffer).position(classDefsOff.toInt())
            return Array(classDefsSize) {
                val classIdx = buffer.int
                // access_flags, skip
                buffer.int
                // superclass_idx, skip
                buffer.int
                // interfaces_off, skip
                buffer.int
                // source_file_idx, skip
                buffer.int
                // annotations_off, skip
                buffer.int
                // class_data_off, skip
                buffer.int
                // static_values_off, skip
                buffer.int

                classIdx
            }
        }

        private fun readStringAtOffset(offset: Int): String {
            (buffer as Buffer).position(offset)
            val len = readULEB128Int()
            return readString(len)
        }

        private fun readULEB128Int(): Int {
            var ret = 0

            var count = 0
            var byte: Int
            do {
                if (count > 4) {
                    throw Exception("read varints error.")
                }
                byte = buffer.get().toInt()
                ret = ret or (byte and 0x7f shl count * 7)
                count++
            } while (byte and 0x80 != 0)

            return ret
        }

        private fun readString(len: Int): String {
            val chars = CharArray(len)

            for (i in 0 until len) {
                val a = buffer.get().toInt()
                when {
                    a and 0x80 == 0 -> {    // ascii char
                        chars[i] = a.toChar()
                    }
                    a and 0xe0 == 0xc0 -> { // read one more
                        val b = buffer.get().toInt()
                        chars[i] = (a and 0x1F shl 6 or (b and 0x3F)).toChar()
                    }
                    a and 0xf0 == 0xe0 -> {
                        val b = buffer.get().toInt()
                        val c = buffer.get().toInt()
                        chars[i] = (a and 0x0F shl 12 or (b and 0x3F shl 6) or (c and 0x3F)).toChar()
                    }
                    else -> {
                        // throw UTFDataFormatException()
                    }
                }
            }

            return String(chars)
        }
    }


}