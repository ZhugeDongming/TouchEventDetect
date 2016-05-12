package com.sogou.utils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


import android.app.Application;
import android.content.Intent;
import android.view.InputEvent;
import android.view.MotionEvent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by deadwalk on 16/4/10.
 */
public class hook implements IXposedHookLoadPackage {
    private static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";
    private Application fristApplication;
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable{
        //将包名不是com.example.login的应用剔除掉
        //if(!lpparam.packageName.equals("com.sogou.toucheventdetect")){
        XposedHelpers.findAndHookMethod( "android.telephony.TelephonyManager", lpparam.classLoader, "getDeviceId",new XC_MethodReplacement(){
            @Override
            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable{
                //XposedBridge.log( "getDeviceId Hook Success!");
                return"Maomao is pig";
            }
        });


//        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager",
//                lpparam.classLoader,
//                "getInstalledPackages",
//                int.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//                        XposedBridge.log( df.format(new Date()) + lpparam.packageName + " getInstalledPackages Hook Success!");
//                        super.beforeHookedMethod(param);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable{
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );
//
//        XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager",
//                lpparam.classLoader,
//                "getInstalledApplications",
//                int.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//                        XposedBridge.log( df.format(new Date()) + lpparam.packageName + " getInstalledApplications Hook Success!");
//                        super.beforeHookedMethod(param);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable{
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );

        /*以下Hook方法可行*/
        XposedHelpers.findAndHookMethod("com.android.server.wm.PointerEventDispatcher",
                lpparam.classLoader,
                "onInputEvent",
                InputEvent.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        //XposedBridge.log( "com.android.server.wm.PointerEventDispatcher Hook Success!");
                        MotionEvent inputEvent = (MotionEvent) param.args[0];
                        int k = inputEvent.getAction();
                        DecimalFormat df = new DecimalFormat("0.00");
                        float x = inputEvent.getX();
                        float y = inputEvent.getY();
                        SocketAddress address = new InetSocketAddress("127.0.0.1", 8803);
                        switch (k & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                //XposedBridge.log("ACTION_DOWN " + k + " x=" + df.format(x) + "  y=" + df.format(y));
                                try {
                                    String string = "ACTION_DOWN";
                                    byte[] data = string.getBytes();
                                    DatagramPacket mPacket = new DatagramPacket(data, data.length,address);
                                    DatagramSocket mSocket = new DatagramSocket();
                                    mSocket.send(mPacket);
                                    mSocket.close();
                                } catch (UnsupportedEncodingException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (SocketException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                //XposedBridge.log("ACTION_UP " + k + " x=" + df.format(x) + "  y=" + df.format(y));
                                    try {
                                        String string = "ACTION_UP";
                                        byte[] data = string.getBytes();
                                        DatagramPacket mPacket = new DatagramPacket(data, data.length,address);
                                        DatagramSocket mSocket = new DatagramSocket();
                                        mSocket.send(mPacket);
                                        mSocket.close();
                                    } catch (UnsupportedEncodingException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (SocketException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                //XposedBridge.log("ACTION_POINTER_UP " + k + " x=" + df.format(x) + "  y=" + df.format(y));
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                //XposedBridge.log("ACTION_POINTER_DOWN " + k + " x=" + df.format(x) + "  y=" + df.format(y));
                                break;
                        }
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable{
                        super.afterHookedMethod(param);
                    }
                }
        );

//        XposedHelpers.findAndHookMethod("android.view.View",
//                lpparam.classLoader,
//                "dispatchTouchEvent",
//                MotionEvent.class,
////                long.class,
////                boolean.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        XposedBridge.log( "android.view.View Hook Success!");
//                        MotionEvent inputEvent = (MotionEvent) param.args[0];
//                        int k = inputEvent.getAction();
//                        DecimalFormat df = new DecimalFormat("0.00");
//                        float x = inputEvent.getX();
//                        float y = inputEvent.getY();
//                        switch (k & MotionEvent.ACTION_MASK) {
//                            case MotionEvent.ACTION_DOWN:
//                                XposedBridge.log("ACTION_DOWN " + k + " x=" + df.format(x) + "  y=" + df.format(y));
//                                break;
//                            case MotionEvent.ACTION_UP:
//                                XposedBridge.log("ACTION_UP " + k + " x=" + df.format(x) + "  y=" + df.format(y));
//                                break;
//                            case MotionEvent.ACTION_POINTER_UP:
//                                XposedBridge.log("ACTION_POINTER_UP " + k + " x=" + df.format(x) + "  y=" + df.format(y));
//                                break;
//                            case MotionEvent.ACTION_POINTER_DOWN:
//                                XposedBridge.log("ACTION_POINTER_DOWN " + k + " x=" + df.format(x) + "  y=" + df.format(y));
//                                break;
//                        }
//                        super.beforeHookedMethod(param);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable{
//                        super.afterHookedMethod(param);
//                    }
//                }
//        );

        XposedBridge.log("Loadedapp:"+lpparam.packageName);
    }

}
