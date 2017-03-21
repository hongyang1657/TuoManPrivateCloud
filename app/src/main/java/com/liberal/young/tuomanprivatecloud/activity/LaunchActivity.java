package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.liberal.young.tuomanprivatecloud.R;

/**
 * Created by Administrator on 2017/3/13.
 */

public class LaunchActivity extends BaseActivity{

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_layout);
        initView();
    }

    private void initView(){
        Message message = new Message();
        message.what = 1;
        handler.sendMessageDelayed(message,1000);
    }
}
