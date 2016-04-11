//package com.sogou.utils;
//
///**
// * Created by Dongming on 2016/4/11.
// */
//
//import android.content.Intent;
//import android.os.Message;
//import android.util.Log;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.SocketException;
//
//
//public class UDPServer extends Thread{
//    private static final String MSG_TAG = "UDPServer";
//    //private byte[] buffer = new byte[20];
//    @Override
//    public void run() {
//        try {
//            byte[] buffer = new byte[20];
//            DatagramSocket mServerSocket = new DatagramSocket(8803);
//            DatagramPacket mPacket = new DatagramPacket(buffer, buffer.length);
//            while (true) {
//                mServerSocket.receive(mPacket);
//                Log.d(MSG_TAG , "接收到的长度：" + mPacket.getLength() );
//                Log.d(MSG_TAG , "端口地址：" + mPacket.getAddress().getHostAddress() );
//                String strRead = new String(mPacket.getData()).trim();
//                Log.d(MSG_TAG , "内容：" + strRead );
//                if (strRead.contains("ACTION_UP")){
//
//                }else if(strRead.contains("ACTION_DOWN")){
//
//                }
//
//            }
//        } catch (SocketException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
////        } catch (ClassNotFoundException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//
//    }
//}