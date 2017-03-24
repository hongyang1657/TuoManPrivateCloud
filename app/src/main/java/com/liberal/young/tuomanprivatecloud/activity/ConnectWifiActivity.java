package com.liberal.young.tuomanprivatecloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/23.
 */

public class ConnectWifiActivity extends BaseActivity {


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
    @BindView(R.id.bt_shade)
    Button btShade;
    @BindView(R.id.bt_up)
    Button btUp;
    @BindView(R.id.rl_add_enter)
    RelativeLayout rlAddEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_wifi_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }

    @OnClick({R.id.iv_title_left, R.id.bt_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.bt_up:

                break;
        }
    }
}
