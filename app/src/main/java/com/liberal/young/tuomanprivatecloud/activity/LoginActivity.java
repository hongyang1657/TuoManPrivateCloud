package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MainActivity;
import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/10.
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_login_name)
    EditText tvLoginName;
    @BindView(R.id.tv_login_pwd)
    EditText tvLoginPwd;
    @BindView(R.id.tv_login_forget)
    TextView tvLoginForget;
    @BindView(R.id.iv_login_button)
    ImageView ivLoginButton;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private static final String url = "http://115.29.172.223:8080/machine/api";
    private MyApplication application;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String loginResponse = (String) msg.obj;


                    break;
                case 2:
                    String loginFail = (String) msg.obj;
                    Toast.makeText(LoginActivity.this, "登录失败，错误信息："+loginFail, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        application.setUserLimits("2");
    }


    @OnClick({R.id.tv_login_forget, R.id.iv_login_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forget:
                //initFindPwdDialog();
                break;
            case R.id.iv_login_button:
                //登录的网络请求
                doHttpLogin(tvLoginName.getText().toString(),tvLoginPwd.getText().toString());
                //登录成功后的操作在handler中处理
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user_type",tvLoginName.getText().toString());
                startActivity(intent);
                break;
        }
    }

    private Dialog findPwdDialog = null;
    private void initFindPwdDialog(){
        if (findPwdDialog==null){
            findPwdDialog = new Dialog(this,R.style.CustomDialog);
            //View view = LayoutInflater.from(this).inflate()
        }
    }

    private void doHttpLogin(String username,String password){
        if (username==null||password==null||username.equals("")||password.equals("")){
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();

        }else {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON,JsonUtils.login(username,password));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("result", "onFailure: "+e.toString());
                    Message message = new Message();
                    message.what = 2;
                    message.obj = e.toString();
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    Log.i("result", "onResponse: "+res);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = res;
                    handler.sendMessage(message);
                }
            });
        }
    }
}



/*
* Toast.makeText(this, "隐藏密码", Toast.LENGTH_SHORT).show();
                if (isHindingPassword){
                    tvLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  //显示密码
                    tvLoginPwd.setSelection(tvLoginPwd.length());
                    isHindingPassword = false;
                }else {
                    tvLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());  //隐藏密码
                    isHindingPassword = true;
                    tvLoginPwd.setSelection(tvLoginPwd.length());
                }
* */