package com.liberal.young.tuomanprivatecloud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.L;

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
                configWIFI();
                break;
        }
    }

    private void configWIFI(){
        try {
            SnifferSmartLinker snifferSmartLinker = SnifferSmartLinker.getInstence();
            snifferSmartLinker.start(this,etSecond.getText().toString(),etFirst.getText().toString());
            snifferSmartLinker.setOnSmartLinkListener(new OnSmartLinkListener() {
                @Override
                public void onLinked(SmartLinkedModule smartLinkedModule) {
                    L.i("onLinked-------ip地址："+smartLinkedModule.getIp());
                    L.i("onLinked-------mac地址："+smartLinkedModule.getMac());

                    Toast.makeText(ConnectWifiActivity.this, "mac地址："+smartLinkedModule.getMac()
                            +"       ip地址："+smartLinkedModule.getIp(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCompleted() {
                    L.i("onCompleted-------------完成配置--------------");
                }

                @Override
                public void onTimeOut() {
                    L.i("onTimeOut------------配置超时---------------");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
