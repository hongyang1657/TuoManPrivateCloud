package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.adapter.MachineRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 陀曼管理员登陆后可以查看的生产线页面
 * Created by Administrator on 2017/3/10.
 */

public class DetailMachineListActivity extends BaseActivity {

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
    @BindView(R.id.rv_machine_list)
    RecyclerView rvMachineList;

    private List<String> detailMachineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_machine_list_layout);
        ButterKnife.bind(this);
        setTitle("自动线", true, MyConstant.TITLE_TYPE_IMG, R.mipmap.ic_launcher, true, MyConstant.TITLE_TYPE_IMG, R.mipmap.ic_launcher);
        initView();

    }

    private void initView() {

        for (int i = 0; i < 20; i++) {
            detailMachineList.add(i, "机床" + i);
        }
        rvMachineList = (RecyclerView) findViewById(R.id.rv_machine_list);
        rvMachineList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        MachineRecyclerAdapter adapter = new MachineRecyclerAdapter(this, detailMachineList);
        adapter.setOnItemClickListener(new MachineRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                Intent intent = new Intent(DetailMachineListActivity.this, WarmUpActivity.class);
                startActivity(intent);
            }
        });
        rvMachineList.setAdapter(adapter);
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                break;
            case R.id.iv_title_right:
                break;
        }
    }

}
