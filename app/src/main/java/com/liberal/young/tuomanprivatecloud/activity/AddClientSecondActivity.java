package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MainActivity;
import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/20.
 */

public class AddClientSecondActivity extends BaseActivity {


    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_client_head)
    ImageView ivClientHead;
    @BindView(R.id.et_first)
    EditText etFirst;
    @BindView(R.id.et_second)
    EditText etSecond;
    @BindView(R.id.bt_shade)
    Button btShade;
    @BindView(R.id.bt_up)
    Button btUp;
    @BindView(R.id.rl_add_enter)
    RelativeLayout rlAddEnter;

    private String clientName = null;
    private String clientPhoneNumber = null;
    private String clientPwd = null;
    private String clientPwdCheck = null;

    private MyApplication myApplication;
    private String userLimit;
    private static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private static final String url = "http://115.29.172.223:8080/machine/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client_second_layout);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        myApplication = (MyApplication) getApplication();
        userLimit = myApplication.getUserLimits();
        ivTitleLeft.setImageResource(R.mipmap.back);
        ivTitleRight.setVisibility(View.GONE);
        if (userLimit.equals("1")||userLimit.equals("2")){
            tvTitle.setText("添加客户");
        }else if (userLimit.equals("3")||userLimit.equals("4")){
            tvTitle.setText("添加操作工");
        }
        Intent intent = getIntent();
        clientName = intent.getStringExtra("clientName");
        clientPhoneNumber = intent.getStringExtra("clientPhoneNumber");
    }

    @OnClick({R.id.iv_title_left, R.id.bt_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.bt_up:
                setData();
                break;
        }
    }

    private void setData(){
        clientPwd = etFirst.getText().toString();
        clientPwdCheck = etSecond.getText().toString();
        if (clientPwd.length()<6||clientPwdCheck.length()<6){
            Toast.makeText(this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
        }else if (!clientPwd.equals(clientPwdCheck)){
            Toast.makeText(this, "确认密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, " "+clientName+" "+clientPhoneNumber+" "+clientPwd, Toast.LENGTH_SHORT).show();
            if (userLimit.equals("1")||userLimit.equals("2")){
                doAddClientHttp(4);
            }else if (userLimit.equals("3")||userLimit.equals("4")){
                doAddClientHttp(5);
            }

        }
    }

    //创建客户或操作工
    private void doAddClientHttp(int roleId){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, JsonUtils.createUser(clientName,clientPwd,
                clientPhoneNumber,myApplication.getAccessToken(),roleId));
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
                        if (JsonUtils.getCode(res)==0){        //操作成功
                            finish();
                        }else {
                            //有问题
                            Toast.makeText(AddClientSecondActivity.this, "错误码："+JsonUtils.getCode(res)+" 错误信息："+JsonUtils.getMsg(res), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
