package com.liberal.young.tuomanprivatecloud.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;


/**
 * Created by Administrator on 2017/3/8.
 */

public class BaseActivity extends Activity{
    protected ProgressDialog waitDialog;
    public static BaseActivity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        ((MyApplication)MyApplication.getApplication()).addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity = null;
    }

    /**
     * activity退出时将activity移出栈
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MyApplication)MyApplication.getApplication()).removeActivity(this);
    }

    /**
     * 需在setContentView方法之后调用. 设置后,如果不对左侧的事件进行监听,默认的点击事件是结束当前界面.
     * <p>
     * 标题传资源id和字符串皆可.
     * <p>
     * 如果某一侧显示的是图片,则那一侧只能传对应的图片资源id.如果是文字,则资源id和字符串皆可.
     *
     * @param title
     *            标题
     * @param left
     *            是否显示左侧的部分
     * @param leftType
     *            左侧的类型    1，text  2，img
     * @param l
     *            左侧部分内容
     * @param right
     *            是否显示右侧的部分
     * @param rightType
     *            右侧的类型
     * @param r
     *            右侧部分的内容
     */
    protected void setTitle(Object title, boolean left, int leftType, Object l, boolean right, int rightType, Object r) {
        try {
            TextView tvTitle = (TextView) findViewById(R.id.tv_title);
            TextView tvLeft = (TextView) findViewById(R.id.tv_title_left);
            LinearLayout llLeft = (LinearLayout) findViewById(R.id.ll_title_left);
            ImageView ivLeft = (ImageView) findViewById(R.id.iv_title_left);
            TextView tvRight = (TextView) findViewById(R.id.tv_title_right);
            ImageView ivRight = (ImageView) findViewById(R.id.iv_title_right);
            LinearLayout llRight = (LinearLayout) findViewById(R.id.ll_title_right);
            if (title != null && title instanceof String) {
                if (!TextUtils.isEmpty((String) title)) {
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText((String) title);
                } else {
                    tvTitle.setVisibility(View.INVISIBLE);
                }
            } else if (title != null && title instanceof Integer) {
                if (((Integer) title) > 0) {
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText((Integer) title);
                } else {
                    tvTitle.setVisibility(View.INVISIBLE);
                }

            }
            if (left) {
                llLeft.setVisibility(View.VISIBLE);
                if (leftType == MyConstant.TITLE_TYPE_TEXT) {
                    ivLeft.setVisibility(View.GONE);
                    tvLeft.setVisibility(View.VISIBLE);
                    if (l instanceof String) {
                        tvLeft.setText((String) l);
                    } else if (l instanceof Integer) {
                        tvLeft.setText((Integer) l);
                    }
                } else if (leftType == MyConstant.TITLE_TYPE_IMG) {
                    ivLeft.setVisibility(View.VISIBLE);
                    tvLeft.setVisibility(View.GONE);
                    if (l instanceof Integer) {
                        ivLeft.setImageResource((Integer) l);
                    }

                }
            } else {
                llLeft.setVisibility(View.INVISIBLE);
            }
            if (right) {
                llRight.setVisibility(View.VISIBLE);
                if (rightType == MyConstant.TITLE_TYPE_TEXT) {
                    ivRight.setVisibility(View.GONE);
                    tvRight.setVisibility(View.VISIBLE);
                    if (r instanceof String) {
                        tvRight.setText((String) r);
                    } else if (r instanceof Integer) {
                        tvRight.setText((Integer) r);
                    }
                } else if (rightType == MyConstant.TITLE_TYPE_IMG) {
                    ivRight.setVisibility(View.VISIBLE);
                    tvRight.setVisibility(View.GONE);
                    if (r instanceof Integer) {
                        ivRight.setImageResource((Integer) r);
                    }
                }
            } else {
                llRight.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {

        }
    }



    /**
     * 全局等待对话框
     */
    public void showWaitDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (waitDialog == null || !waitDialog.isShowing()) {
                    waitDialog = new ProgressDialog(BaseActivity.this);
                    waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    waitDialog.setCanceledOnTouchOutside(false);
                    ImageView view = new ImageView(BaseActivity.this);
                    view.setLayoutParams(new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
                    /*Animation loadAnimation = AnimationUtils.loadAnimation(
                            BaseActivity.this, R.anim.);
                    view.startAnimation(loadAnimation);
                    loadAnimation.start();*/
                    ObjectAnimator obj = ObjectAnimator.ofFloat(view,"rotation",0f,360f).setDuration(1000);
                    obj.setRepeatCount(-1);
                    obj.start();
                    view.setImageResource(R.mipmap.ic_launcher);
                    // waitDialog.setCancelable(false);
                    waitDialog.show();
                    waitDialog.setContentView(view);
                    Log.i("result", "waitDialong..............");
                }

            }
        });

    }

    public void dismissWaitDialog() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (waitDialog != null && waitDialog.isShowing()) {
                    waitDialog.dismiss();
                    waitDialog = null;
                }
            }
        });

    }
}
