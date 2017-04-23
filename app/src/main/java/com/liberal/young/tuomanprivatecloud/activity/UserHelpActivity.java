package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator-12:28 on 2017/4/22.
 * Description:
 * Blog：www.qiuchengjia.cn
 * Author: Young_H
 */
public class UserHelpActivity extends Activity {


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
    @BindView(R.id.et_user_help)
    EditText etUserHelp;
    @BindView(R.id.tv_send_user_help)
    TextView tvSendUserHelp;

    private MyApplication application;
    private WaitingDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_help_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        waitingDialog = new WaitingDialog(this,application,"",false);
        tvTitle.setText("帮助反馈");
        ivTitleLeft.setImageResource(R.mipmap.back);
        ivTitleRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_left, R.id.tv_send_user_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.tv_send_user_help:
                waitingDialog.waiting();
                dohttpSendHelpMsg();
                break;
        }
    }

    private void dohttpSendHelpMsg(){
        String msg = etUserHelp.getText().toString();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON,JsonUtils.createSuggest(application.getCompanyId()
                ,msg,application.getAccessToken()));
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
                        Toast.makeText(UserHelpActivity.this, "操作失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                        waitingDialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse提交意见: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        if (JsonUtils.getCode(res)==0){    //请求成功
                            Toast.makeText(UserHelpActivity.this, "提交成功！感谢您的意见反馈，我们会收到意见并及时处理！", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Toast.makeText(UserHelpActivity.this, "操作失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
