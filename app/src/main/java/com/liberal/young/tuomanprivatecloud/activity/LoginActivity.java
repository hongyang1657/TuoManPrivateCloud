package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();

    }


    @OnClick({R.id.tv_login_forget, R.id.iv_login_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forget:
                initFindPwdDialog();
                break;
            case R.id.iv_login_button:
                //登录的网络请求
                doHttpLogin(tvLoginName.getText().toString(),tvLoginPwd.getText().toString());
                break;
        }
    }

    private Dialog findPwdDialog = null;
    private View view = null;
    private EditText etUsername;
    private EditText etPhoneNum;
    private EditText etSecurityCode;
    private TextView tvGetCode;
    private Button btSend;
    private void initFindPwdDialog(){
        if (findPwdDialog==null){
            findPwdDialog = new Dialog(this,R.style.CustomDialog);
            view = LayoutInflater.from(this).inflate(R.layout.find_pwd_dialog_layout,null);
            etUsername = (EditText) view.findViewById(R.id.et_username);
            etPhoneNum = (EditText) view.findViewById(R.id.et_phone_num);
            etSecurityCode = (EditText) view.findViewById(R.id.et_security_code);
            tvGetCode = (TextView) view.findViewById(R.id.tv_get_security_code);
            btSend = (Button) view.findViewById(R.id.bt_send);
            tvGetCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取验证码
                }
            });
            btSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //验证信息，跳转到修改密码页面

                }
            });
            findPwdDialog.setContentView(view);
            findPwdDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = findPwdDialog.getWindow().getAttributes();
            params.width = (int) (application.getWidth()*0.9);
            Window mWindow = findPwdDialog.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        findPwdDialog.show();
    }

    private void doHttpLogin(final String username, final String password){
        if (username==null||password==null||username.equals("")||password.equals("")){
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();

        }else {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(JSON,JsonUtils.login(username,password,"login",""));
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("hy_debug_message", "onFailure: "+e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    Log.i("hy_debug_message", "onResponse: "+res);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("hy_debug_message", "onResponse: "+res);
                            Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
                            if (JsonUtils.getCode(res)==0){        //登录操作成功
                                application.setUserLimits(String.valueOf(JsonUtils.getRole(res)));    //获取账号的权限等级
                                application.setAccessToken(JsonUtils.getToken(res));     //获取accessToken
                                //用户名密码存在本地
                                SharedPreferences sharedPreferences = getSharedPreferences("LoginInformation",MODE_PRIVATE);
                                sharedPreferences.edit().putString("username",username).putString("password",password).commit();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                //登录有问题
                                Toast.makeText(LoginActivity.this, "错误码："+JsonUtils.getCode(res)+" 错误信息："+JsonUtils.getMsg(res), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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