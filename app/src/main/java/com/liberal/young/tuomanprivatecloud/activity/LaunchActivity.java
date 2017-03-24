package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.liberal.young.tuomanprivatecloud.R;

import java.io.ByteArrayInputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/13.
 */

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.iv_launch)
    ImageView ivLaunch;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
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
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        String path= Environment.getExternalStorageDirectory()+"/abc.jpg";
        File mFile=new File(path);
        //若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            Log.i("result", "getBitmap: 找到文件");
            ivLaunch.setImageBitmap(bitmap);
        }

        Message message = new Message();
        message.what = 1;
        handler.sendMessageDelayed(message, 3000);
    }
}
