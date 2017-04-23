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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MainActivity;
import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/13.
 */

public class LaunchActivity extends BaseActivity {

    @BindView(R.id.iv_launch)
    ImageView ivLaunch;

    private int width;
    private int height;

    private MyApplication application;

    private SharedPreferences sharedPreferences;
    private String isCanLogin;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isCanLogin.equals("")||isCanLogin==null){
                        Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        doHttpLogin(sharedPreferences.getString("accessToken",""));
                    }
                    break;
                case 2:
                    Toast.makeText(LaunchActivity.this, "网络连接失败，请检查网络后重新登录", Toast.LENGTH_LONG).show();
                    Message m = new Message();
                    m.what = 3;
                    handler.sendMessageDelayed(m,5000);
                    break;
                case 3:
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
        application = (MyApplication) getApplication();
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        application.setWidth(width);
        application.setHeight(height);
        sharedPreferences = getSharedPreferences("LoginInformation",MODE_PRIVATE);
        isCanLogin = sharedPreferences.getString("accessToken","");

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
        handler.sendMessageDelayed(message, 1000);
    }

    //根据accessToken来登录
    private void doHttpLogin(final String accessToken){
        if (accessToken==null||accessToken.equals("")){
            Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MyConstant.JSON,
                    JsonUtils.login(sharedPreferences.getString("username",""),"","tokenLogin",accessToken));
            Request request = new Request.Builder()
                    .url(MyConstant.SERVER_URL)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("hy_debug_message", "onFailure: "+e.toString());
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    Log.i("hy_debug_message", "onResponse: "+res);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("hy_debug_message", "onResponse: "+res);
                            //Toast.makeText(LaunchActivity.this, res, Toast.LENGTH_SHORT).show();
                            if (JsonUtils.getCode(res)==0){        //登录操作成功
                                application.setUserLimits(String.valueOf(JsonUtils.getRole(res)));    //获取账号的权限等级
                                application.setAccessToken(JsonUtils.getToken(res));     //获取accessToken
                                application.setUsername(JsonUtils.getUsername(res));
                                application.setUserHeadUrl(JsonUtils.getHeadUrl(res));
                                application.setCompanyId(JsonUtils.getCompanyId(res));

                                //用户token存在本地
                                SharedPreferences sharedPreferences = getSharedPreferences("LoginInformation",MODE_PRIVATE);
                                sharedPreferences.edit().putString("username",JsonUtils.getUsername(res))
                                        .putString("accessToken",JsonUtils.getToken(res)).apply();

                                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                //登录有问题
                                Toast.makeText(LaunchActivity.this, "错误码："+JsonUtils.getCode(res)+" 错误信息："+JsonUtils.getMsg(res), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            });
        }
    }
}
