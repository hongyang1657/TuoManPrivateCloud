package com.liberal.young.tuomanprivatecloud;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liberal.young.tuomanprivatecloud.activity.BaseActivity;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusFromMainFragment;
import com.liberal.young.tuomanprivatecloud.bean.eventBus.MyEventBusMachineFragment;
import com.liberal.young.tuomanprivatecloud.fragment.MachineFragment;
import com.liberal.young.tuomanprivatecloud.fragment.MainFragment;
import com.liberal.young.tuomanprivatecloud.fragment.MineFragment;
import com.liberal.young.tuomanprivatecloud.fragment.QueryFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.rb_main)
    RadioButton rbMain;
    @BindView(R.id.rb_search)
    RadioButton rbSearch;
    @BindView(R.id.rb_mine)
    RadioButton rbMine;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.fl_blank)
    FrameLayout flBlank;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    private MyApplication application;
    private MainFragment mainFragment;
    private MachineFragment machineFragment;
    private MineFragment mineFragment;
    private QueryFragment queryFragment;

    private static String user_type;
    private static final String USER_TYPE_ADMIN = "1";         //管理员
    private static final String USER_TYPE_CLIENT_MAIN = "2";         //客户
    private static final String USER_TYPE_CLIENT_SECOND = "3";       //二级客户


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {

        judgeAdmin();   //判断用户权限

    }

    @Subscribe
    public void onEventMainThread(MyEventBusFromMainFragment event){
        if (event.isOnDeleteState()){
            tvDelete.setVisibility(View.VISIBLE);
        }else {
            tvDelete.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEventSecondThread(MyEventBusMachineFragment event){
        tvDelete.setText("开启");

        if (event.isBatching()){
            tvDelete.setVisibility(View.VISIBLE);
        }else {
            tvDelete.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.rb_main, R.id.rb_search, R.id.rb_mine})
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.rb_main:
                if (user_type.equals("1") || user_type.equals("")) {
                    mainFragment = new MainFragment();
                    transaction.replace(R.id.fl_blank, mainFragment);
                    transaction.commit();
                } else if (user_type.equals("2") || user_type.equals("3")) {
                    machineFragment = new MachineFragment();
                    transaction.replace(R.id.fl_blank, machineFragment);
                    transaction.commit();
                } else {
                    mainFragment = new MainFragment();
                    transaction.replace(R.id.fl_blank, mainFragment);
                    transaction.commit();
                }
                break;
            case R.id.rb_search:

                queryFragment = new QueryFragment();
                transaction.replace(R.id.fl_blank, queryFragment);
                transaction.commit();
                break;
            case R.id.rb_mine:
                mineFragment = new MineFragment();
                transaction.replace(R.id.fl_blank, mineFragment);
                transaction.commit();
                break;
        }
    }

    //判断用户的权限
    private void judgeAdmin() {
        application = (MyApplication) getApplication();
        user_type = application.getUserLimits();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        switch (user_type) {
            case USER_TYPE_ADMIN:
                //管理员权限
                rbMain.setText("客户");
                mainFragment = new MainFragment();
                transaction.replace(R.id.fl_blank, mainFragment);
                transaction.commit();
                break;
            case USER_TYPE_CLIENT_MAIN:
                //一级客户权限
                rbMain.setText("自动线");
                machineFragment = new MachineFragment();
                transaction.replace(R.id.fl_blank, machineFragment);
                transaction.commit();
                break;
            case USER_TYPE_CLIENT_SECOND:
                //二级客户权限
                rbMain.setText("自动线");
                machineFragment = new MachineFragment();
                transaction.replace(R.id.fl_blank, machineFragment);
                transaction.commit();
                rbSearch.setClickable(false);
                break;
            default:
                //管理员权限
                rbMain.setText("客户");
                mainFragment = new MainFragment();
                transaction.replace(R.id.fl_blank, mainFragment);
                transaction.commit();
                break;
        }
    }

    //删除按钮监听
    @OnClick(R.id.tv_delete)
    public void onClick() {
        EventBus.getDefault().post(new MyEventBusFromMainFragment(true,true));
        EventBus.getDefault().post(new MyEventBusMachineFragment(true,true));
    }
}
