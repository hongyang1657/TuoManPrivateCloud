package com.liberal.young.tuomanprivatecloud.activity;

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
 * Created by Administrator on 2017/3/21.
 */

public class ModifiPwdActivity extends BaseActivity {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String url = "http://115.29.172.223:8080/machine/api";
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
    @BindView(R.id.et_first)
    EditText etFirst;
    @BindView(R.id.et_second)
    EditText etSecond;
    @BindView(R.id.et_third)
    EditText etThird;
    @BindView(R.id.bt_up)
    Button btUp;
    @BindView(R.id.rl_add_enter)
    RelativeLayout rlAddEnter;
    private MyApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifi_pwd_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        tvTitle.setText("修改密码");
        ivTitleRight.setVisibility(View.GONE);
        ivTitleLeft.setImageResource(R.mipmap.back);
    }

    @OnClick({R.id.iv_title_left, R.id.bt_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.bt_up:
                String oldPassword = etFirst.getText().toString();
                String newPassword = etSecond.getText().toString();
                String enterPassword = etThird.getText().toString();
                if (oldPassword.equals("") || newPassword.equals("") || enterPassword.equals("")) {
                    Toast.makeText(this, "密码不能为空,请重新输入", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(enterPassword)) {
                    Toast.makeText(this, "确定密码不正确,请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    changePassword(oldPassword, newPassword);
                }
                break;
        }
    }

    //修改密码
    private void changePassword(String oldPassword, String newPassword) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, JsonUtils.changePassword("updatePassword", "", oldPassword, newPassword, "", application.getAccessToken()));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse: " + res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (JsonUtils.getCode(res) == 0) {
                            //修改密码成功
                            Toast.makeText(ModifiPwdActivity.this, "修改密码成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ModifiPwdActivity.this, "错误：" + JsonUtils.getMsg(res), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
