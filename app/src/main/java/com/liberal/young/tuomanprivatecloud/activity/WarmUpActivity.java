package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/9.
 */

public class WarmUpActivity extends BaseActivity {


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
    @BindView(R.id.iv_yield_inquire)
    ImageView ivYieldInquire;
    @BindView(R.id.iv_machine_switch)
    ImageView ivMachineSwitch;
    @BindView(R.id.tv_machine_switch)
    TextView tvMachineSwitch;
    @BindView(R.id.rl_machine_switch)
    RelativeLayout rlMachineSwitch;
    @BindView(R.id.tv_state_light_open)
    TextView tvStateLightOpen;
    @BindView(R.id.tv_state_light_close)
    TextView tvStateLightClose;
    @BindView(R.id.rl_machine_state_light)
    RelativeLayout rlMachineStateLight;

    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warm_up_layout);
        ButterKnife.bind(this);
        setTitle("22222", true, MyConstant.TITLE_TYPE_IMG, R.mipmap.play_3x, true, MyConstant.TITLE_TYPE_IMG, R.mipmap.play_3x);  //设置当前activity的title
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        String userLimits = application.getUserLimits();
        if (userLimits.equals("1")||userLimits.equals("2")){           //陀曼管理员：只能看到机器开关状态
            rlMachineStateLight.setVisibility(View.VISIBLE);
            rlMachineSwitch.setVisibility(View.GONE);

        }else {              //用户可以操作机器开关
            rlMachineStateLight.setVisibility(View.GONE);
            rlMachineSwitch.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right, R.id.iv_yield_inquire})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                break;
            case R.id.iv_yield_inquire:
                Intent intent = new Intent(WarmUpActivity.this, TableQueryActivity.class);
                startActivity(intent);
                break;
        }
    }
}
