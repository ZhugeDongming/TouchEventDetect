package com.sogou.toucheventdetect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Dongming on 2016/4/11.
 */
public class HookReceiver extends BroadcastReceiver {
    private static final String MSG_TAG = "ReceiverTest";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d( MSG_TAG , "hookReceiver onReceiver========================>");
//        Intent show = new Intent(Floating_WindowActivity.this, TopWindowService.class);
//        show.putExtra(TopWindowService.OPERATION,
//                TopWindowService.OPERATION_SHOW);
//        startService(show);
    }
}
