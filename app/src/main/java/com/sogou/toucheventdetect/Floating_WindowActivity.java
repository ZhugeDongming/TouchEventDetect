package com.sogou.toucheventdetect;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.content.Intent;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;


public class Floating_WindowActivity extends AppCompatActivity {

    private Button btn_show;
    private Button btn_hide;
    private Button btn_refresh;
    private Button btn_broadcast;
    private TextView tv_content;
    private EditText txt_edittxt;

    private String MSG_TAG = "FloatWindowActivity";
    private static final String ACTION = "com.android.broadcast.RECEIVER_ACTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating__window);


        btn_show = (Button) findViewById(R.id.btn_show);
        btn_hide = (Button) findViewById(R.id.btn_hide);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        btn_broadcast = (Button) findViewById(R.id.btn_broadcast);
        btn_show.setOnClickListener(listener);
        btn_hide.setOnClickListener(listener);
        btn_refresh.setOnClickListener(listener);
        btn_broadcast.setOnClickListener(listener);

        txt_edittxt = (EditText) findViewById(R.id.txt_editText);
        txt_edittxt.setOnClickListener(listener);
        txt_edittxt.setOnTouchListener(touchlisener);

        tv_content = (TextView) findViewById(R.id.tv_content);

        //启动后台Service
//        Intent show = new Intent(Floating_WindowActivity.this, TopWindowService.class);
//        show.putExtra(TopWindowService.OPERATION,
//                TopWindowService.OPERATION_SHOW);
//        startService(show);
    }

    private View.OnTouchListener touchlisener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch ( v.getId())
            {
                case R.id.txt_editText:
                    Log.d( MSG_TAG , "onTouchEvent Action = " + event.getAction());
                    break;
            }
            return true;
        }
    };

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_show:
                    Intent show = new Intent(Floating_WindowActivity.this, TopWindowService.class);
                    show.putExtra(TopWindowService.OPERATION,
                            TopWindowService.OPERATION_SHOW);
                    startService(show);
                    break;
                case R.id.btn_hide:
                    Intent hide = new Intent(Floating_WindowActivity.this, TopWindowService.class);
                    hide.putExtra(TopWindowService.OPERATION,
                            TopWindowService.OPERATION_HIDE);
                    startService(hide);
                    break;
                case R.id.btn_refresh:
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    tv_content.setText("imei:" + tm.getDeviceId());
                    break;
                case R.id.btn_broadcast:
                    Intent intent = new Intent();
                    intent.setAction(ACTION);
                    sendBroadcast(intent);
            }
        }
    };

}
