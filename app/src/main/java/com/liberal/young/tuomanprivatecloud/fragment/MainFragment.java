package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.activity.AddClientActivity;
import com.liberal.young.tuomanprivatecloud.activity.DetailMachineListActivity;
import com.liberal.young.tuomanprivatecloud.adapter.ClientRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.bean.JsonResponse;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusFromMainFragment;
import com.liberal.young.tuomanprivatecloud.utils.JsonParseUtil;
import com.liberal.young.tuomanprivatecloud.utils.JsonUtils;
import com.liberal.young.tuomanprivatecloud.utils.L;
import com.liberal.young.tuomanprivatecloud.utils.MyConstant;
import com.liberal.young.tuomanprivatecloud.utils.WaitingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
 * 陀曼管理员登陆后的首页，查看陀曼客户信息的页面
 * Created by Administrator on 2017/3/10.
 */

public class MainFragment extends Fragment {


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
    RecyclerView rvClientList;
    @BindView(R.id.ll_machine_line_title)
    LinearLayout llMachineLineTitle;

    private static final int REQUEST_CODE_ADD_CLIENT = 1;
    private static final int PAGE_ITEM_NUMBER = 15;      //一页加载的item数


    private ClientRecyclerAdapter adapter = null;
    private int dataLength = 0;
    private List<String> clientNameList = new ArrayList<>();
    private List<String> clientHeadList = new ArrayList<>();
    private List<Integer> clientId= new ArrayList<>();
    private boolean isOnDeleteState = false;      //是否正在删除客户
    private List<Boolean> selectList = null;      //用于选中删除客户的数组

    private MyApplication application;
    private View view;

    private boolean isScrollToBottom;
    private OkHttpClient client;
    private WaitingDialog waitingDialog;

