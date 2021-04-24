package com.yc.inter.websocket.linkserver;

import com.google.protobuf.ByteString;
import com.google.protobuf.TextFormat;
import com.yc.inter.websocket.protobuf.CmdPb;
import com.yc.inter.websocket.protobuf.WwCmdPb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class WwHookerApis {

    private static WwHookerApis instance = null;

    private static Logger logger = LoggerFactory.getLogger(WwHookerApis.class);
    private WwServerClient mClient = null;

    public static WwHookerApis getInstance() {
        synchronized (WwHookerApis.class) {
            if (instance == null) {
                instance = new WwHookerApis();
            }
            return instance;
        }
    }

    private WwHookerApis() {
    }


    public void reconnect() {
        try {
            Thread.sleep(5 * 1000);
            mClient = null;
            connect();
        } catch (Exception e) {
            logger.error("websocket 服务器关闭，等待重连中...");
        }
    }


    public void connect() {
        try {
            if (mClient == null) {
                mClient = new WwServerClient(new URI(WwCmdConfigs.wsHost + "/" + WwCmdConfigs.username + "/" + WwCmdConfigs.password));
            }
            if (!mClient.isOpen()) {
                mClient.connect();
            }
        } catch (Exception e) {
            logger.error("websocket 创建连接时出错 " + e.getMessage());
        }
    }

    public void receiveMessage(byte[] data) {
        try {
            CmdPb.Cmd cmd = CmdPb.Cmd.parseFrom(data);
            String jsonString = "";
            if (cmd.getCode() == 0) {
                switch (cmd.getCname()) {
                    case WwCmdConfigs.CmdWw321:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw321.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw331:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw331.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw332:
                        jsonString =  TextFormat.printToUnicodeString(WwCmdPb.CmdWw332.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw333:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw333.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw334:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw334.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw335:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw335.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw336:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw336.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw337:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw337.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw338:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw338.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw341:
                        jsonString = WwCmdPb.CmdWw341.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw342:
                        jsonString = WwCmdPb.CmdWw342.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw343:
                        // 由于demo是固定配置的，故这里设置为搜索到好友直接就下发添加好友的指令
                        WwCmdPb.CmdWw343 cmd343 = WwCmdPb.CmdWw343.parseFrom(cmd.getData());
                        jsonString = cmd343.toString();
                        int k = 0;
                        for (ByteString originalData : cmd343.getOriginalUserList()) {
                            sendCmdWw344(originalData.toByteArray(), "你好，我是" + cmd343.getUser(k).getNickname());
                            k += 1;
                        }
                        break;
                    case WwCmdConfigs.CmdWw344:
                        jsonString = WwCmdPb.CmdWw344.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw345:
                        // 由于demo是固定配置的，这里设置为自动通过
                        WwCmdPb.CmdWw345 cmd345 = WwCmdPb.CmdWw345.parseFrom(cmd.getData());
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw345.parseFrom(cmd.getData()));
                        for (WwCmdPb.User user : cmd345.getUserList()) {
                            sendCmdWw346(user.getId(), 0);
                        }
                        break;
                    case WwCmdConfigs.CmdWw346:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw346.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw347:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw347.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw348:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw348.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw349:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw349.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3410:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3410.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3411:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3411.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3412:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3412.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3415:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3415.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3416:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3416.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3417:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3417.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3418:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3418.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3419:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3419.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw351:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw351.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw352:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw352.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw353:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw353.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw354:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw354.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw355:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw355.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw356:
                        jsonString = WwCmdPb.CmdWw356.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw357:
                        jsonString = WwCmdPb.CmdWw357.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw358:
                        jsonString = WwCmdPb.CmdWw358.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw359:
                        jsonString = WwCmdPb.CmdWw359.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3510:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3510.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3511:
                        jsonString = WwCmdPb.CmdWw3511.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3512:
                        WwCmdPb.CmdWw3512 cmd3512 = WwCmdPb.CmdWw3512.parseFrom(cmd.getData());
                        jsonString = cmd3512.toString();

                        // 创建群聊成功，邀请人进群
                        List<Long> list3511 = new ArrayList<>();
                        list3511.add(WwCmdConfigs.innerId);
                        sendCmdWw3511(cmd3512.getConv().getId(), list3511);
                        break;
                    case WwCmdConfigs.CmdWw3513:
                        jsonString = WwCmdPb.CmdWw3513.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3514:
                        jsonString = WwCmdPb.CmdWw3514.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3515:
                        jsonString = WwCmdPb.CmdWw3515.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3516:
                        jsonString = WwCmdPb.CmdWw3516.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3517:
                        jsonString = WwCmdPb.CmdWw3517.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3518:
                        jsonString = WwCmdPb.CmdWw3518.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3519:
                        jsonString = WwCmdPb.CmdWw3519.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3520:
                        jsonString = WwCmdPb.CmdWw3520.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3521:
                        jsonString = WwCmdPb.CmdWw3521.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3522:
                        jsonString = WwCmdPb.CmdWw3522.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3523:
                        jsonString = WwCmdPb.CmdWw3523.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3524:
                        jsonString = WwCmdPb.CmdWw3524.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3525:
                        jsonString = WwCmdPb.CmdWw3525.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw361:
                        WwCmdPb.CmdWw361 cmd361 = WwCmdPb.CmdWw361.parseFrom(cmd.getData());
                        // 361 是各种消息体，这里为了更清晰看到数据结构需要做解析
                        WwCmdPb.Message message = cmd361.getMsg();
                        if (message.hasContent()) {
                            switch (message.getContent().getTypeValue()) {
                                case WwCmdPb.RichMessage.Type.SYSTEM_VALUE:
                                    jsonString += "转换后:  系统消息   " + TextFormat.printToUnicodeString(WwCmdPb.RichMessage.SystemMessage.parseFrom(message.getContent().getContent()));
                                    break;
                                case WwCmdPb.RichMessage.Type.TEXT_VALUE:
                                    jsonString += "转换后:  文本   " + TextFormat.printToUnicodeString(WwCmdPb.RichMessage.TextMessage.parseFrom(message.getContent().getContent()));
                                    if (WwCmdPb.RichMessage.TextMessage.parseFrom(message.getContent().getContent()).getText().equalsIgnoreCase("测试撤回")) {
                                        sendCmdWw364(message.getConvId(), message.getId());
                                    }
                                    break;
                                case WwCmdPb.RichMessage.Type.IMAGE_VALUE:
                                    jsonString += "转换后:  图片   " + TextFormat.printToUnicodeString(WwCmdPb.RichMessage.ImageMessage.parseFrom(message.getContent().getContent()));
                                    break;
                                case WwCmdPb.RichMessage.Type.VOICE_VALUE:
                                    jsonString += "转换后:  语音   " + TextFormat.printToUnicodeString(WwCmdPb.RichMessage.VoiceMessage.parseFrom(message.getContent().getContent()));
                                    break;
                                case WwCmdPb.RichMessage.Type.VIDEO_VALUE:
                                    jsonString += "转换后:  视频   " + TextFormat.printToUnicodeString(WwCmdPb.RichMessage.VideoMessage.parseFrom(message.getContent().getContent()));
                                    break;
                                case WwCmdPb.RichMessage.Type.FILE_VALUE:
                                    jsonString += "转换后:  文件   " + TextFormat.printToUnicodeString(WwCmdPb.RichMessage.FileMessage.parseFrom(message.getContent().getContent()));
                                    break;
                                case WwCmdPb.RichMessage.Type.LINK_VALUE:
                                    jsonString += "转换后:  链接   " + TextFormat.printToUnicodeString(WwCmdPb.RichMessage.LinkMessage.parseFrom(message.getContent().getContent()));
                                    break;
                                case WwCmdPb.RichMessage.Type.LOCATION_VALUE:
                                    jsonString += "转换后:  地理位置   " + WwCmdPb.RichMessage.LocationMessage.parseFrom(message.getContent().getContent());
                                    break;
                                case WwCmdPb.RichMessage.Type.WE_APP_VALUE:
                                    WwCmdPb.RichMessage.WeAppMessage weAppMessage = WwCmdPb.RichMessage.WeAppMessage.parseFrom(message.getContent().getContent());
                                    jsonString += "转换后:  小程序   " + TextFormat.printToUnicodeString(weAppMessage) + " \n isEmpty: " + weAppMessage.getOriginalData().isEmpty();
                                    if (!weAppMessage.getOriginalData().isEmpty() && cmd.getWid() != message.getSender()) {
                                        sendCmdWw366(cmd361.getConv().getId(), "这是程序自动回复发送原始小程序数据", new ArrayList<>(), 0L);
                                        sendCmdWw3613(cmd361.getConv().getId(), weAppMessage.getOriginalData());
                                    }
                                    break;
                                case WwCmdPb.RichMessage.Type.PERSONAL_CARD_VALUE:
                                    jsonString += "转换后:  个人名片   " + WwCmdPb.RichMessage.PersonalCard.parseFrom(message.getContent().getContent());
                                    break;
                            }
                        } else if (message.hasHbcontent()) {
                            WwCmdPb.RedEnvelope redEnvelope = message.getHbcontent();
                            switch (redEnvelope.getTypeValue()) {
                                case WwCmdPb.RedEnvelope.Type.HB_MSG_SYSTEM_VALUE:
                                    jsonString += "转换后:  hb消息  sys  " + TextFormat.printToUnicodeString(WwCmdPb.RedEnvelope.HongBaoSysMsgContent.parseFrom(redEnvelope.getContent()));
                                    break;
                                case WwCmdPb.RedEnvelope.Type.HB_MSG_CONTENT_VALUE:
                                    WwCmdPb.RedEnvelope.HongBaoMsgContent hongBaoMsgContent = WwCmdPb.RedEnvelope.HongBaoMsgContent.parseFrom(redEnvelope.getContent());
                                    sendCmdWw382(hongBaoMsgContent.getHongbaoid(), hongBaoMsgContent.getHbticket(), hongBaoMsgContent.getVidticket(), hongBaoMsgContent.getSceneid());
                                    jsonString += "转换后:  hb消息  msg  " + TextFormat.printToUnicodeString(hongBaoMsgContent);
                                    break;
                                case WwCmdPb.RedEnvelope.Type.HB_ACKED_MSG_CONTENT_VALUE:
                                    jsonString += "转换后:  hb消息   ack " + TextFormat.printToUnicodeString(WwCmdPb.RedEnvelope.HongBaoAckedMsgContent.parseFrom(redEnvelope.getContent()));
                                    break;
                                case WwCmdPb.RedEnvelope.Type.HB_LISHI_CONTENT_VALUE:
                                    jsonString += "转换后:  hb消息  ls  " + TextFormat.printToUnicodeString(WwCmdPb.RedEnvelope.LiShiMsgContent.parseFrom(redEnvelope.getContent()));
                                    break;
                            }
                        }
                        jsonString += "\n 转换前： 原始数据 " + cmd361.toString();
                        break;
                    case WwCmdConfigs.CmdWw362:
                        jsonString = WwCmdPb.CmdWw362.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw363:
                        jsonString = WwCmdPb.CmdWw363.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw364:
                        jsonString = WwCmdPb.CmdWw364.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw365:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw365.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw366:
                        jsonString = WwCmdPb.CmdWw366.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw367:
                        jsonString = WwCmdPb.CmdWw367.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw368:
                        jsonString = WwCmdPb.CmdWw368.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw369:
                        jsonString = WwCmdPb.CmdWw369.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3610:
                        jsonString = WwCmdPb.CmdWw3610.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3611:
                        jsonString = WwCmdPb.CmdWw3611.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3612:
                        jsonString = WwCmdPb.CmdWw3612.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw3613:
                        jsonString = WwCmdPb.CmdWw3613.parseFrom(cmd.getData()).toString();
                        break;
                    case WwCmdConfigs.CmdWw371:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw371.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw372:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw372.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw373:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw373.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw374:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw374.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw375:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw375.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw376:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw376.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw377:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw377.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw378:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw378.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw379:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw379.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3710:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3710.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3711:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3711.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3712:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3712.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3713:
                        WwCmdPb.CmdWw3713 cmd3713 = WwCmdPb.CmdWw3713.parseFrom(cmd.getData());
                        sendCmdWw3102(true, cmd3713.getOriginalRecord());
                        jsonString = TextFormat.printToUnicodeString(cmd3713);
                        break;
                    case WwCmdConfigs.CmdWw3714:
                        WwCmdPb.CmdWw3714 cmd3714 = WwCmdPb.CmdWw3714.parseFrom(cmd.getData());
                        sendCmdWw366(cmd3714.getUser().getId(), "这里是成功加入企业的自动回复", new ArrayList<>(), 0L);
                        jsonString = TextFormat.printToUnicodeString(cmd3714);
                        break;
                    case WwCmdConfigs.CmdWw3715:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3715.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3716:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3716.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3717:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3717.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3718:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3718.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3719:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3719.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3720:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3720.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3721:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3721.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3722:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3722.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3723:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3723.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw382:
                        WwCmdPb.CmdWw382 cmdWw382 = WwCmdPb.CmdWw382.parseFrom(cmd.getData());
                        if (cmdWw382.getResult().getStatus() == 2) {
                            sendCmdWw383(cmdWw382.getHongbaoid(), cmdWw382.getHbticket(), cmdWw382.getVidticket(), 0);
                        }
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw382.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw383:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw382.parseFrom(cmd.getData()));
                        break;
                    case WwCmdConfigs.CmdWw3102:
                        jsonString = TextFormat.printToUnicodeString(WwCmdPb.CmdWw3102.parseFrom(cmd.getData()));
                        break;
                    default:
                        break;
                }
            }
            logger.info(cmd.getWid() + " -- " + cmd.getCname() + " - " + cmd.getCode() + " - " + cmd.getMsg() + "\n" + jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("解析数据出错： " + e.getMessage());
        }

    }

    public void sendMessage(String name) {
        try {
            switch (name) {
                case "test":
                    sendCmdWw366(WwCmdConfigs.convId, "这里是测试消息1 延迟10s-------\n\n\nddlfjasldfkj", new ArrayList<>(), 10000L);
//                    sendCmdWw367(WwCmdConfigs.convId, "http://image.fnull.top/00df0f7743609fd7f9a7045b8fa93478", 20000L);
                    sendCmdWw366(WwCmdConfigs.convId, "这里是测试消息2xxxxxx  延迟7s", new ArrayList<>(), 7000L);
//                    sendCmdWw367(WwCmdConfigs.convId, "http://image.fnull.top/00df0f7743609fd7f9a7045b8fa93478", 10000L);
                    sendCmdWw366(WwCmdConfigs.convId, "这里是测试消息2xxxxxx  延迟 5s", new ArrayList<>(), 5000L);
                    sendCmdWw366(WwCmdConfigs.convId, "这里是测试消息2xxxxxx  延迟1s", new ArrayList<>(), 1000L);
                    break;
                case "321":
                    sendCmdWw321();
                    break;
                case "331":
                    sendCmdWw331();
                    break;
                case "332":
                    sendCmdWw332("http://ww-image.yuanchuan.tech/avatar0000001.png");
                    break;
                case "333":
                    sendCmdWw333();
                    break;
                case "334":
                    sendCmdWw334(0L);
                    break;
                case "335":
                    sendCmdWw335();
                    break;
                case "336":
                    sendCmdWw336();
                    break;
                case "337":
                    sendCmdWw337();
                    break;
                case "338":
                    sendCmdWw338("http://ww-image.yuanchuan.tech/7f163d615103b4fe19a45a5aba116272.0");
                    break;
                case "341":
                    sendCmdWw341(WwCmdConfigs.outterId, "这里是备注", "备注公司名称", new ArrayList<String>(), "这里是描述", "");
                    break;
                case "342":
                    sendCmdWw342(WwCmdConfigs.innerId, "这里是内部备注", "这里是内部成员的描述");
                    break;
                case "343":
                    sendCmdWw343(WwCmdConfigs.phone);
                    break;
                case "344":
                    logger.info("demo 测试已在343搜索回调后自动执行了，请注意343返回的结果");
                    break;
                case "345":
                    sendCmdWw345();
                    break;
                case "346":
                    logger.info("demo 测试已在343搜索回调后自动执行了，请注意343返回的结果");
                    break;
                case "347":
                    if (WwCmdConfigs.delOutterId > 0) {
                        sendCmdWw347(WwCmdConfigs.delOutterId);
                    } else {
                        logger.info("demo 为配置需要删除的联系人");
                    }
                    break;
                case "348":
                    sendCmdWw348();
                    break;
                case "349":
                    sendCmdWw349();
                    break;
                case "3410":
                    List<Long> list = new ArrayList<>();
//                    list.add(WwCmdConfigs.deptId);
                    sendCmdWw3410(list);
                    break;
                case "3411":
                    sendCmdWw3411();
                    break;
                case "3412":
                    sendCmdWw3412();
                    break;
                case "3415":
                    sendCmdWw3415();
                    break;
                case "3416":
                    sendCmdWw3416();
                    break;
                case "3417":
                    sendCmdWw3417();
                    break;
                case "3418":
                    sendCmdWw3418();
                    break;
                case "3419":
                    sendCmdWw3419();
                    break;
                case "351":
                    logger.info("demo 3.5.1仅是预留接口，下发无效");
                    break;
                case "352":
                    sendCmdWw352();
                    break;
                case "353":
                    sendCmdWw353();
                    break;
                case "354":
                    sendCmdWw354();
                    break;
                case "355":
                    List<Long> list355 = new ArrayList<>();
                    list355.add(WwCmdConfigs.innerId);
                    list355.add(WwCmdConfigs.outterId);
                    sendCmdWw355(list355, "测试群聊", 0);
                    break;
                case "356":
                    if (WwCmdConfigs.convId > 0L) {
                        sendCmdWw356(WwCmdConfigs.convId);
                    } else {
                        logger.info("demo 3.5.6未配置群id");
                    }
                    break;
                case "357":
                    if (WwCmdConfigs.convId > 0L) {
                        sendCmdWw357(WwCmdConfigs.convId);
                    } else {
                        logger.info("demo 3.5.6未配置群id");
                    }
                case "358":
                    sendCmdWw358(WwCmdConfigs.convId, "测试群聊-修改名称");
                    break;
                case "359":
                    sendCmdWw359(WwCmdConfigs.convId, "我的群内昵称");
                    break;
                case "3510":
                    sendCmdWw3510(WwCmdConfigs.convId);
                    break;
                case "3511":
                    List<Long> list3511 = new ArrayList<>();
                    list3511.add(WwCmdConfigs.invitedId);
                    sendCmdWw3511(WwCmdConfigs.convId, list3511);
                    break;
                case "3512":
                    sendCmdWw3512();
                    break;
                case "3513":
                    List<Long> list3513 = new ArrayList<>();
                    list3513.add(WwCmdConfigs.invitedId);
                    sendCmdWw3513(WwCmdConfigs.convId, list3513);
                    break;
                case "3514":
                    sendCmdWw3514(WwCmdConfigs.convId);
                    break;
                case "3515":
                    // 注意这里转让群主，群内必须包含有内部成员，仅能转让给内部成员
                    sendCmdWw3515(WwCmdConfigs.convId, WwCmdConfigs.innerId);
                    break;
                case "3516":
                    sendCmdWw3516(WwCmdConfigs.convId, "这里是群公告");
                    break;
                case "3517":
                    sendCmdWw3517(WwCmdConfigs.convId, true);
                    break;
                case "3518":
                    sendCmdWw3518(WwCmdConfigs.convId, true);
                    break;
                case "3519":
                    sendCmdWw3519(WwCmdConfigs.convId, true);
                    break;
                case "3520":
                    sendCmdWw3520(WwCmdConfigs.convId, true);
                    break;
                case "3521":
                    sendCmdWw3521(WwCmdConfigs.convId, true);
                    break;
                case "3522":
                    sendCmdWw3522(WwCmdConfigs.convId);
                    break;
                case "3523":
                    sendCmdWw3523(WwCmdConfigs.convId);
                    break;
                case "3524":
                    sendCmdWw3524(WwCmdConfigs.convId, true);
                    break;
                case "3525":
                    List<Long> list3525 = new ArrayList<>();
                    list3525.add(WwCmdConfigs.innerId);
                    sendCmdWw3525(WwCmdConfigs.convId, list3525, 0);
                    break;
                case "35251":
                    List<Long> list35251 = new ArrayList<>();
                    list35251.add(WwCmdConfigs.innerId);
                    sendCmdWw3525(WwCmdConfigs.convId, list35251, 1);
                    break;
                case "3526":
                    sendCmdWw3526("2Tw1Q2XL88Rnlfl5");
                    break;
                case "361":
                    logger.info("3.6.1 仅用于消息上报，下发指令无效");
                    break;
                case "362":
                    logger.info("3.6.3 仅用于发送失败、撤回消息上报，下发指令无效，暂时将改功能屏蔽");
                    break;
                case "363":
                    logger.info("3.6.4 用于发送失败的消息重发，demo无法配置参数，下发指令无效");
                    break;
                case "364":
                    logger.info("3.6.4 用于撤回2分钟以内消息，demo无法配置参数，下发指令无效");
                    break;
                case "365":
                    sendCmdWw365(WwCmdConfigs.convId, 0L);
                    break;
                case "366":
                    List<Long> list366 = new ArrayList<>();
                    list366.add(WwCmdConfigs.innerId);
                    sendCmdWw366(WwCmdConfigs.convId, "这里是测试消息", list366, 0L);
                    break;
                case "367":
                    sendCmdWw367(WwCmdConfigs.convId, "http://ww-image.szqhzkcd.com/de64f75e-9cf1-4494-aec5-b3ba24e8efed", 0L);
                    break;
                case "368":
                    sendCmdWw368(WwCmdConfigs.convId, "http://ww-voice.yuanchuan.tech/2ead265e4039841ca898af8383134690.mp3");
                    break;
                case "369":
                    sendCmdWw369(WwCmdConfigs.convId, "http://ww-video.yuanchuan.tech/0f5d299bb7cf146a74aa31c845b98f44.mp4", null);
                    break;
                case "3610":
                    sendCmdWw3610(WwCmdConfigs.convId, "http://ww-file.yuanchuan.tech/测试更新文件名.txt");
                    break;
                case "3611":
                    sendCmdWw3611(WwCmdConfigs.convId, "https://www.baidu.com", "这里是测试", "测试一下了垃圾书的；快疯了就；拉惊世毒妃看；就", "http://ww-image.yuanchuan.tech/avatar0000001.png");
                    break;
                case "3612":
                    sendCmdWw3612(WwCmdConfigs.convId, 123.23, 323.12, "中国北京开发区", "地理位置", 16);
                    break;
                case "3613":
                    sendCmdWw3613(
                            WwCmdConfigs.convId, WwCmdConfigs.weappid, "汽车之家", "gh_bfcd071dba17@app", "汽车之家，买买买",
                            "", 428, 2, "http://wx.qlogo.cn/mmhead/Q3auHgzwzM4wyKJDCic3uMGHVXg4reyribJRtS3lzDuzPV58RsYx2XFg/96",
                            "http://wx.qlogo.cn/mmhead/Q3auHgzwzM4wyKJDCic3uMGHVXg4reyribJRtS3lzDuzPV58RsYx2XFg/96", "pages/home/index.html"
                    );
                    break;
                default:
                    logger.info("不支持该指令：" + name);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("解析数据出错： " + e.getMessage());
        }
    }

    private void send(byte[] data) {
        if (mClient.isOpen()) {
            mClient.send(data);
        } else {
            logger.error("未能连接上server");
        }
    }

    private void sendCmdWw321() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw321, WwCmdPb.CmdWw321.newBuilder().setType(1).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw331() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw331, WwCmdPb.CmdWw331.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw332(String url) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw332, WwCmdPb.CmdWw332.newBuilder().setUrl(url).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw333() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw333, WwCmdPb.CmdWw333.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw334(Long delay) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw334, WwCmdPb.CmdWw334.newBuilder().build().toByteString(), delay);
        send(cmd.toByteArray());
    }

    private void sendCmdWw335() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw335, WwCmdPb.CmdWw335.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw336() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw336, WwCmdPb.CmdWw336.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw337() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw337, WwCmdPb.CmdWw337.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw338(String imageUrl) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw338, WwCmdPb.CmdWw338.newBuilder().setUrl(imageUrl).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw341(long id, String remark, String company, List<String> phones, String desc, String remarkUrl) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw341,
                WwCmdPb.CmdWw341.newBuilder()
                        .setId(id)
                        .setRemark(remark)
                        .setCompany(company)
                        .addAllPhone(phones)
                        .setDesc(desc)
                        .setRemarkUrl(remarkUrl)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw342(long id, String remark, String desc) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw342,
                WwCmdPb.CmdWw342.newBuilder()
                        .setId(id)
                        .setRemark(remark)
                        .setDesc(desc)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw343(String phone) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw343, WwCmdPb.CmdWw343.newBuilder().setPhone(phone).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    // 当client收到了CmdWw343 则需要立马下发344
    private void sendCmdWw344(byte[] originalData, String content) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw344, WwCmdPb.CmdWw344.newBuilder().setOriginalUser(ByteString.copyFrom(originalData)).setContent(content).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw345() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw345, WwCmdPb.CmdWw345.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    // 当client 收到了CmdWw345 需要立即下发346，并且type = 0
    private void sendCmdWw346(long userId, int type) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw346,
                WwCmdPb.CmdWw346.newBuilder()
                        .setUserId(userId)
                        .setTypeValue(type)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw347(long userId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw347, WwCmdPb.CmdWw347.newBuilder().setUserId(userId).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw348() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw348, WwCmdPb.CmdWw348.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw349() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw349, WwCmdPb.CmdWw349.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3410(List<Long> deptIds) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3410, WwCmdPb.CmdWw3410.newBuilder().addAllDeptId(deptIds).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3411() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3411, WwCmdPb.CmdWw3411.newBuilder().setNeedRefresh(false).setOffset(0).setLimit(200).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3412() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3412, WwCmdPb.CmdWw3412.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3413(ByteString data) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3413, WwCmdPb.CmdWw3413.newBuilder().setOriginalUser(data).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3415() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3415, WwCmdPb.CmdWw3415.newBuilder().setKeyword("测试").build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3416() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3416, WwCmdPb.CmdWw3416.newBuilder().setKeyword("刚子").build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3417() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3417, WwCmdPb.CmdWw3417.newBuilder().setKeyword("刚子").build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3418() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3418, WwCmdPb.CmdWw3418.newBuilder().setDeptId(0).setOffset(0L).setLimit(10000).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3419() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3419, WwCmdPb.CmdWw3419.newBuilder().setUserId(WwCmdConfigs.wid).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw352() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(4);
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw352, WwCmdPb.CmdWw352.newBuilder().addAllType(list).build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw353() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw353, WwCmdPb.CmdWw353.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw354() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw354, WwCmdPb.CmdWw354.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw355(List<Long> userIds, String name, int type) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw355,
                WwCmdPb.CmdWw355.newBuilder()
                        .addAllUserId(userIds)
                        .setName(name)
                        .setPersonalRoomType(type)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw356(long convId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw356,
                WwCmdPb.CmdWw356.newBuilder()
                        .setConvId(convId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw357(long convId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw357,
                WwCmdPb.CmdWw357.newBuilder()
                        .setConvId(convId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw358(long convId, String name) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw358,
                WwCmdPb.CmdWw358.newBuilder()
                        .setConvId(convId)
                        .setName(name)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw359(long convId, String nickname) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw359,
                WwCmdPb.CmdWw359.newBuilder()
                        .setConvId(convId)
                        .setNickname(nickname)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3510(long convId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3510,
                WwCmdPb.CmdWw3510.newBuilder()
                        .setConvId(convId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3511(long convId, List<Long> userIds) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3511,
                WwCmdPb.CmdWw3511.newBuilder()
                        .setConvId(convId)
                        .addAllUserId(userIds)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3512() {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3512, WwCmdPb.CmdWw3512.newBuilder().build().toByteString(), 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3513(long convId, List<Long> userIds) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3513,
                WwCmdPb.CmdWw3513.newBuilder()
                        .setConvId(convId)
                        .addAllUserId(userIds)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3514(long convId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3514,
                WwCmdPb.CmdWw3514.newBuilder()
                        .setConvId(convId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3515(long convId, long userId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3515,
                WwCmdPb.CmdWw3515.newBuilder()
                        .setConvId(convId)
                        .setUserId(userId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3516(long convId, String notice) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3516,
                WwCmdPb.CmdWw3516.newBuilder()
                        .setConvId(convId)
                        .setNotice(notice)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3517(long convId, boolean save) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3517,
                WwCmdPb.CmdWw3517.newBuilder()
                        .setConvId(convId)
                        .setSave(save)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3518(long convId, boolean top) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3518,
                WwCmdPb.CmdWw3518.newBuilder()
                        .setConvId(convId)
                        .setTop(top)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3519(long convId, boolean shield) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3519,
                WwCmdPb.CmdWw3519.newBuilder()
                        .setConvId(convId)
                        .setShield(shield)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3520(long convId, boolean forbidden) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3520,
                WwCmdPb.CmdWw3520.newBuilder()
                        .setConvId(convId)
                        .setForbidden(forbidden)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3521(long convId, boolean confirm) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3521,
                WwCmdPb.CmdWw3521.newBuilder()
                        .setConvId(convId)
                        .setConfirm(confirm)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3522(long convId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3522,
                WwCmdPb.CmdWw3522.newBuilder()
                        .setConvId(convId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3523(long convId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3523,
                WwCmdPb.CmdWw3523.newBuilder()
                        .setConvId(convId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3524(long convId, boolean forbidden) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3524,
                WwCmdPb.CmdWw3524.newBuilder()
                        .setConvId(convId)
                        .setForbidden(forbidden)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3525(long convId, List<Long> userIds, int opType) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3525,
                WwCmdPb.CmdWw3525.newBuilder()
                        .setConvId(convId)
                        .addAllUserId(userIds)
                        .setOpType(opType)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3526(String oepnId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3526,
                WwCmdPb.CmdWw3526.newBuilder()
                        .setOpenId(oepnId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw363(long convId, long msgId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw363,
                WwCmdPb.CmdWw363.newBuilder()
                        .setConvId(convId)
                        .setMsgId(msgId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw364(long convId, long msgId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw364,
                WwCmdPb.CmdWw364.newBuilder()
                        .setConvId(convId)
                        .setMsgId(msgId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw365(long convId, long msgId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw365,
                WwCmdPb.CmdWw365.newBuilder()
                        .setConvId(convId)
                        .setMsgId(msgId)
                        .setSource(1)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw366(long id, String text, List<Long> userIds, Long delay) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw366,
                WwCmdPb.CmdWw366.newBuilder()
                        .setId(id)
                        .setText(text)
                        .addAllUserId(userIds)
                        .build().toByteString()
                , delay);
        send(cmd.toByteArray());
    }

    private void sendCmdWw367(long id, String url, Long delay) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw367,
                WwCmdPb.CmdWw367.newBuilder()
                        .setId(id)
                        .setUrl(url)
                        .build().toByteString()
                , delay);
        send(cmd.toByteArray());
    }

    private void sendCmdWw368(long id, String url) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw368,
                WwCmdPb.CmdWw368.newBuilder()
                        .setId(id)
                        .setUrl(url)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw369(long id, String url, String priviewUrl) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw369,
                WwCmdPb.CmdWw369.newBuilder()
                        .setId(id)
                        .setUrl(url)
                        .setPreviewUrl(priviewUrl == null ? "" : priviewUrl)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3610(long id, String url) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3610,
                WwCmdPb.CmdWw3610.newBuilder()
                        .setId(id)
                        .setUrl(url)
                        .setName("测试更新文件名.txt")
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3611(long id, String linkUrl, String title, String desc, String imageUrl) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3611,
                WwCmdPb.CmdWw3611.newBuilder()
                        .setId(id)
                        .setLinkUrl(linkUrl)
                        .setTitle(title)
                        .setDesc(desc)
                        .setImageUrl(imageUrl)
                        .build().toByteString()
        , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3612(long id, double latitude, double longitude, String address, String title, double zoom) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3612,
                WwCmdPb.CmdWw3612.newBuilder()
                        .setId(id)
                        .setLatitude(latitude)
                        .setLongitude(longitude)
                        .setAddress(address)
                        .setTitle(title)
                        .setZoom(zoom)
                        .build().toByteString()
        , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3613(long id, ByteString originalData) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3613,
                WwCmdPb.CmdWw3613.newBuilder().setId(id).setOriginalMini(originalData).build().toByteString()
        , 0L);
        send(cmd.toByteArray());
    }

    private void sendCmdWw3613(
            long id, String appId, String appName, String username, String title,
            String desc, int version, int type, String thumbUrl, String weappIconUrl, String pagePath
    ) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3613,
                WwCmdPb.CmdWw3613.newBuilder()
                        .setId(id)
                        .setAppid(appId)
                        .setPagepath(pagePath)
                        .setAppName(appName)
                        .setUsername(username)
                        .setTitle(title)
                        .setDesc(desc)
                        .setType(type)
                        .setVersion(version)
                        .setThumbUrl(thumbUrl)
                        .setWeappIconUrl(weappIconUrl)
                        .build().toByteString()
        , 0L);
        send(cmd.toByteArray());
    }

    private void  sendCmdWw382(String hongbaoid, String ticket, int vticket, int sceneId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw382,
                WwCmdPb.CmdWw382.newBuilder()
                        .setHongbaoid(hongbaoid)
                        .setHbticket(ticket)
                        .setVidticket(vticket)
                        .setSceneId(sceneId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void  sendCmdWw383(String hongbaoid, String ticket, int vticket, int sceneId) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw383,
                WwCmdPb.CmdWw383.newBuilder()
                        .setHongbaoid(hongbaoid)
                        .setHbticket(ticket)
                        .setVidticket(vticket)
                        .setSceneId(sceneId)
                        .build().toByteString()
                , 0L);
        send(cmd.toByteArray());
    }

    private void  sendCmdWw3102(Boolean accept, ByteString record) {
        CmdPb.Cmd cmd = getCmdBy(WwCmdConfigs.CmdWw3102,
                WwCmdPb.CmdWw3102.newBuilder()
                        .setAccept(accept)
                        .setOriginalRecord(record)
                        .build().toByteString()
        , 0L);
        send(cmd.toByteArray());
    }

    private CmdPb.Cmd getCmdBy(String name, ByteString data, Long delay) {
        return getCmdBy(name, data, WwCmdConfigs.wid, delay);

    }

    private CmdPb.Cmd getCmdBy(String name, ByteString data, Long wid, Long delay) {
        return CmdPb.Cmd.newBuilder()
                .setCid(UUID.randomUUID().toString())
                .setCname(name)
                .setData(data)
                .setWid(wid)
                .setPriority(CmdPb.Cmd.Priority.DEFAULT)
                .setRetryTimes(3)
                .setTimestamp(System.currentTimeMillis())
                .setDelayMillis(delay)
                .build();

    }
}
