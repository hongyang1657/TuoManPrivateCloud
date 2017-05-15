package com.liberal.young.tuomanprivatecloud.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.JsonParseUtil;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
 * Created by Administrator on 2017/3/20.
 */

public class WorkerInfoActivity extends BaseActivity {


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
    @BindView(R.id.iv_worker_head)
    ImageView ivWorkerHead;
    @BindView(R.id.ll_reset_worker_pwd)
    LinearLayout llResetWorkerPwd;
    @BindView(R.id.tv_worker_name)
    TextView tvWorkerName;
    @BindView(R.id.tv_worker_phone)
    TextView tvWorkerPhone;
    @BindView(R.id.tv_worker_number)
    TextView tvWorkerNumber;
    @BindView(R.id.bt_delete_woker)
    Button btDeleteWoker;
    private MyApplication application;
    private List<Integer> idList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_info_layout);
        ButterKnife.bind(this);
        application = (MyApplication) getApplication();
        initView();

    }

    private void initView() {
        tvTitle.setText("操作工信息");
        ivTitleRight.setVisibility(View.GONE);
        ivTitleLeft.setImageResource(R.mipmap.back);
        tvWorkerName.setText(getIntent().getStringExtra("workerName"));
        tvWorkerPhone.setText(getIntent().getStringExtra("workerPhone"));
        tvWorkerNumber.setText(getIntent().getIntExtra("workerNum", 000) + "");
        idList.add(getIntent().getIntExtra("workerNum", 000));
    }

    @OnClick({R.id.iv_title_left, R.id.ll_reset_worker_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.ll_reset_worker_pwd:
                break;
        }
    }

    @OnClick(R.id.bt_delete_woker)
    public void onClick() {
        //删除操作工
        initEnterDialog();
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
            tvHintContent.setText("确定删除该操作工吗？");
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enterDialog.dismiss();
                }
            });
            tvEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doHttpPageSearch();
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
        enterDialog.show();
    }

    private void doHttpPageSearch(){

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.deleteUser(idList,application.getAccessToken()));
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

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();

                    }
                });
            }
        });
    }
}