    public MainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment_layout, container, false);
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

    private void initView(View view) {
        client = new OkHttpClient();
        application = (MyApplication) getActivity().getApplication();
        llMachineLineTitle = (LinearLayout) view.findViewById(R.id.ll_machine_line_title);
        ivTitleRight = (ImageView) view.findViewById(R.id.iv_title_right);
        ivTitleLeft = (ImageView) view.findViewById(R.id.iv_title_left);
        tvTitleLeft = (TextView) view.findViewById(R.id.tv_title_left);
        tvTitleRight = (TextView) view.findViewById(R.id.tv_title_right);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        llMachineLineTitle.setVisibility(View.GONE);
        tvTitle.setText("客户");
        ivTitleLeft.setImageResource(R.mipmap.add_title);
        ivTitleRight.setImageResource(R.mipmap.delete_client);
        waitingDialog = new WaitingDialog(getActivity(),application,"",false);
        waitingDialog.waiting();
        doHttpPageSearch();


        rvClientList = (RecyclerView) view.findViewById(R.id.rv_machine_list);
        rvClientList.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        //rvClientList.setHasFixedSize(true);
        rvClientList.setItemAnimator(new DefaultItemAnimator());
        //recyclerView滑动监听
        rvClientList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) rvClientList.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int[] lastVisibleItem = manager.findLastCompletelyVisibleItemPositions(null);
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向下滚动

                    //取三个中值最大的
                    int lastVisibleItemMax;
                    if (lastVisibleItem[0]<lastVisibleItem[1]){
                        lastVisibleItemMax = lastVisibleItem[1];
                    }else {
                        lastVisibleItemMax = lastVisibleItem[0];
                    }
                    if (lastVisibleItemMax<lastVisibleItem[2]){
                        lastVisibleItemMax = lastVisibleItem[2];
                    }
                    L.i("totalItemCount:"+totalItemCount);
                    L.i("lastVisibleItemMax:"+lastVisibleItemMax);
                    if (lastVisibleItemMax == (totalItemCount - 1) && isScrollToBottom && !isOnDeleteState&&totalItemCount%PAGE_ITEM_NUMBER==0) {
                        //加载更多功能的代码
                        L.i("滑动到了底部");
                        int addpage = (totalItemCount/PAGE_ITEM_NUMBER)+1;
                        L.i("addpage"+addpage);
                        addHttpPage(addpage);
                        adapter.getNotify(clientNameList,clientHeadList);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0){
                    isScrollToBottom = true;
                }else {
                    isScrollToBottom = false;
                }
            }
        });

        adapter = new ClientRecyclerAdapter(getActivity(), clientHeadList, clientNameList);
        adapter.setOnItemClickListener(new ClientRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isOnDeleteState) {
                    if (!selectList.get(position)) {
                        selectList.set(position, true);
                    } else {
                        selectList.set(position, false);
                    }
                    adapter.selectItemToDelete(selectList);
                } else {
                    Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), DetailMachineListActivity.class);
                    startActivity(intent);
                }
            }
        });
        rvClientList.setAdapter(adapter);

    }

    private List<Integer> deleteIDList = new ArrayList<>();
    private List<Integer> deletePositionList = new ArrayList<>();
    @Subscribe
    public void onEventMainThread(MyEventBusFromMainFragment event) {
        if (event.isEnterToDelete()) {
            for (int i = 0;i<selectList.size();i++){
                L.i("选择删除的："+selectList.get(i));
                if (selectList.get(i)){
                    deletePositionList.add(i);
                    ivTitleLeft.setVisibility(View.VISIBLE);
                    ivTitleRight.setVisibility(View.VISIBLE);
                    tvTitleLeft.setVisibility(View.GONE);
                    tvTitleRight.setVisibility(View.GONE);
                    deleteIDList.add(clientId.get(i));  //需要删除的用户id

                }
            }
            waitingDialog.waiting();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                        deleteUser();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right, R.id.tv_title_left, R.id.tv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                if (isOnDeleteState) {

                } else {
                    Toast.makeText(getActivity(), "添加客户", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AddClientActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_ADD_CLIENT);
                }
                break;
            case R.id.iv_title_right:
                if (isOnDeleteState) {

                } else {
                    Toast.makeText(getActivity(), "删除客户", Toast.LENGTH_SHORT).show();
                    //底部导航栏变为删除按钮
                    adapter.deleteClient(true);
                    EventBus.getDefault().post(new MyEventBusFromMainFragment(true, false));
                    ivTitleLeft.setVisibility(View.GONE);
                    ivTitleRight.setVisibility(View.GONE);
                    tvTitleLeft.setVisibility(View.VISIBLE);
                    tvTitleRight.setVisibility(View.VISIBLE);

                    //做一个数组
                    selectList = new ArrayList<>();
                    for (int i = 0; i < clientNameList.size(); i++) {
                        selectList.add(i, false);
                    }

                    isOnDeleteState = true;
                }
                break;
            case R.id.tv_title_left:
                tvTitleLeft.setVisibility(View.GONE);
                tvTitleRight.setVisibility(View.GONE);
                ivTitleLeft.setVisibility(View.VISIBLE);
                ivTitleRight.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "取消删除", Toast.LENGTH_SHORT).show();
                adapter.deleteClient(false);
                EventBus.getDefault().post(new MyEventBusFromMainFragment(false, false));
                isOnDeleteState = false;
                selectList = null;
                adapter.selectItemToDelete(selectList);
                break;
            case R.id.tv_title_right:
                Toast.makeText(getActivity(), "全选", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < clientNameList.size(); i++) {
                    //selectList.remove(i);
                    selectList.set(i, true);
                }
                adapter.selectItemToDelete(selectList);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_ADD_CLIENT:
                waitingDialog.waiting();
                doHttpPageSearch();
                L.i("直接返回");
                adapter.getNotify(clientNameList,clientHeadList);
                break;
        }
    }

    private void doHttpPageSearch(){
        clientNameList = new ArrayList<>();
        clientHeadList = new ArrayList<>();
        clientId = new ArrayList<>();

        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.pageSearch(1,PAGE_ITEM_NUMBER,"pageSearchCustomer",application.getAccessToken()));
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
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse: "+res);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        JsonParseUtil jsonParseUtil = new JsonParseUtil(res);
                        JsonResponse jsonResponse = jsonParseUtil.parsePageSearchJson();
                        dataLength = jsonResponse.getResult().size();

                        for (int i = 0; i< dataLength; i++){
                            clientNameList.add(i,jsonResponse.getResult().get(i).getUsername());
                            clientHeadList.add(jsonResponse.getResult().get(i).getLogo());
                            clientId.add(i,jsonResponse.getResult().get(i).getId());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //上拉加载更多数据
    private void addHttpPage(int page){
        waitingDialog.waiting();
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.pageSearch(page,PAGE_ITEM_NUMBER,"pageSearchCustomer",application.getAccessToken()));
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
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse: "+res);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.stopWaiting();
                        JsonParseUtil jsonParseUtil = new JsonParseUtil(res);
                        JsonResponse jsonResponse = jsonParseUtil.parsePageSearchJson();
                        dataLength = jsonResponse.getResult().size();

                        for (int i = 0; i< dataLength; i++){
                            clientNameList.add(jsonResponse.getResult().get(i).getUsername());
                            clientHeadList.add(jsonResponse.getResult().get(i).getLogo());
                            clientId.add(jsonResponse.getResult().get(i).getId());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    //删除客户
    private void deleteUser(){
        RequestBody body = RequestBody.create(MyConstant.JSON, JsonUtils.deleteUser(deleteIDList,application.getAccessToken()));
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
                final String res = response.body().string();
                Log.i("hy_debug_message", "onResponse: "+res);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (JsonUtils.getCode(res)!=0){
                            Toast.makeText(getActivity(), "操作失败", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
                        }
                        waitingDialog.stopWaiting();
                        adapter.deleteClient(false);
                        EventBus.getDefault().post(new MyEventBusFromMainFragment(false, false));
                        isOnDeleteState = false;
                        selectList = null;
                        adapter.selectItemToDelete(selectList);
                    }
                });
            }
        });
    }

}
