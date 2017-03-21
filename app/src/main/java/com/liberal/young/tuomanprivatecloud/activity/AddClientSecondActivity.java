package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client_second_layout);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tvTitle.setText("添加客户");
        ivTitleRight.setVisibility(View.GONE);
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
        }
    }
}
