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
 * Created by Administrator on 2017/3/14.
 */

public class AddClientActivity extends BaseActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client_layout);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        tvTitle.setText("添加客户");
        ivTitleRight.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_title_left, R.id.iv_client_head, R.id.bt_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_client_head:
                break;
            case R.id.bt_up:
                setData();
                break;
        }
    }

    private void setData(){
        clientName = etFirst.getText().toString();
        clientPhoneNumber = etSecond.getText().toString();
        if (clientPhoneNumber==null||clientName==null||clientPhoneNumber.equals("")||clientName.equals("")){
            Toast.makeText(this, "用户名或手机号不能为空", Toast.LENGTH_SHORT).show();
        }else {

            Intent intent = new Intent(AddClientActivity.this,AddClientSecondActivity.class);
            intent.putExtra("clientName",clientName);
            intent.putExtra("clientPhoneNumber",clientPhoneNumber);
            startActivity(intent);
        }
    }
}
