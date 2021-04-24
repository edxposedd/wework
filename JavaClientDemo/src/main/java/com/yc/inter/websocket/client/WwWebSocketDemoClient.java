package com.yc.inter.websocket.client;

import com.yc.inter.websocket.linkserver.WwHookerApis;
import java.util.Scanner;

public class WwWebSocketDemoClient {

    public static void main(String[] args) {
        try {
            WwHookerApis.getInstance().connect();
            Scanner input = new Scanner(System.in);
            String val = null;       // 记录输入度的字符串
            do {
                val = input.next();       // 等待输入值
                WwHookerApis.getInstance().sendMessage(val);
            } while (!val.equals("#"));   // 如果输入的值不版是#就继续输入
            input.close(); // 关闭资源
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}