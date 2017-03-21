package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ivTitleRight.setVisibility(View.GONE);
        tvTitle.setText("设置");
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
                break;
        }
    }
}
