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
import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

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
    @BindView(R.id.bt_up)
    Button btUp;
    @BindView(R.id.rl_add_enter)
    RelativeLayout rlAddEnter;

    private WaitingDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_wifi_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        ivTitleLeft.setImageResource(R.mipmap.back);
        ivTitleRight.setImageResource(R.mipmap.wifi_help);
        tvTitle.setText("智能配置");
        waitingDialog = new WaitingDialog(this, (MyApplication) getApplication(),"正在连接，请稍后",false);
    }

    @OnClick({R.id.iv_title_left, R.id.bt_up, R.id.iv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:

                break;
            case R.id.bt_up:
                configWIFI();
                waitingDialog.waiting();
                break;
        }
    }

    private void configWIFI() {
        try {
            SnifferSmartLinker snifferSmartLinker = SnifferSmartLinker.getInstence();
            snifferSmartLinker.start(this, etSecond.getText().toString(), etFirst.getText().toString());
            snifferSmartLinker.setOnSmartLinkListener(new OnSmartLinkListener() {

                @Override
                public void onLinked(SmartLinkedModule smartLinkedModule) {
                    L.i("onLinked-------ip地址：" + smartLinkedModule.getIp());
                    L.i("onLinked-------mac地址：" + smartLinkedModule.getMac());
                    L.i("onLinked-------id：" + smartLinkedModule.getId());
                    L.i("onLinked-------Mid：" + smartLinkedModule.getMid());
                    L.i("onLinked-------ModuleIP：" + smartLinkedModule.getModuleIP());

                    Toast.makeText(ConnectWifiActivity.this, "mac地址：" + smartLinkedModule.getMac()
                            + "       ip地址：" + smartLinkedModule.getIp(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCompleted() {
                    L.i("onCompleted-------------完成配置--------------");
                    waitingDialog.stopWaiting();
                    Toast.makeText(ConnectWifiActivity.this, "完成配置", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTimeOut() {
                    L.i("onTimeOut------------配置超时---------------");
                    waitingDialog.stopWaiting();
                    Toast.makeText(ConnectWifiActivity.this, "配置超时", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
