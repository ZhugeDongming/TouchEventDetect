package com.sogou.toucheventdetect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

public class Floating_WindowActivity extends AppCompatActivity {

    private Button btn_show;
    private Button btn_hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floating__window);

        btn_show = (Button) findViewById(R.id.btn_show);
        btn_hide = (Button) findViewById(R.id.btn_hide);
        btn_show.setOnClickListener(listener);
        btn_hide.setOnClickListener(listener);

    }

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
            }
        }
    };

}
