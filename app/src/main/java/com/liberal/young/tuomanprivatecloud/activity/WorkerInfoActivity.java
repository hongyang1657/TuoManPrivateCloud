package com.liberal.young.tuomanprivatecloud.activity;

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
 * Created by Administrator on 2017/3/20.
 */

public class WorkerInfoActivity extends BaseActivity {


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
    @BindView(R.id.iv_worker_head)
    ImageView ivWorkerHead;
    @BindView(R.id.ll_reset_worker_pwd)
    LinearLayout llResetWorkerPwd;
    @BindView(R.id.tv_worker_name)
    TextView tvWorkerName;
    @BindView(R.id.tv_worker_phone)
    TextView tvWorkerPhone;
    @BindView(R.id.tv_worker_number)
    TextView tvWorkerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_info_layout);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tvTitle.setText("操作工信息");
        ivTitleRight.setVisibility(View.GONE);
        tvWorkerName.setText(getIntent().getStringExtra("workerName"));
        tvWorkerPhone.setText(getIntent().getStringExtra("workerPhone"));

    }

    @OnClick({R.id.iv_title_left, R.id.ll_reset_worker_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.ll_reset_worker_pwd:
                break;
        }
    }
}
