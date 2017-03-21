package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.adapter.ManageClientBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/20.
 */

public class ManageClientActivity extends BaseActivity {


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
    @BindView(R.id.lv_manage_client)
    ListView lvManageClient;

    private ManageClientBaseAdapter adapter;
    private List<String> mClientNameList;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_client_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        application = (MyApplication) getApplication();
        lvManageClient = (ListView) findViewById(R.id.lv_manage_client);
        mClientNameList = new ArrayList<>();
        if (application.getUserLimits().equals("1")){
            tvTitle.setText("客户列表");
            for (int i=0;i<20;i++){
                mClientNameList.add("企业企业"+i);
            }
        }else if (application.getUserLimits().equals("2")){
            tvTitle.setText("操作工管理");
            for (int i=0;i<20;i++){
                mClientNameList.add("操作工"+i);
            }
        }


        adapter = new ManageClientBaseAdapter(this,mClientNameList);
        lvManageClient.setOnItemClickListener(itemClickListener);
        lvManageClient.setAdapter(adapter);
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                break;
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (application.getUserLimits().equals("1")){
                Intent intent = new Intent(ManageClientActivity.this,ManageClientDetailActivity.class);
                startActivity(intent);
            }else if (application.getUserLimits().equals("2")){
                Intent intent = new Intent(ManageClientActivity.this,WorkerInfoActivity.class);
                startActivity(intent);
            }
        }
    };
}
