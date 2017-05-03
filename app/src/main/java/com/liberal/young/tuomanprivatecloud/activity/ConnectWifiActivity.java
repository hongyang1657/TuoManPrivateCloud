package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.liberal.young.tuomanprivatecloud.bean.MachineResponse;
import com.liberal.young.tuomanprivatecloud.utils.JsonParseUtil;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private MyApplication application;
    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_wifi_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        ivTitleLeft.setImageResource(R.mipmap.back);
        ivTitleRight.setImageResource(R.mipmap.wifi_help);
        tvTitle.setText("智能配置");
        waitingDialog = new WaitingDialog(this, application,"正在连接，请稍后",false);
        WifiManager wifiMgr = (WifiManager) getSystemService(this.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        int lenth = wifiId.length();
        wifiId = wifiId.substring(1,lenth-1);
        etFirst.setText(wifiId);
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
                //doHttpSendMac();
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
                    mac = smartLinkedModule.getMac();
                    //mac地址做一下处理

                    Toast.makeText(ConnectWifiActivity.this, "mac地址：" + smartLinkedModule.getMac()
                            + "       ip地址：" + smartLinkedModule.getIp(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCompleted() {
                    L.i("onCompleted-------------完成配置--------------");
                    waitingDialog.stopWaiting();
                    Toast.makeText(ConnectWifiActivity.this, "完成配置", Toast.LENGTH_SHORT).show();
                    //发送mac地址给服务器
                    initSaveDialog();

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

    //配置设备信息提示框
    private Dialog bindMachineDialog = null;
    private View view = null;
    private TextView tvCancelSave;
    private TextView tvEnterSave;
    private EditText etMachineName;
    private EditText etWorkShop;

    private void initSaveDialog() {
        if (bindMachineDialog == null) {
            bindMachineDialog = new Dialog(this, R.style.CustomDialog);
            view = LayoutInflater.from(this).inflate(R.layout.bind_wifi_dialog, null);
            tvCancelSave = (TextView) view.findViewById(R.id.tv_cancel);
            tvEnterSave = (TextView) view.findViewById(R.id.tv_enter);
            etMachineName = (EditText) view.findViewById(R.id.et_bind_machine_name);
            etWorkShop = (EditText) view.findViewById(R.id.et_bind_machine_workshop);
            tvCancelSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消绑定
                    bindMachineDialog.dismiss();
                    finish();
                }
            });
            tvEnterSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //绑定设备

                    doHttpBindMachine(etMachineName.getText().toString(),etWorkShop.getText().toString());
                }
            });

            bindMachineDialog.setContentView(view);
            bindMachineDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = bindMachineDialog.getWindow().getAttributes();
            params.width = (int) (application.getWidth() * 0.9);
            Window mWindow = bindMachineDialog.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        bindMachineDialog.show();
    }

    private void doHttpBindMachine(String name,String workshop){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.bindMachine(name,workshop,turnMacToDec(mac),application.getAccessToken()));
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure: "+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        Toast.makeText(ConnectWifiActivity.this, "绑定失败,请重启设备后再试", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse绑定: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        if (JsonUtils.getCode(res)==0){
                            Toast.makeText(ConnectWifiActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(ConnectWifiActivity.this, "绑定失败,请重启设备后再试", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }


    //
    /*private void doHttpSendMac(){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.getLinkStatus(turnMacToDec(mac),application.getAccessToken()));
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure: "+e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse获取的设备信息: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        if (JsonUtils.getCode(res)==0){

                            initSaveDialog();
                        }else{

                        }
                    }
                });
            }
        });
    }*/


    //处理mac地址为十进制
    private String turnMacToDec(String mac){
        String a = mac.substring(0,2);
        String b = mac.substring(2,4);
        String c = mac.substring(4,6);
        String d = mac.substring(6,8);
        String e = mac.substring(8,10);
        String f = mac.substring(10,12);

        a = Integer.parseInt(a, 16)+"";
        b = Integer.parseInt(b, 16)+"";
        c = Integer.parseInt(c, 16)+"";
        d = Integer.parseInt(d, 16)+"";
        e = Integer.parseInt(e, 16)+"";
        f = Integer.parseInt(f, 16)+"";

        L.i("mac: "+a+":"+b+":"+c+":"+d+":"+e+":"+f);

        return a+":"+b+":"+c+":"+d+":"+e+":"+f;
    }

}
