package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.activity.ConnectWifiActivity;
import com.liberal.young.tuomanprivatecloud.activity.WarmUpActivity;
import com.liberal.young.tuomanprivatecloud.adapter.MachineRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.bean.JsonResponse;
import com.liberal.young.tuomanprivatecloud.bean.MachineResponse;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusMachineFragment;
import com.liberal.young.tuomanprivatecloud.utils.JsonParseUtil;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;
import com.liberal.young.tuomanprivatecloud.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 陀曼的客户登录后加载的生产线界面
 * <p>
 * Created by Administrator on 2017/3/14.
 */

public class MachineFragment extends Fragment {


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
    @BindView(R.id.ll_machine_line_title)
    LinearLayout llMachineLineTitle;


    private List<String> detailMachineList = new ArrayList<>();
    private List<Integer> machineStatus = new ArrayList<>();
    private List<Boolean> machineLinkStatus = new ArrayList<>();
    private List<Integer> machineForeCast = new ArrayList<>();  //标准产量
    private List<Integer> machineId = new ArrayList<>(); //机床id
    private MachineRecyclerAdapter adapter;
    private List<Boolean> selectList = null;      //用于选中删除客户的数组
    private boolean isBatching = false;        //是否正在批量处理
    private int itemNum = 100;
    private OkHttpClient client;
    private WaitingDialog waitingDialog;
    private MyApplication application;
    private String limit;


    public MachineFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_layout, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainThread(MyEventBusMachineFragment event) {
        if (event.isEnterToStart()){
            //Toast.makeText(getActivity(), "打开选中的机器", Toast.LENGTH_SHORT).show();
            dohttpTurnOnMachine(1);

        }
    }

