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
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

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
    private WaitingDialog waitingDialog;

    private MyApplication application;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    int time = (int) msg.obj;
                    tvGetCode.setText("("+time+")");
                    tvGetCode.setClickable(false);
                    L.i(""+time);
                    if (time==0){
                        tvGetCode.setText("重新发送");
                        tvGetCode.setClickable(true);
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initView() {
        application = (MyApplication) getApplication();
        waitingDialog = new WaitingDialog(this,application,"正在登录",false);
    }


    @OnClick({R.id.tv_login_forget, R.id.iv_login_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forget:
                initFindPwdDialog();
                break;
            case R.id.iv_login_button:
                //登录的网络请求
                waitingDialog.waiting();
                doHttpLogin(tvLoginName.getText().toString(),tvLoginPwd.getText().toString());

                break;
        }
    }

    private Dialog findPwdDialog = null;
    private View view = null;
    private EditText etUsername;
    private EditText etSecurityCode;
    private EditText etNewPassword;
    private EditText etCheckNewPassword;
    private TextView tvGetCode;
    private Button btSend;
    private void initFindPwdDialog(){
        if (findPwdDialog==null){
            findPwdDialog = new Dialog(this,R.style.CustomDialog);
            view = LayoutInflater.from(this).inflate(R.layout.find_pwd_dialog_layout,null);
            etUsername = (EditText) view.findViewById(R.id.et_username);
            etNewPassword = (EditText) view.findViewById(R.id.et_new_pwd);
            etCheckNewPassword = (EditText) view.findViewById(R.id.et_enter_new_pwd);
            etSecurityCode = (EditText) view.findViewById(R.id.et_security_code);
            tvGetCode = (TextView) view.findViewById(R.id.tv_get_security_code);
            btSend = (Button) view.findViewById(R.id.bt_send);
            tvGetCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取验证码
                    if (etUsername.getText().toString().equals("")){
                        Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        waitingDialog.waiting();
                        sendCode(etUsername.getText().toString());

                    }
                }
            });
            btSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //发送修改密码命令
                    if (etUsername.getText().toString().equals("")||etSecurityCode.getText().toString().equals("")){
                        Toast.makeText(LoginActivity.this, "用户名或验证码不能为空", Toast.LENGTH_SHORT).show();
                    }else {
                        if (!etNewPassword.getText().toString().equals(etCheckNewPassword.getText().toString())){
                            Toast.makeText(LoginActivity.this, "确认密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                        }else {
                            if (etNewPassword.getText().toString().length()<6){
                                Toast.makeText(LoginActivity.this, "密码不能小于6位数", Toast.LENGTH_SHORT).show();
                            }else {
                                changePassword(etUsername.getText().toString(),etNewPassword.getText().toString(),etSecurityCode.getText().toString());
                            }
                        }
                    }

                }
            });
            findPwdDialog.setContentView(view);
            findPwdDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = findPwdDialog.getWindow().getAttributes();
            params.width = (int) (application.getWidth()*0.9);
            Window mWindow = findPwdDialog.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        tvGetCode.setClickable(true);
        findPwdDialog.show();
    }

    private void doHttpLogin(final String username, final String password){
        if (username==null||password==null||username.equals("")||password.equals("")){
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();

        }else {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MyConstant.JSON,JsonUtils.login(username,password,"login",""));
            Request request = new Request.Builder()
                    .url(MyConstant.SERVER_URL)
                    .post(body)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("hy_debug_message", "onFailure: "+e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waitingDialog.stopWaiting();
                            Toast.makeText(LoginActivity.this, "登录失败，请检查网络后重新登录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String res = response.body().string();
                    Log.i("hy_debug_message", "onResponse: "+res);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("hy_debug_message", "onResponse: "+res);
                           // Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
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

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                //登录有问题
                                Toast.makeText(LoginActivity.this, "错误码："+JsonUtils.getCode(res)+" 错误信息："+JsonUtils.getMsg(res), Toast.LENGTH_SHORT).show();
                            }
                            waitingDialog.stopWaiting();
                        }
                    });
                }
            });
        }
    }

    //发送验证码
    private void sendCode(String username){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON,JsonUtils.sendCode(username));
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure: "+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        Toast.makeText(LoginActivity.this, "请检查网络后重试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (JsonUtils.getCode(res)==0){
                            //已经发送验证码，等待接收
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    for (int i=60;i>-1;i--){
                                        try {
                                            sleep(1000);
                                            Message msg = new Message();
                                            msg.what = 1;
                                            msg.obj = i;
                                            handler.sendMessage(msg);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }.start();
                        }
                        waitingDialog.stopWaiting();
                    }
                });
            }
        });
    }

    //修改密码
    private void changePassword(String username,String newPassword,String code){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON,JsonUtils.changePassword("changePassword",username,"",newPassword,code,application.getAccessToken()));
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
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
                        if (JsonUtils.getCode(res)==0){
                            //修改密码成功
                            Toast.makeText(LoginActivity.this, "修改密码成功，请重新登录", Toast.LENGTH_SHORT).show();
                            findPwdDialog.dismiss();
                        }else {
                            Toast.makeText(LoginActivity.this, "错误："+JsonUtils.getMsg(res), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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