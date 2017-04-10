package com.liberal.young.tuomanprivatecloud.utils;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;

/**
 * Created by Administrator on 2017/3/30.
 */

public class WaitingDialog {

    private Dialog myWaitingDialog = null;
    private View view = null;
    private ImageView ivWaitingRotate;
    private TextView tvWaitingContent;
    private TextView tvCancel;
    public WaitingDialog(Context context,MyApplication application,String content,boolean isHaveCancel){
        if (myWaitingDialog==null){
            myWaitingDialog = new Dialog(context, R.style.CustomDialog);
            view = LayoutInflater.from(context).inflate(R.layout.waiting_dialog_layout,null);
            ivWaitingRotate = (ImageView) view.findViewById(R.id.iv_waiting_rotate);
            tvWaitingContent = (TextView) view.findViewById(R.id.tv_waiting_content);
            tvCancel = (TextView) view.findViewById(R.id.tv_cancel_wating);
            ObjectAnimator objectAnimator = new ObjectAnimator().ofFloat(ivWaitingRotate,"rotation",0f,360f).setDuration(2000);
            LinearInterpolator ll = new LinearInterpolator();    //线性动画（匀速）
            objectAnimator.setRepeatCount(-1);
            objectAnimator.setInterpolator(ll);
            objectAnimator.start();
            tvWaitingContent.setText(content);
            if (!isHaveCancel){
                tvCancel.setVisibility(View.GONE);
            }

            myWaitingDialog.setContentView(view);
            myWaitingDialog.setCanceledOnTouchOutside(true);
            WindowManager.LayoutParams params = myWaitingDialog.getWindow().getAttributes();
            params.width = application.getWidth();
            params.height =  application.getWidth();
            Window mWindow = myWaitingDialog.getWindow();
            mWindow.setGravity(Gravity.CENTER);
            mWindow.setGravity(Gravity.CENTER);
        }
    }

    public void waiting(){
        myWaitingDialog.show();
    }

    public void stopWaiting(){
        myWaitingDialog.dismiss();
    }

    public void release(){
        myWaitingDialog = null;
        view = null;
    }
}
