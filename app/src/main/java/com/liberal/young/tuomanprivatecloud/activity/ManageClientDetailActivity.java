package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
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

    private Dialog dialogWarmTimeSlot = null;
    private Dialog dialogAutoClose = null;
    private Dialog dialogWorkerMaxNum = null;
    private View viewWarmTimeSlot = null;
    private View viewAutoClose = null;
    private View viewWorkerMaxNum = null;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_client_detail_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        tvManageClientDetailName.setText(getIntent().getStringExtra("clientName"));
    }

    @OnClick({R.id.iv_title_left, R.id.ll_manage_client_detail_warm_time_slot, R.id.ll_manage_client_detail_auto_close_time, R.id.ll_manage_client_detail_worker_max_num})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
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

    private void initWarmTimeslotDialog(){
        /*dialogWarmTimeSlot = new Dialog(this,R.style.CustomDialog);
        viewWarmTimeSlot = LayoutInflater.from(this).inflate(R.layout.)*/
    }

    private Switch swAutoCloseImpower;
    private TimePicker tpAutoClose;
    private TextView tvCancel1;
    private TextView tvEnter1;
    private TextView tvHint1;
    private void initAutoCloseTimeDialog(){
        if (dialogAutoClose==null){
            dialogAutoClose = new Dialog(this,R.style.CustomDialog);
            viewAutoClose = LayoutInflater.from(this).inflate(R.layout.auto_close_time_dialog_layout,null);
            swAutoCloseImpower = (Switch) viewAutoClose.findViewById(R.id.sw_auto_close);
            tpAutoClose = (TimePicker) viewAutoClose.findViewById(R.id.tp_auto_close);
            tvCancel1 = (TextView) viewAutoClose.findViewById(R.id.tv_cancel);
            tvEnter1 = (TextView) viewAutoClose.findViewById(R.id.tv_enter);
            tvHint1 = (TextView) viewAutoClose.findViewById(R.id.tv_hint);
            tvCancel1.setOnClickListener(onclickListener);
            tvEnter1.setOnClickListener(onclickListener);
            swAutoCloseImpower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swAutoCloseImpower.isChecked()){
                        tvHint1.setVisibility(View.GONE);
                    }else {
                        tvHint1.setVisibility(View.VISIBLE);
                    }
                }
            });
            dialogAutoClose.setContentView(viewAutoClose);
            dialogAutoClose.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = dialogAutoClose.getWindow().getAttributes();
            params.width = (int) (width*0.9);
            Window mWindow = dialogAutoClose.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        dialogAutoClose.show();
    }

    private Switch swWorkerImpower;
    private Spinner spinnerWorkerNum;
    private TextView tvCancel;
    private TextView tvEnter;
    private TextView tvhint;
    private int workerNumber = 0;
    private void initWorkerMaxNumDialog(){
        if (dialogWorkerMaxNum==null){
            dialogWorkerMaxNum = new Dialog(this,R.style.CustomDialog);
            viewWorkerMaxNum = LayoutInflater.from(this).inflate(R.layout.worker_setting_dialog_layout,null);
            swWorkerImpower = (Switch) viewWorkerMaxNum.findViewById(R.id.sw_worker_empower);
            spinnerWorkerNum = (Spinner) viewWorkerMaxNum.findViewById(R.id.spinner_worker_num);
            tvCancel = (TextView) viewWorkerMaxNum.findViewById(R.id.tv_cancel);
            tvEnter = (TextView) viewWorkerMaxNum.findViewById(R.id.tv_enter);
            tvhint = (TextView) viewWorkerMaxNum.findViewById(R.id.tv_hint);
            tvCancel.setOnClickListener(onclickListener);
            tvEnter.setOnClickListener(onclickListener);
            swWorkerImpower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swWorkerImpower.isChecked()){
                        tvhint.setVisibility(View.GONE);
                    }else {
                        tvhint.setVisibility(View.VISIBLE);
                    }
                }
            });
            spinnerWorkerNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    workerNumber = position+1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    workerNumber = 0;
                }
            });

            dialogWorkerMaxNum.setContentView(viewWorkerMaxNum);
            dialogWorkerMaxNum.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = dialogWorkerMaxNum.getWindow().getAttributes();
            params.width = (int) (width*0.9);
            Window mWindow = dialogWorkerMaxNum.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }
        dialogWorkerMaxNum.show();

    }

    View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_cancel:
                    if (dialogWorkerMaxNum!=null&&dialogWorkerMaxNum.isShowing()){
                        dialogWorkerMaxNum.dismiss();
                    }
                    if (dialogAutoClose!=null&&dialogAutoClose.isShowing()){
                        dialogAutoClose.dismiss();
                    }
                    if (dialogWarmTimeSlot!=null&&dialogWarmTimeSlot.isShowing()){
                        dialogWarmTimeSlot.dismiss();
                    }
                    break;
                case R.id.tv_enter:
                    if (dialogWorkerMaxNum!=null&&dialogWorkerMaxNum.isShowing()){
                        dialogWorkerMaxNum.dismiss();
                        tvManageClientWorkerMaxNum.setText(workerNumber+"个");
                    }
                    if (dialogAutoClose!=null&&dialogAutoClose.isShowing()){
                        dialogAutoClose.dismiss();
                        setAutoCloseTime();
                    }
                    if (dialogWarmTimeSlot!=null&&dialogWarmTimeSlot.isShowing()){
                        dialogWarmTimeSlot.dismiss();
                    }

                    break;
            }
        }
    };

    //得到自动关闭的时间并设置
    private void setAutoCloseTime(){
        int hour = tpAutoClose.getCurrentHour();
        int min = tpAutoClose.getCurrentMinute();
        String strHour = "";
        String strMin = "";
        if (hour<10){
            strHour = "0"+hour;
        }else {
            strHour = String.valueOf(hour);
        }
        if (min<10){
            strMin = "0"+min;
        }else {
            strMin = String.valueOf(min);
        }
        tvManageClientAutoCloseTime.setText(strHour+":"+strMin);
    }
}