    private void initView(View view) {
        application = (MyApplication) getActivity().getApplication();
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ivTitleRight = (ImageView) view.findViewById(R.id.iv_title_right);
        ivTitleLeft = (ImageView) view.findViewById(R.id.iv_title_left);
        ivTitleRight.setImageResource(R.mipmap.more_title);
        ivTitleLeft.setImageResource(R.mipmap.add_title);
        client = new OkHttpClient();
        waitingDialog = new WaitingDialog(getActivity(),application,"",false);
        waitingDialog.waiting();
        tvTitle.setText("自动线");
        limit = application.getUserLimits();
        if (limit.equals("5")){
            doHttpSearchMachine("pageByStaff");         //按操作工查找生产线
        }else {
            doHttpSearchMachine("pageByCompany");       //按公司查找生产线
        }

        rvMachineList = (RecyclerView) view.findViewById(R.id.rv_machine_list);
        rvMachineList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new MachineRecyclerAdapter(getActivity(), detailMachineList,machineStatus,machineLinkStatus,machineForeCast);
        adapter.setOnItemClickListener(new MachineRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isBatching){                          //在批量处理
                    if (!selectList.get(position)) {
                        selectList.set(position, true);
                    } else {
                        selectList.set(position, false);
                    }
                    adapter.selectItemToBatch(selectList);
                }else {
                    Intent intent = new Intent(getActivity(), WarmUpActivity.class);
                    intent.putExtra("machineStatus",machineStatus.get(position));
                    intent.putExtra("detailMachineName",detailMachineList.get(position));
                    intent.putExtra("machineId",machineId.get(position));
                    intent.putExtra("position",position);
                    intent.putExtra("res",res);
                    startActivity(intent);
                }
            }
        });
        rvMachineList.setAdapter(adapter);
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right,R.id.tv_title_left, R.id.tv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                //startActivity(new Intent(getActivity(), CaptureActivity.class));
                startActivity(new Intent(getActivity(), ConnectWifiActivity.class));
                break;
            case R.id.iv_title_right:
                initPopWindow();
                break;
            case R.id.tv_title_left:
                //取消
                EventBus.getDefault().post(new MyEventBusMachineFragment(false,false));
                isBatching = false;
                adapter.batchProcessing(isBatching);
                tvTitleLeft.setVisibility(View.GONE);
                tvTitleRight.setVisibility(View.GONE);
                ivTitleLeft.setVisibility(View.VISIBLE);
                ivTitleRight.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_title_right:
                //全选
                for (int i = 0; i < itemNum; i++) {
                    //selectList.remove(i);
                    selectList.set(i, true);
                }
                adapter.selectItemToBatch(selectList);
                break;
        }
    }

    private PopupWindow popWnd = null;
    private void initPopWindow() {
        if (popWnd == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_layout, null);
            TextView tv1 = (TextView) contentView.findViewById(R.id.tv_pop_1);
            TextView tv2 = (TextView) contentView.findViewById(R.id.tv_pop_2);
            tv2.setVisibility(View.GONE);
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //开始批量处理
                    EventBus.getDefault().post(new MyEventBusMachineFragment(true,false));
                    popWnd.dismiss();
                    ivTitleLeft.setVisibility(View.GONE);
                    ivTitleRight.setVisibility(View.GONE);
                    tvTitleLeft.setVisibility(View.VISIBLE);
                    tvTitleRight.setVisibility(View.VISIBLE);
                    isBatching = true;
                    adapter.batchProcessing(isBatching);
                    selectList = new ArrayList<>();
                    for (int i = 0; i < itemNum; i++) {
                        selectList.add(i, false);
                    }
                    adapter.selectItemToBatch(selectList);
                }
            });
            popWnd = new PopupWindow(getActivity());
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

    private int dataLength;
    private String res;
    private void doHttpSearchMachine(String method){
        detailMachineList = new ArrayList<>();
        machineStatus = new ArrayList<>();
        machineLinkStatus = new ArrayList<>();
        machineForeCast = new ArrayList<>();  //标准产量
        machineId = new ArrayList<>(); //机床id

        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.pageByCompany(method,application.getCompanyId(),1,itemNum,application.getAccessToken()));
        Request request = new Request.Builder()
                .url(MyConstant.SERVER_URL)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("hy_debug_message", "onFailure: "+e.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                Log.i("hy_debug_message", "onResponse机床: "+res);

                getActivity().runOnUiThread(new Runnable() {
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
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    //开关机床的网络请求(这里只能开机床)
    private void dohttpTurnOnMachine(final int status){
        List<Integer> selectMachineIdList = new ArrayList<>();
        for (int i=0;i<itemNum;i++){
            if (selectList.get(i)){
                selectMachineIdList.add(machineId.get(i));
            }
        }

        int[] machineIds = new int[selectMachineIdList.size()];          //创造一个数组
        L.i("machineIds长度："+machineIds.length);
        L.i("selectMachineIdList长度："+selectMachineIdList.size());
        for (int i=0;i<machineIds.length;i++){
            machineIds[i] = selectMachineIdList.get(i);
        }

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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "操作失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                        waitingDialog.stopWaiting();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse机床: "+res);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        if (JsonUtils.getCode(res)==0){    //请求成功
                            Toast.makeText(getActivity(), "操作成功！！！", Toast.LENGTH_SHORT).show();
                            //退出批量模式
                            EventBus.getDefault().post(new MyEventBusMachineFragment(false,false));
                            isBatching = false;
                            adapter.batchProcessing(isBatching);
                            tvTitleLeft.setVisibility(View.GONE);
                            tvTitleRight.setVisibility(View.GONE);
                            ivTitleLeft.setVisibility(View.VISIBLE);
                            ivTitleRight.setVisibility(View.VISIBLE);
                            //这里要刷新一下界面
                            if (limit.equals("5")){
                                doHttpSearchMachine("pageByStaff");         //按操作工查找生产线
                            }else {
                                doHttpSearchMachine("pageByCompany");       //按公司查找生产线
                            }
                        }else {
                            Toast.makeText(getActivity(), "操作失败，请检查网络后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}
