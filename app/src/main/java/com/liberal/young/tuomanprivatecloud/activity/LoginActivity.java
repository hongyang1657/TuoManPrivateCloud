package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MainActivity;
import com.liberal.young.tuomanprivatecloud.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/10.
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_login_name)
    EditText tvLoginName;
    @BindView(R.id.tv_login_pwd)
    EditText tvLoginPwd;
    @BindView(R.id.tv_login_forget)
    TextView tvLoginForget;
    @BindView(R.id.iv_login_button)
    ImageView ivLoginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }


    @OnClick({R.id.tv_login_forget, R.id.iv_login_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forget:
                break;
            case R.id.iv_login_button:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("user_type",tvLoginName.getText().toString());
                startActivity(intent);
                break;
        }
    }
}



/*
* Toast.makeText(this, "隐藏密码", Toast.LENGTH_SHORT).show();
                if (isHindingPassword){
                    tvLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());  //显示密码
                    tvLoginPwd.setSelection(tvLoginPwd.length());
                    isHindingPassword = false;
                }else {
                    tvLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());  //隐藏密码
                    isHindingPassword = true;
                    tvLoginPwd.setSelection(tvLoginPwd.length());
                }
* */