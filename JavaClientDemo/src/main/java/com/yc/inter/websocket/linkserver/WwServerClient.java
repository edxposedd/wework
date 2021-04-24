package com.yc.inter.websocket.linkserver;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

public class WwServerClient extends WebSocketClient {

    private static Logger logger = LoggerFactory.getLogger(WwServerClient.class);

    public WwServerClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("WwServerClient握手。。。");
    }

    public void onMessage(String message) {
        try {
            logger.info("客户端接收服务端回调标识"+message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onMessage(ByteBuffer message) {
        try {
            byte[] bytes = new byte[message.limit() - message.position()];
            message.get(bytes);
            WwHookerApis.getInstance().receiveMessage(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClose(int i, String s, boolean b) {
        WwHookerApis.getInstance().reconnect();
    }

    @Override
    public void onError(Exception e) {
        WwHookerApis.getInstance().reconnect();
    }

}
