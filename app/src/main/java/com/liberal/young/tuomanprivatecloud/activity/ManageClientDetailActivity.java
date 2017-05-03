package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.bean.MachineResponse;
import com.liberal.young.tuomanprivatecloud.utils.CircleTransformPicasso;
import com.liberal.young.tuomanprivatecloud.utils.JsonParseUtil;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;
import com.squareup.picasso.Picasso;

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
 * 客户信息的详情页面，可以设置每条生产线的参数
 * Created by Administrator on 2017/3/20.
 */

public class ManageClientDetailActivity extends BaseActivity {


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
    @BindView(R.id.iv_manage_client_detail_head)
    ImageView ivManageClientDetailHead;
    @BindView(R.id.tv_manage_client_detail_name)
    TextView tvManageClientDetailName;
    @BindView(R.id.tv_manage_client_select_warm_time)
    TextView tvManageClientSelectWarmTime;
    @BindView(R.id.ll_manage_client_detail_warm_time_slot)
    LinearLayout llManageClientDetailWarmTimeSlot;
    @BindView(R.id.tv_manage_client_auto_close_time)
    TextView tvManageClientAutoCloseTime;
    @BindView(R.id.ll_manage_client_detail_auto_close_time)
    LinearLayout llManageClientDetailAutoCloseTime;
    @BindView(R.id.tv_manage_client_worker_max_num)
    TextView tvManageClientWorkerMaxNum;
    @BindView(R.id.ll_manage_client_detail_worker_max_num)
    LinearLayout llManageClientDetailWorkerMaxNum;
    @BindView(R.id.sw_manage_client_impower)
    Switch swManageClientImpower;
    @BindView(R.id.ll_manage_client_impower)
    LinearLayout llManageClientImpower;
    @BindView(R.id.et_standard_yield)
    EditText etStandardYield;
    @BindView(R.id.ll_manage_client_standard_yield)
    LinearLayout llManageClientStandardYield;
    @BindView(R.id.tv_blank)
    TextView tvBlank;

