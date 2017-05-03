package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;

import java.io.File;
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
 * Created by Administrator on 2017/3/21.
 */

public class SettingActivity extends BaseActivity {


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
    @BindView(R.id.tv_setting_warm_time_zone)
    TextView tvSettingWarmTimeZone;
    @BindView(R.id.ll_setting_warm_time_zone)
    LinearLayout llSettingWarmTimeZone;
    @BindView(R.id.tv_setting_auto_close)
    TextView tvSettingAutoClose;
    @BindView(R.id.ll_setting_auto_close)
    LinearLayout llSettingAutoClose;
    @BindView(R.id.tv_setting_worker_num)
    TextView tvSettingWorkerNum;
    @BindView(R.id.ll_setting_worker_num)
    LinearLayout llSettingWorkerNum;
    @BindView(R.id.ll_custom_launch_page)
    LinearLayout llCustomLaunchPage;
    @BindView(R.id.tv_unbind_phone)
    TextView tvUnbindPhone;
    @BindView(R.id.ll_unbind_phone)
    LinearLayout llUnbindPhone;
    @BindView(R.id.ll_app_promotion)
    LinearLayout llAppPromotion;
    @BindView(R.id.ll_logout)
    LinearLayout llLogout;
    @BindView(R.id.ll_custom_setting)
    LinearLayout llCustomSetting;

    private MyApplication application;
    private static String userLimits;
    private Dialog dialogWarmTimeSlot = null;
    private Dialog dialogAutoClose = null;
    private Dialog dialogWorkerMaxNum = null;
    private View viewWarmTimeSlot = null;
    private View viewAutoClose = null;
    private View viewWorkerMaxNum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        userLimits = application.getUserLimits();
        ivTitleRight.setVisibility(View.GONE);
        tvTitle.setText("设置");
        ivTitleLeft.setImageResource(R.mipmap.back);
        /*if (userLimits.equals("1") || userLimits.equals("2")|| userLimits.equals("4")|| userLimits.equals("5")) {
            llCustomSetting.setVisibility(View.GONE);
        }else if (userLimits.equals("3")){
            llCustomSetting.setVisibility(View.VISIBLE);
        }*/
    }

    @OnClick({R.id.iv_title_left, R.id.ll_setting_warm_time_zone, R.id.ll_setting_auto_close, R.id.ll_setting_worker_num, R.id.ll_custom_launch_page, R.id.ll_unbind_phone, R.id.ll_app_promotion, R.id.ll_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.ll_setting_warm_time_zone:
                initWarmTimeslotDialog();
                break;
            case R.id.ll_setting_auto_close:
                initAutoCloseTimeDialog();
                break;
            case R.id.ll_setting_worker_num:
                initWorkerMaxNumDialog();
                break;
            case R.id.ll_custom_launch_page:
                Intent intent = new Intent(SettingActivity.this, SetCustomLaunchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_unbind_phone:
                break;
            case R.id.ll_app_promotion:
                break;
            case R.id.ll_logout:
                initExitDialog();

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
            params.width = (int) (application.getWidth() * 0.9);
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
            params.width = (int) (application.getWidth() * 0.9);
            Window mWindow = dialogAutoClose.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        dialogAutoClose.show();
    }

    private Spinner spinnerWorkerNum;
    private TextView tvCancelNum;
    private TextView tvEnterNum;
    private int workerNumber = 0;

    private void initWorkerMaxNumDialog() {
        if (dialogWorkerMaxNum == null) {
            dialogWorkerMaxNum = new Dialog(this, R.style.CustomDialog);
            viewWorkerMaxNum = LayoutInflater.from(this).inflate(R.layout.worker_setting_dialog_layout, null);
            spinnerWorkerNum = (Spinner) viewWorkerMaxNum.findViewById(R.id.spinner_worker_num);
            tvCancelNum = (TextView) viewWorkerMaxNum.findViewById(R.id.tv_cancel);
            tvEnterNum = (TextView) viewWorkerMaxNum.findViewById(R.id.tv_enter);
            tvCancelNum.setOnClickListener(onclickListener);
            tvEnterNum.setOnClickListener(onclickListener);
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
            params.width = (int) (application.getWidth() * 0.9);
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
                        tvSettingWorkerNum.setText(workerNumber + "个");
                    }
                    if (dialogAutoClose != null && dialogAutoClose.isShowing()) {
                        dialogAutoClose.dismiss();
                        setAutoCloseTime();
                    }
                    if (dialogWarmTimeSlot != null && dialogWarmTimeSlot.isShowing()) {
                        dialogWarmTimeSlot.dismiss();
                        tvSettingWarmTimeZone.setText(startTime+" ~ "+endTime);
                    }

                    break;
            }
        }
    };

    //-------------------------------------------------------------
    private Dialog ExitDialog = null;
    private View view = null;
    private TextView tvHintContent;
    private TextView tvCancel;
    private TextView tvEnter;

    private void initExitDialog() {
        if (ExitDialog == null) {
            ExitDialog = new Dialog(this, R.style.CustomDialog);
            view = LayoutInflater.from(this).inflate(R.layout.my_alert_dialog, null);
            tvHintContent = (TextView) view.findViewById(R.id.tv_hint_content);
            tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
            tvEnter = (TextView) view.findViewById(R.id.tv_enter);
            tvCancel.setOnClickListener(clickListener);
            tvEnter.setOnClickListener(clickListener);
            tvHintContent.setText("确定退出账号吗？");

            ExitDialog.setContentView(view);
            ExitDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = ExitDialog.getWindow().getAttributes();
            L.i(application.getWidth() + "");
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
                    break;
                case R.id.tv_enter:
                    //退出账号
                    ExitDialog.dismiss();
                    logOut();
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    };

    private void logOut() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.login("", "", "logout", application.getAccessToken()));  //注销账号
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure:注销： " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("hy_debug_message", "onResponse:注销： " + res);
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginInformation", MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        String path = Environment.getExternalStorageDirectory() + "/abc.jpg";
                        File mFile = new File(path);
                        //若该文件存在
                        if (mFile.exists()) {
                            mFile.delete();
                        }
                    }
                });
            }
        });
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
        tvSettingAutoClose.setText(strHour + ":" + strMin);
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
