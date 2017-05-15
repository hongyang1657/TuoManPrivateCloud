package com.liberal.young.tuomanprivatecloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.adapter.MachineRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.bean.MachineResponse;
import com.liberal.young.tuomanprivatecloud.utils.JsonParseUtil;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

import java.io.IOException;
import java.util.ArrayList;
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

    private MyApplication application;
    private List<String> detailMachineList = new ArrayList<>();
    private List<Integer> machineStatus = new ArrayList<>();
    private List<Boolean> machineLinkStatus = new ArrayList<>();
    private List<Integer> machineForeCast = new ArrayList<>();  //标准产量
    private List<Integer> machineId = new ArrayList<>();  //机床id
    private List<Integer> userTopList = new ArrayList<>(); //操作工上限
    private List<String> closeTimeList = new ArrayList<>();   //自动关闭时间
    private List<String> operableStartList = new ArrayList<>();  //预热可操作时间start
    private List<String> operableEndList = new ArrayList<>();  //预热可操作时间end

    private int itemNum = 50;
    private int companyId;
    private WaitingDialog waitingDialog;
    private MachineRecyclerAdapter adapter;
    private String clientName;
    private int userId;
    private int roleId;
    private String logo;
    private int flag = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String workshop;
    private int haha = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_machine_list_layout);
        ButterKnife.bind(this);
        setTitle("自动线", true, MyConstant.TITLE_TYPE_IMG, R.mipmap.back, false, MyConstant.TITLE_TYPE_IMG, R.mipmap.ic_launcher);
        initView();

    }

    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_detail_machine_activity);
        application = (MyApplication) getApplication();
        waitingDialog = new WaitingDialog(this,application,"",false);
        companyId = getIntent().getIntExtra("companyId",-1);
        workshop = getIntent().getStringExtra("workshop");

        clientName = getIntent().getStringExtra("clientName");
        userId = getIntent().getIntExtra("id",-1);
        roleId = getIntent().getIntExtra("roleId",-1);
        logo = getIntent().getStringExtra("logo");
        flag = getIntent().getIntExtra("flag",0);
        haha = getIntent().getIntExtra("haha",0);

        rvMachineList = (RecyclerView) findViewById(R.id.rv_machine_list);
        rvMachineList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new MachineRecyclerAdapter(this, detailMachineList,machineStatus,machineLinkStatus,machineForeCast);
        adapter.setOnItemClickListener(new MachineRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (flag==1){         //从管理页面跳过来的
                    Intent intent1 = new Intent(DetailMachineListActivity.this,ManageClientDetailActivity.class);
                    intent1.putExtra("clientName",clientName);
                    intent1.putExtra("id",userId);
                    intent1.putExtra("roleId",roleId);
                    intent1.putExtra("logo",logo);
                    intent1.putExtra("detailMachineName",detailMachineList.get(position));
                    intent1.putExtra("userTop",userTopList.get(position));
                    intent1.putExtra("closeTime",closeTimeList.get(position));
                    intent1.putExtra("operableStart",operableStartList.get(position));
                    intent1.putExtra("operableEnd",operableEndList.get(position));
                    startActivity(intent1);
                }else {               //从主页面跳过来的
                    Intent intent = new Intent(DetailMachineListActivity.this, WarmUpActivity.class);
                    intent.putExtra("machineStatus",machineStatus.get(position));
                    intent.putExtra("detailMachineName",detailMachineList.get(position));
                    intent.putExtra("machineId",machineId.get(position));
                    intent.putExtra("operableStart",operableStartList.get(position));
                    intent.putExtra("operableEnd",operableEndList.get(position));
                    startActivity(intent);
                }
            }
        });
        rvMachineList.setAdapter(adapter);
        doHttpSearchMachine();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doHttpSearchMachine();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
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

    private int dataLength;
    private void doHttpSearchMachine(){
        detailMachineList = new ArrayList<>();
        machineStatus = new ArrayList<>();
        machineLinkStatus = new ArrayList<>();
        machineForeCast = new ArrayList<>();  //标准产量
        machineId = new ArrayList<>();  //机床id

        userTopList = new ArrayList<>(); //操作工上限
        closeTimeList = new ArrayList<>();   //自动关闭时间
        operableStartList = new ArrayList<>();  //预热可操作时间start
        operableEndList = new ArrayList<>();  //预热可操作时间end

        OkHttpClient client = new OkHttpClient();
        RequestBody body;
        if (haha==1){
            body = RequestBody.create(MyConstant.JSON, JsonUtils.getPageByWorkshop(companyId,workshop,1,100,application.getAccessToken()));
        }else {
            body = RequestBody.create(MyConstant.JSON, JsonUtils.pageByCompany("pageByCompany",companyId,1,itemNum,application.getAccessToken()));
        }
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
                Log.i("hy_debug_message", "onResponse机床: "+res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        JsonParseUtil jsonParseUtil = new JsonParseUtil(res);
                        MachineResponse machineResponse = jsonParseUtil.parseMachineSearchJson();
                        dataLength = machineResponse.getResult().size();

                        for (int i = 0; i< dataLength; i++){
                            detailMachineList.add(i,machineResponse.getResult().get(i).getWorkshop()+
                                    machineResponse.getResult().get(i).getName());
                            machineStatus.add(i,machineResponse.getResult().get(i).getStatus());
                            machineLinkStatus.add(i,machineResponse.getResult().get(i).isLinkStatus());
                            machineForeCast.add(i,machineResponse.getResult().get(i).getForecast());
                            machineId.add(i,machineResponse.getResult().get(i).getId());

                            userTopList.add(i,machineResponse.getResult().get(i).getUserTop());
                            closeTimeList.add(i,machineResponse.getResult().get(i).getCloseTime());
                            operableStartList.add(i,machineResponse.getResult().get(i).getOperableStart());
                            operableEndList.add(i,machineResponse.getResult().get(i).getOperableEnd());
                        }
                        adapter.notifyData(detailMachineList,machineStatus,machineLinkStatus,machineForeCast);
                    }
                });
            }
        });
    }

}