    private Dialog dialogWarmTimeSlot = null;
    private Dialog dialogAutoClose = null;
    private Dialog dialogWorkerMaxNum = null;
    private View viewWarmTimeSlot = null;
    private View viewAutoClose = null;
    private View viewWorkerMaxNum = null;
    private int width;
    private int height;
    private MyApplication application;
    private static int userId;
    private WaitingDialog dialog;
    private String detailMachineName;
    private int fromSetting = 0;
    private boolean isImpower = false;
    private String res;
    private String titleName;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_client_detail_layout);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        detailMachineName = getIntent().getStringExtra("detailMachineName");
        application = (MyApplication) getApplication();
        ivTitleLeft.setImageResource(R.mipmap.back);
        ivTitleRight.setVisibility(View.GONE);
        tvTitle.setText(detailMachineName);
        width = application.getWidth();
        height = application.getHeight();
        tvManageClientDetailName.setText(getIntent().getStringExtra("clientName"));
        Picasso.with(this).load(getIntent().getStringExtra("logo"))
                .resize(300, 300)
                .transform(new CircleTransformPicasso())
                .placeholder(R.mipmap.head_big)
                .error(R.mipmap.head_big).into(ivManageClientDetailHead);
        int roleId = getIntent().getIntExtra("roleId", -1);
        if (roleId == 3) {
            swManageClientImpower.setChecked(true);
        } else if (roleId == 4) {
            swManageClientImpower.setChecked(false);
        }
        userId = getIntent().getIntExtra("id", -1);
        fromSetting = getIntent().getIntExtra("fromSetting", 0);
        swManageClientImpower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isImpower = true;
                } else {
                    isImpower = false;
                }
            }
        });
        dialog = new WaitingDialog(this, application, "", false);
        //从预热开关页面跳转过来
        if (fromSetting == 1) {
            titleName = getIntent().getStringExtra("titleName");
            res = getIntent().getStringExtra("res");
            position = getIntent().getIntExtra("position",-1);
            JsonParseUtil jsonParseUtil = new JsonParseUtil(res);
            MachineResponse machineResponse = jsonParseUtil.parseMachineSearchJson();
            startTime = machineResponse.getResult().get(position).getOperableStart();
            endTime = machineResponse.getResult().get(position).getOperableEnd();
            int perWorkerNumber = machineResponse.getResult().get(position).getUserTop();
            int perStandardYield = machineResponse.getResult().get(position).getForecast();
            String autoCloseTime = machineResponse.getResult().get(position).getCloseTime();

            ivManageClientDetailHead.setVisibility(View.GONE);
            tvTitle.setText("设置");
            llManageClientImpower.setVisibility(View.GONE);
            llManageClientStandardYield.setVisibility(View.VISIBLE);
            tvManageClientDetailName.setText(titleName);
            tvBlank.setVisibility(View.VISIBLE);
            tvManageClientSelectWarmTime.setText(startTime + " ~ " + endTime);
            tvManageClientWorkerMaxNum.setText(perWorkerNumber + "");
            etStandardYield.setText(perStandardYield+"");
            tvManageClientAutoCloseTime.setText(autoCloseTime);
        }else {
            tvManageClientWorkerMaxNum.setText(getIntent().getIntExtra("userTop",0)+"");
            tvManageClientAutoCloseTime.setText(getIntent().getStringExtra("closeTime"));
            tvManageClientSelectWarmTime.setText(getIntent().getStringExtra("operableStart")+ " ~ " +getIntent().getStringExtra("operableEnd"));

        }
    }

    @OnClick({R.id.iv_title_left, R.id.ll_manage_client_detail_warm_time_slot, R.id.ll_manage_client_detail_auto_close_time, R.id.ll_manage_client_detail_worker_max_num})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                //返回上一页面时，弹出是否保存设置dialog
                initSaveDialog();
                break;
            case R.id.ll_manage_client_detail_warm_time_slot:
                initWarmTimeslotDialog();
                break;
            case R.id.ll_manage_client_detail_auto_close_time:
                initAutoCloseTimeDialog();
                break;
            case R.id.ll_manage_client_detail_worker_max_num:
                initWorkerMaxNumDialog();
                break;
        }
    }

    private TimePicker tpWarmTime;
    private TextView tvCancel0;
    private TextView tvEnter0;
    private RadioButton rbStartTime;
    private RadioButton rbEndTime;
    private int checkedItem = 1;
    private String startTime;
    private String endTime;

    private void initWarmTimeslotDialog() {
        if (dialogWarmTimeSlot == null) {
            dialogWarmTimeSlot = new Dialog(this, R.style.CustomDialog);
            viewWarmTimeSlot = LayoutInflater.from(this).inflate(R.layout.warm_time_dialog_layout, null);
            tpWarmTime = (TimePicker) viewWarmTimeSlot.findViewById(R.id.tp_auto_close);
            tvCancel0 = (TextView) viewWarmTimeSlot.findViewById(R.id.tv_cancel);
            tvEnter0 = (TextView) viewWarmTimeSlot.findViewById(R.id.tv_enter);
            rbStartTime = (RadioButton) viewWarmTimeSlot.findViewById(R.id.rb_start_time);
            rbEndTime = (RadioButton) viewWarmTimeSlot.findViewById(R.id.rb_end_time);
            rbStartTime.setBackgroundResource(R.color.colorBule);
            rbEndTime.setBackgroundResource(R.color.colorWhite);
            rbStartTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedItem = 1;
                    rbStartTime.setBackgroundResource(R.color.colorBule);
                    rbEndTime.setBackgroundResource(R.color.colorWhite);
                }
            });
            rbEndTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedItem = 2;
                    rbEndTime.setBackgroundResource(R.color.colorBule);
                    rbStartTime.setBackgroundResource(R.color.colorWhite);
                }
            });
            tpWarmTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    if (checkedItem == 1) {
                        startTime = getTimePickerTime();
                        rbStartTime.setText(startTime);
                    } else if (checkedItem == 2) {
                        endTime = getTimePickerTime();
                        rbEndTime.setText(endTime);
                    }
                }
            });

            tvCancel0.setOnClickListener(onclickListener);
            tvEnter0.setOnClickListener(onclickListener);
            dialogWarmTimeSlot.setContentView(viewWarmTimeSlot);
            dialogWarmTimeSlot.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = dialogWarmTimeSlot.getWindow().getAttributes();
            params.width = (int) (width * 0.9);
            Window mWindow = dialogWarmTimeSlot.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        dialogWarmTimeSlot.show();
    }

    private TimePicker tpAutoClose;
    private TextView tvCancel1;
    private TextView tvEnter1;

    private void initAutoCloseTimeDialog() {
        if (dialogAutoClose == null) {
            dialogAutoClose = new Dialog(this, R.style.CustomDialog);
            viewAutoClose = LayoutInflater.from(this).inflate(R.layout.auto_close_time_dialog_layout, null);
            tpAutoClose = (TimePicker) viewAutoClose.findViewById(R.id.tp_auto_close);

            tvCancel1 = (TextView) viewAutoClose.findViewById(R.id.tv_cancel);
            tvEnter1 = (TextView) viewAutoClose.findViewById(R.id.tv_enter);
            tvCancel1.setOnClickListener(onclickListener);
            tvEnter1.setOnClickListener(onclickListener);
            dialogAutoClose.setContentView(viewAutoClose);
            dialogAutoClose.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = dialogAutoClose.getWindow().getAttributes();
            params.width = (int) (width * 0.9);
            Window mWindow = dialogAutoClose.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        dialogAutoClose.show();
    }

    private Spinner spinnerWorkerNum;
    private TextView tvCancel;
    private TextView tvEnter;
    private int workerNumber = 0;

    private void initWorkerMaxNumDialog() {
        if (dialogWorkerMaxNum == null) {
            dialogWorkerMaxNum = new Dialog(this, R.style.CustomDialog);
            viewWorkerMaxNum = LayoutInflater.from(this).inflate(R.layout.worker_setting_dialog_layout, null);
            spinnerWorkerNum = (Spinner) viewWorkerMaxNum.findViewById(R.id.spinner_worker_num);
            tvCancel = (TextView) viewWorkerMaxNum.findViewById(R.id.tv_cancel);
            tvEnter = (TextView) viewWorkerMaxNum.findViewById(R.id.tv_enter);
            tvCancel.setOnClickListener(onclickListener);
            tvEnter.setOnClickListener(onclickListener);
            spinnerWorkerNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    workerNumber = position + 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    workerNumber = 0;
                }
            });

            dialogWorkerMaxNum.setContentView(viewWorkerMaxNum);
            dialogWorkerMaxNum.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = dialogWorkerMaxNum.getWindow().getAttributes();
            params.width = (int) (width * 0.9);
            Window mWindow = dialogWorkerMaxNum.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        dialogWorkerMaxNum.show();

    }

    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_cancel:
                    if (dialogWorkerMaxNum != null && dialogWorkerMaxNum.isShowing()) {
                        dialogWorkerMaxNum.dismiss();
                    }
                    if (dialogAutoClose != null && dialogAutoClose.isShowing()) {
                        dialogAutoClose.dismiss();
                    }
                    if (dialogWarmTimeSlot != null && dialogWarmTimeSlot.isShowing()) {
                        dialogWarmTimeSlot.dismiss();
                    }
                    break;
                case R.id.tv_enter:
                    if (dialogWorkerMaxNum != null && dialogWorkerMaxNum.isShowing()) {
                        dialogWorkerMaxNum.dismiss();
                        tvManageClientWorkerMaxNum.setText(workerNumber + "");
                    }
                    if (dialogAutoClose != null && dialogAutoClose.isShowing()) {
                        dialogAutoClose.dismiss();
                        setAutoCloseTime();
                    }
                    if (dialogWarmTimeSlot != null && dialogWarmTimeSlot.isShowing()) {
                        dialogWarmTimeSlot.dismiss();
                        tvManageClientSelectWarmTime.setText(startTime + " ~ " + endTime);
                    }

                    break;
            }
        }
    };

    //是否保存提示框
    private Dialog ExitDialog = null;
    private View view = null;
    private TextView tvHintContent;
    private TextView tvCancelSave;
    private TextView tvEnterSave;

    private void initSaveDialog() {
        if (ExitDialog == null) {
            ExitDialog = new Dialog(this, R.style.CustomDialog);
            view = LayoutInflater.from(this).inflate(R.layout.my_alert_dialog, null);
            tvHintContent = (TextView) view.findViewById(R.id.tv_hint_content);
            tvCancelSave = (TextView) view.findViewById(R.id.tv_cancel);
            tvEnterSave = (TextView) view.findViewById(R.id.tv_enter);
            tvCancelSave.setOnClickListener(clickListener);
            tvEnterSave.setOnClickListener(clickListener);
            tvHintContent.setText("是否保存当前设置");

            ExitDialog.setContentView(view);
            ExitDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = ExitDialog.getWindow().getAttributes();
            params.width = (int) (application.getWidth() * 0.9);
            Window mWindow = ExitDialog.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        ExitDialog.show();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_cancel:
                    ExitDialog.dismiss();
                    finish();
                    break;
                case R.id.tv_enter:
                    //保存并返回上一activity
                    ExitDialog.dismiss();
                    if (fromSetting == 1){
                        doHttpUpdateMachine();
                    }else {
                        if (isImpower) {
                            doHttpSave(3);
                        } else {
                            doHttpSave(4);
                        }
                        dialog.waiting();
                    }
                    break;
            }
        }
    };

    //修改用户权限
    private void doHttpSave(int roleId) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.updateRole(userId, roleId, application.getAccessToken()));  //注销账号
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure:修改权限： " + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("hy_debug_message", "onResponse:修改权限： " + res);

                        dialog.stopWaiting();
                        finish();
                    }
                });
            }
        });
    }

    //--------------修改机床开关机时间，操作工人数等信息--------------
    private void doHttpUpdateMachine() {
        //获取设定好的参数
        JsonParseUtil jsonParseUtil = new JsonParseUtil(res);
        MachineResponse machineResponse = jsonParseUtil.parseMachineSearchJson();
        int machineId = machineResponse.getResult().get(position).getId();
        int userTop = Integer.parseInt(tvManageClientWorkerMaxNum.getText().toString());
        String closeTime = tvManageClientAutoCloseTime.getText().toString();
        long foreCast = Long.parseLong(etStandardYield.getText().toString());

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.updateMachine(machineId
                ,userTop,closeTime,startTime,endTime,foreCast,application.getAccessToken()));
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure:更新机床： " + e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("hy_debug_message", "onResponse:更新机床： " + res);

                        dialog.stopWaiting();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            //返回上一页面时，弹出是否保存设置dialog
            initSaveDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //得到自动关闭的时间并设置
    private void setAutoCloseTime() {
        int hour = tpAutoClose.getCurrentHour();
        int min = tpAutoClose.getCurrentMinute();
        String strHour = "";
        String strMin = "";
        if (hour < 10) {
            strHour = "0" + hour;
        } else {
            strHour = String.valueOf(hour);
        }
        if (min < 10) {
            strMin = "0" + min;
        } else {
            strMin = String.valueOf(min);
        }
        tvManageClientAutoCloseTime.setText(strHour + ":" + strMin);
    }

    //设置时间
    private String getTimePickerTime() {
        int hour = tpWarmTime.getCurrentHour();
        int min = tpWarmTime.getCurrentMinute();
        String strHour = "";
        String strMin = "";
        if (hour < 10) {
            strHour = "0" + hour;
        } else {
            strHour = String.valueOf(hour);
        }
        if (min < 10) {
            strMin = "0" + min;
        } else {
            strMin = String.valueOf(min);
        }

        return strHour + ":" + strMin;
    }
}
