package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.bean.MachineResponse;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusMachineFragment;
import com.liberal.young.tuomanprivatecloud.utils.JsonParseUtil;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

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
    private int machineStatus;    //机器开关状态
    private WaitingDialog waitingDialog;
    private int machineId;
    private String res;
    private String titleName;
    private int position;
    private String userLimits;
    private String operableStart;
    private String operableEnd;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warm_up_layout);
        ButterKnife.bind(this);
        titleName = getIntent().getStringExtra("detailMachineName");
        setTitle(titleName, true, MyConstant.TITLE_TYPE_IMG, R.mipmap.back, true, MyConstant.TITLE_TYPE_IMG, R.mipmap.more_title);  //设置当前activity的title
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        machineStatus = getIntent().getIntExtra("machineStatus",0);
        machineId = getIntent().getIntExtra("machineId",-1);
        position = getIntent().getIntExtra("position",-1);
        res = getIntent().getStringExtra("res");
        operableStart = getIntent().getStringExtra("operableStart");
        operableEnd = getIntent().getStringExtra("operableEnd");
        L.i("operableStart"+operableStart);
        L.i("operableEnd"+operableEnd);
        ivTitleLeft.setImageResource(R.mipmap.back);
        application = (MyApplication) getApplication();
        waitingDialog = new WaitingDialog(this,application,"",false);
        userLimits = application.getUserLimits();
        if (userLimits.equals("1")||userLimits.equals("2")){           //陀曼管理员：只能看到机器开关状态
            ivYieldInquire.setImageResource(R.mipmap.yield_cycle);
            rlMachineStateLight.setVisibility(View.VISIBLE);
            rlMachineSwitch.setVisibility(View.GONE);
            ivYieldInquire.setClickable(false);
            if (machineStatus==0){          //机床为关闭状态
                tvStateLightOpen.setVisibility(View.GONE);
                tvStateLightClose.setVisibility(View.VISIBLE);
            }else if (machineStatus==1){     //机床为开启状态
                tvStateLightOpen.setVisibility(View.VISIBLE);
                tvStateLightClose.setVisibility(View.GONE);
            }
        }else {              //用户可以操作机器开关
            rlMachineStateLight.setVisibility(View.GONE);
            rlMachineSwitch.setVisibility(View.VISIBLE);
            if (machineStatus==0){          //机床为关闭状态，变蓝色按钮
                ivMachineSwitch.setImageResource(R.mipmap.warm_on);
                tvMachineSwitch.setText("开启设备");
                tvMachineSwitch.setTextColor(getColor(R.color.colorBlueShade));
            }else if (machineStatus==1){     //机床为开启状态,变红色按钮
                ivMachineSwitch.setImageResource(R.mipmap.warm_off);
                tvMachineSwitch.setText("关闭设备");
                tvMachineSwitch.setTextColor(getColor(R.color.colorTuomanRed));
            }
        }
        if (userLimits.equals("5")||userLimits.equals("1")||userLimits.equals("2")){   //操作工没有右上角选项
            ivTitleRight.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right, R.id.iv_yield_inquire,R.id.iv_machine_switch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                initPopWindow();
                break;
            case R.id.iv_yield_inquire:
                Intent intent = new Intent(WarmUpActivity.this, TableQueryActivity.class);
                intent.putExtra("machineId",machineId);
                intent.putExtra("titleName",titleName);
                startActivity(intent);
                break;
            case R.id.iv_machine_switch:
                initEnterDialog();
                break;
        }
    }


    private Dialog enterDialog = null;
    private View view = null;
    private TextView tvHintContent;
    private TextView tvCancel;
    private TextView tvEnter;
    private void initEnterDialog() {
        if (enterDialog == null) {
            enterDialog = new Dialog(this, R.style.CustomDialog);
            view = LayoutInflater.from(this).inflate(R.layout.my_alert_dialog, null);
            tvHintContent = (TextView) view.findViewById(R.id.tv_hint_content);
            tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            tvEnter = (TextView) view.findViewById(R.id.tv_enter);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterDialog.dismiss();
                }
            });
            tvEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取当前时间
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int min = calendar.get(Calendar.MINUTE);
                    int currentTime = hour*60+min;
                    L.i("currentTime:"+currentTime);
                    L.i("可遇热时间段:"+turnOperableTimeToInt(operableStart)+"----"+turnOperableTimeToInt(operableEnd));
                    if (currentTime>=turnOperableTimeToInt(operableStart)&&currentTime<=turnOperableTimeToInt(operableEnd)){
                        //发开关机床指令
                        if (machineStatus==0){
                            dohttpSwitchMachine(1);   //开启设备
                        }else if (machineStatus==1){
                            dohttpSwitchMachine(0);   //关闭设备
                        }
                    }else {
                        Toast.makeText(WarmUpActivity.this, "不在可预热开启时间段", Toast.LENGTH_SHORT).show();
                    }
                    
                    enterDialog.dismiss();
                }
            });

            enterDialog.setContentView(view);
            enterDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = enterDialog.getWindow().getAttributes();
            L.i(application.getWidth() + "");
            params.width = (int) (application.getWidth() * 0.9);
            Window mWindow = enterDialog.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        if (machineStatus==0){
            tvHintContent.setText("确定开启设备吗？");
        }else if (machineStatus==1){
            tvHintContent.setText("确定关闭设备吗？");
        }
        enterDialog.show();
    }

    //初始化popupWindow
    private PopupWindow popWnd = null;
    private void initPopWindow() {
        if (popWnd == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_layout, null);
            TextView tv1 = (TextView) contentView.findViewById(R.id.tv_pop_1);
            final TextView tv2 = (TextView) contentView.findViewById(R.id.tv_pop_2);
            tv1.setText("绑定操作工");
            tv2.setText("设置");
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //绑定操作工
                    Intent intent = new Intent(WarmUpActivity.this,ManageClientActivity.class);
                    intent.putExtra("machineId",machineId);
                    startActivity(intent);
                }
            });
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //设置机床的参数信息
                    if (userLimits.equals("3")){
                        Intent intent = new Intent(WarmUpActivity.this,ManageClientDetailActivity.class);
                        intent.putExtra("res",res);
                        intent.putExtra("position",position);
                        intent.putExtra("titleName",titleName);
                        intent.putExtra("fromSetting",1);
                        startActivity(intent);
                    }else if (userLimits.equals("4")){
                        tv2.setTextColor(getColor(R.color.colorText));
                        Toast.makeText(WarmUpActivity.this, "当前账号无权限", Toast.LENGTH_SHORT).show();
                    }
                    
                }
            });
            popWnd = new PopupWindow(this);
            popWnd.setOutsideTouchable(true);
            popWnd.setContentView(contentView);
            popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
            popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        if (popWnd.isShowing()) {
            popWnd.dismiss();
        } else {
            popWnd.showAsDropDown(llTitleRight, 0, 0, Gravity.RIGHT);
        }
    }


    //开关机床的网络请求
    private void dohttpSwitchMachine(final int status){
        int[] machineIds = {machineId};

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.switchMachine(machineIds,status,application.getAccessToken()));
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
                        Toast.makeText(WarmUpActivity.this, "操作失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                        waitingDialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse机床: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        if (JsonUtils.getCode(res)==0){    //请求成功
                            Toast.makeText(WarmUpActivity.this, "操作成功！！！", Toast.LENGTH_SHORT).show();
                            if (status==0){          //如果发送的是关闭设备
                                ivMachineSwitch.setImageResource(R.mipmap.warm_on);
                                tvMachineSwitch.setText("开启设备");
                                tvMachineSwitch.setTextColor(getColor(R.color.colorBlueShade));
                                machineStatus = 0;
                            }else if (status==1){     //如果发送的是开启
                                ivMachineSwitch.setImageResource(R.mipmap.warm_off);
                                tvMachineSwitch.setText("关闭设备");
                                tvMachineSwitch.setTextColor(getColor(R.color.colorTuomanRed));
                                machineStatus = 1;
                            }
                        }else {
                            Toast.makeText(WarmUpActivity.this, "操作失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private int turnOperableTimeToInt(String operableTime){
        String hour = operableTime.substring(0,2);
        String min = operableTime.substring(3,5);
        if (hour.substring(0,1).equals("0")){
            hour = hour.substring(1,2);
        }
        if (min.substring(0,1).equals("0")){
            min = min.substring(1,2);
        }
        int h = Integer.parseInt(hour);
        int m = Integer.parseInt(min);
        return h*60+m;
    }
}
