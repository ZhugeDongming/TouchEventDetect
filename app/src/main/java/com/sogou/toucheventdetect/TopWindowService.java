package com.sogou.toucheventdetect;

/**
 * Created by Dongming on 2016/4/8.
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.util.Log;
import com.sogou.utils.*;

public class TopWindowService extends Service
{
    public static final String OPERATION = "operation";
    public static final int OPERATION_SHOW = 100;
    public static final int OPERATION_HIDE = 101;

    private static final int HANDLE_CHECK_ACTIVITY = 200;


    private boolean isAdded = false; // 是否已增加悬浮窗
    private static WindowManager wm;
    private static WindowManager.LayoutParams params;
    private Button btn_floatView;

    private List<String> homeList; // 桌面应用程序包名列表
    private ActivityManager mActivityManager;

    private HookReceiver mhookReceiver;
    private static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";

    UDPServer mudpserver = new UDPServer();
    Handler mTimehandler = new Handler();
    MyHandler myHandler = new MyHandler(Looper.myLooper());
    Runnable runnable = new Runnable() {
        private String MSG_TAG = "TopWindowThread";
        @Override
        public void run() {

        }
    };

    public class UDPServer extends Thread{
        private static final String MSG_TAG = "UDPServer";
        @Override
        public void run() {
            try {
                byte[] buffer = new byte[20];
                DatagramSocket mServerSocket = new DatagramSocket(8803);
                DatagramPacket mPacket = new DatagramPacket(buffer, buffer.length);
                while (true) {
                    mServerSocket.receive(mPacket);
                    Log.d(MSG_TAG , "接收到的长度：" + mPacket.getLength() );
                    Log.d(MSG_TAG , "端口地址：" + mPacket.getAddress().getHostAddress() );
                    String strRead = new String(mPacket.getData()).trim();
                    Log.d(MSG_TAG , "内容：" + strRead );


                    Looper curLooper = Looper.myLooper ();
                    Looper mainLooper = Looper.getMainLooper ();
                    String msg = "" ;
                    if (curLooper== null ){
                        myHandler = new MyHandler(mainLooper);
                    } else {
                        myHandler = new MyHandler(curLooper);
                    }

                    if (strRead.contains("ACTION_UP")){
                        msg = "ACTION_UP";
                    }else if(strRead.contains("ACTION_DOWN")){
                        msg = "ACTION_DOWN";
                    }else {
                        msg = "NO ACTION";
                    }
                    myHandler.removeMessages(0);
                    Message m = myHandler.obtainMessage(1, 1, 1, msg);
                    myHandler.sendMessage(m);
                }
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        }
    }

    private class MyHandler extends Handler{
        public MyHandler(Looper looper){
            super (looper);
        }
        @Override
        public void handleMessage(Message msg) { // 处理消息
            if(msg!=null){
                String strMsg = msg.obj.toString();
                if (strMsg.equals("ACTION_UP")){
                    btn_floatView.setBackgroundResource(R.drawable.green);
                }else if (strMsg.equals("ACTION_DOWN")){
                    btn_floatView.setBackgroundResource(R.drawable.red);
                }else {
                    //do nothing
                }
            }
        }
    }



    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        homeList = getHomes();
        createFloatView();

        mudpserver.start();
    }

    @Override
    public void onDestroy()
    {
//        unregisterReceiver(mhookReceiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int operation = intent.getIntExtra(OPERATION, OPERATION_SHOW);
        switch (operation)
        {
            case OPERATION_SHOW:
                mHandler.removeMessages(HANDLE_CHECK_ACTIVITY);
                mHandler.sendEmptyMessage(HANDLE_CHECK_ACTIVITY);
                break;
            case OPERATION_HIDE:
                mHandler.removeMessages(HANDLE_CHECK_ACTIVITY);
                break;
        }



        mTimehandler.postDelayed(runnable, 2000);

        return  super.onStartCommand(intent, flags, startId);
        //return 1;
    }

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case HANDLE_CHECK_ACTIVITY:
                    if (isHome())
                    {
                        if (!isAdded)
                        {
                            wm.addView(btn_floatView, params);
                            isAdded = true;
                        }
                    } else
                    {
                        if (isAdded)
                        {
                            wm.removeView(btn_floatView);
                            isAdded = false;
                        }
                    }
                    mHandler.sendEmptyMessageDelayed(HANDLE_CHECK_ACTIVITY, 1000);
                    break;
            }
        }
    };

    /**
     * 创建悬浮窗
     */
    private void createFloatView()
    {
        btn_floatView = new Button(getApplicationContext());
        //btn_floatView.setText("警告");
        btn_floatView.setBackgroundResource(R.drawable.green);

        wm = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();

        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		/*
		 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE; 那么优先级会降低一些,
		 * 即拉下通知栏不可见
		 */

        params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 下面的flags属性的效果形同“锁定”。 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

        // 设置悬浮窗的长得宽
        params.width = 200;
        params.height = 200;

        //设置View默认的摆放位置
        params.gravity = Gravity.LEFT | Gravity.TOP;

        //计算输入法键盘高度
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int boardheight = (int) Math.min(0.6472*dm.widthPixels, 0.6 * dm.heightPixels);

        //设置View
        params.x = 0;
        params.y = boardheight - 80;


        // 设置悬浮窗的Touch监听
        btn_floatView.setOnTouchListener(new OnTouchListener()
        {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        btn_floatView.setBackgroundResource(R.drawable.red);


                        break;
                    case MotionEvent.ACTION_UP:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        btn_floatView.setBackgroundResource(R.drawable.green);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        wm.updateViewLayout(btn_floatView, params);
                        break;
                }
                return true;
            }
        });

        wm.addView(btn_floatView, params);
        isAdded = true;
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes()
    {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        // 属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo)
        {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    /**
     * 判断当前界面是否是桌面
     */
    public boolean isHome()
    {
        if (mActivityManager == null)
        {
            mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        }
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return true;
        // return homeList.contains(rti.get(0).topActivity.getPackageName());
    }

}

//class DetectThread extends Thread{
//    Handler mhandler;
//    private String MSG_TAG = "TopWindow";
//    public DetectThread( Handler handler ){
//        mhandler = handler;
//    }
//
//    @Override
//    public void run() {
//        //super.run();
//        ShellCommand command = new ShellCommand();
//        if (command.canSU()){
//
//            while ( true ) {
//                ShellCommand.CommandResult result = command.su.runWaitFor("chmod 666 /dev/input/event*");
//                if (result.success() && result.stderr == null){
//                    ShellCommand.CommandResult r = command.su.runWaitFor("getevent /dev/input/event5");
//                    if (!r.success()) {
//                        Log.d(MSG_TAG, "Error " + r.stderr);
//                    } else {
//                        Log.d(MSG_TAG, "Successfully executed getprop wifi.interface. Result: " + r.stdout);
//                        //this.tetherNetworkDevice = (r.stdout);
//                    }
//
//                }
//            }
//        }
//    }
//}