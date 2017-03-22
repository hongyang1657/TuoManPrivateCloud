package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.activity.AddClientActivity;
import com.liberal.young.tuomanprivatecloud.activity.DetailMachineListActivity;
import com.liberal.young.tuomanprivatecloud.adapter.ClientRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusFromMainFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private ClientRecyclerAdapter adapter = null;
    private int itemNum = 30;
    private List<String> clientNameList = new ArrayList<>();
    private List<Integer> clientHeadList = new ArrayList<>();
    private boolean isOnDeleteState = false;      //是否正在删除客户
    private List<Boolean> selectList = null;      //用于选中删除客户的数组

    public MainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

    private void initView(View view) {
        llMachineLineTitle = (LinearLayout) view.findViewById(R.id.ll_machine_line_title);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        llMachineLineTitle.setVisibility(View.GONE);
        tvTitle.setText("客户");
        for (int i = 0; i < itemNum; i++) {
            clientNameList.add(i, "客户" + i);
            clientHeadList.add(i, R.mipmap.login_logo_3x);
        }

        rvClientList = (RecyclerView) view.findViewById(R.id.rv_machine_list);
        rvClientList.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        //rvClientList.setHasFixedSize(true);
        rvClientList.setItemAnimator(new DefaultItemAnimator());
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

    @Subscribe
    public void onEventMainThread(MyEventBusFromMainFragment event) {
        if (event.isEnterToDelete()) {
            adapter.removeItem(2);
        }
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                if (isOnDeleteState) {
                    Toast.makeText(getActivity(), "取消删除", Toast.LENGTH_SHORT).show();
                    adapter.deleteClient(false);
                    EventBus.getDefault().post(new MyEventBusFromMainFragment(false, false));
                    isOnDeleteState = false;
                    selectList = null;
                    adapter.selectItemToDelete(selectList);
                } else {
                    Toast.makeText(getActivity(), "添加客户", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), AddClientActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_title_right:
                if (isOnDeleteState) {
                    Toast.makeText(getActivity(), "全选", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < itemNum; i++) {
                        //selectList.remove(i);
                        selectList.set(i, true);
                    }
                    adapter.selectItemToDelete(selectList);
                } else {
                    Toast.makeText(getActivity(), "删除客户", Toast.LENGTH_SHORT).show();
                    //底部导航栏变为删除按钮
                    adapter.deleteClient(true);
                    EventBus.getDefault().post(new MyEventBusFromMainFragment(true, false));

                    //做一个数组
                    selectList = new ArrayList<>();
                    for (int i = 0; i < itemNum; i++) {
                        selectList.add(i, false);
                    }

                    isOnDeleteState = true;
                }

                break;
        }
    }

}
