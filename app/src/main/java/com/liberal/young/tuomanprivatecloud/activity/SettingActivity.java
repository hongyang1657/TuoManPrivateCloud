package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;

import java.io.File;
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

public class SettingActivity extends BaseActivity {


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
    @BindView(R.id.tv_setting_warm_time_zone)
    TextView tvSettingWarmTimeZone;
    @BindView(R.id.ll_setting_warm_time_zone)
    LinearLayout llSettingWarmTimeZone;
    @BindView(R.id.tv_setting_auto_close)
    TextView tvSettingAutoClose;
    @BindView(R.id.ll_setting_auto_close)
    LinearLayout llSettingAutoClose;
    @BindView(R.id.tv_setting_worker_num)
    TextView tvSettingWorkerNum;
    @BindView(R.id.ll_setting_worker_num)
    LinearLayout llSettingWorkerNum;
    @BindView(R.id.ll_custom_launch_page)
    LinearLayout llCustomLaunchPage;
    @BindView(R.id.tv_unbind_phone)
    TextView tvUnbindPhone;
    @BindView(R.id.ll_unbind_phone)
    LinearLayout llUnbindPhone;
    @BindView(R.id.ll_app_promotion)
    LinearLayout llAppPromotion;
    @BindView(R.id.ll_logout)
    LinearLayout llLogout;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private static final String url = "http://115.29.172.223:8080/machine/api";
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        ivTitleRight.setVisibility(View.GONE);
        tvTitle.setText("设置");
        ivTitleLeft.setImageResource(R.mipmap.back);
    }

    @OnClick({R.id.iv_title_left, R.id.ll_setting_warm_time_zone, R.id.ll_setting_auto_close, R.id.ll_setting_worker_num, R.id.ll_custom_launch_page, R.id.ll_unbind_phone, R.id.ll_app_promotion, R.id.ll_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.ll_setting_warm_time_zone:
                break;
            case R.id.ll_setting_auto_close:
                break;
            case R.id.ll_setting_worker_num:
                break;
            case R.id.ll_custom_launch_page:
                Intent intent = new Intent(SettingActivity.this,SetCustomLaunchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_unbind_phone:
                break;
            case R.id.ll_app_promotion:
                break;
            case R.id.ll_logout:
                initExitDialog();

                break;
        }
    }

    private Dialog ExitDialog = null;
    private View view = null;
    private TextView tvHintContent;
    private TextView tvCancel;
    private TextView tvEnter;
    private void initExitDialog(){
        if (ExitDialog==null){
            ExitDialog = new Dialog(this,R.style.CustomDialog);
            view = LayoutInflater.from(this).inflate(R.layout.my_alert_dialog,null);
            tvHintContent = (TextView) view.findViewById(R.id.tv_hint_content);
            tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            tvEnter = (TextView) view.findViewById(R.id.tv_enter);
            tvCancel.setOnClickListener(clickListener);
            tvEnter.setOnClickListener(clickListener);
            tvHintContent.setText("确定退出账号吗？");

            ExitDialog.setContentView(view);
            ExitDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = ExitDialog.getWindow().getAttributes();
            L.i(application.getWidth()+"");
            params.width = (int) (application.getWidth()*0.9);
            Window mWindow = ExitDialog.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        ExitDialog.show();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_cancel:
                    ExitDialog.dismiss();
                    break;
                case R.id.tv_enter:
                    //退出账号
                    ExitDialog.dismiss();
                    logOut();
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    };

    private void logOut(){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, JsonUtils.login("","","logout",application.getAccessToken()));  //注销账号
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure:注销： "+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("hy_debug_message", "onResponse:注销： "+res);
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginInformation",MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        String path= Environment.getExternalStorageDirectory()+"/abc.jpg";
                        File mFile=new File(path);
                        //若该文件存在
                        if (mFile.exists()) {
                            mFile.delete();
                        }
                    }
                });
            }
        });
    }
}
