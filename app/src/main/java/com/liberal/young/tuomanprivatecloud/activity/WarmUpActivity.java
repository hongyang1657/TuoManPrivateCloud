package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warm_up_layout);
        ButterKnife.bind(this);
        setTitle("22222", true, MyConstant.TITLE_TYPE_IMG, R.mipmap.play_3x, true, MyConstant.TITLE_TYPE_IMG, R.mipmap.play_3x);  //设置当前activity的title


    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right, R.id.iv_yield_inquire})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                break;
            case R.id.iv_title_right:
                break;
            case R.id.iv_yield_inquire:
                Intent intent = new Intent(WarmUpActivity.this,TableQueryActivity.class);
                startActivity(intent);
                break;
        }
    }
}
