package com.liberal.young.tuomanprivatecloud;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.liberal.young.tuomanprivatecloud.utils.L;

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
    @BindView(R.id.rb_auto_line)
    RadioButton rbAutoLine;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;

    private MyApplication application;
    private MainFragment mainFragment = null;
    private MachineFragment machineFragment = null;
    private MineFragment mineFragment = null;
    private QueryFragment queryFragment = null;

    private String user_type;
    private static final String USER_TYPE_SUPER_ADMIN = "1";         //超级管理员
    private static final String USER_TYPE_ADMIN = "2";         //管理员
    private static final String USER_TYPE_CLIENT_MAIN = "3";         //超级客户
    private static final String USER_TYPE_CLIENT_SECOND = "4";         //普通客户
    private static final String USER_TYPE_CLIENT_WORKER = "5";       //操作工

    private FragmentManager fm;
    private FragmentTransaction transaction;

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
    public void onEventMainThread(MyEventBusFromMainFragment event) {
        L.i("MyEventBusFromMainFragment------------------");
        tvDelete.setText("删除");
        ivDelete.setImageResource(R.mipmap.delete);
        if (event.isOnDeleteState()) {
            llDelete.setVisibility(View.VISIBLE);

        } else {
            llDelete.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEventSecondThread(MyEventBusMachineFragment event) {
        L.i("MyEventBusMachineFragment------------------");
        tvDelete.setText("开启");
        ivDelete.setImageResource(R.mipmap.machine_open_lots);
        if (event.isBatching()) {
            llDelete.setVisibility(View.VISIBLE);
        } else {
            llDelete.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.rb_main, R.id.rb_search, R.id.rb_mine, R.id.rb_auto_line})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_main:
                FragmentTransaction transaction1 = fm.beginTransaction();
                if (queryFragment != null) {
                    transaction1.hide(queryFragment);
                }
                if (mineFragment != null) {
                    transaction1.hide(mineFragment);
                }
                if (machineFragment != null) {
                    transaction1.hide(machineFragment);
                }
                transaction1.show(mainFragment);
                transaction1.commit();
                break;
            case R.id.rb_auto_line:
                FragmentTransaction transaction4 = fm.beginTransaction();
                if (queryFragment != null) {
                    transaction4.hide(queryFragment);
                }
                if (mineFragment != null) {
                    transaction4.hide(mineFragment);
                }
                if (mainFragment != null) {
                    transaction4.hide(mainFragment);
                }
                transaction4.show(machineFragment);
                transaction4.commit();
                break;
            case R.id.rb_search:
                FragmentTransaction transaction2 = fm.beginTransaction();
                if (mainFragment != null) {
                    transaction2.hide(mainFragment);
                }
                if (mineFragment != null) {
                    transaction2.hide(mineFragment);
                }
                if (machineFragment != null) {
                    transaction2.hide(machineFragment);
                }
                transaction2.show(queryFragment);
                transaction2.commit();
                break;
            case R.id.rb_mine:
                FragmentTransaction transaction3 = fm.beginTransaction();
                if (machineFragment != null) {
                    transaction3.hide(machineFragment);
                }
                if (mainFragment != null) {
                    transaction3.hide(mainFragment);
                }
                if (queryFragment != null) {
                    transaction3.hide(queryFragment);
                }
                transaction3.show(mineFragment);
                transaction3.commit();
                break;
        }
    }

    //判断用户的权限
    private void judgeAdmin() {
        application = (MyApplication) getApplication();
        user_type = application.getUserLimits();
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        queryFragment = new QueryFragment();
        mineFragment = new MineFragment();

        if (user_type==null){
            SharedPreferences sharedPreferences = getSharedPreferences("LoginInformation",MODE_PRIVATE);
            user_type = sharedPreferences.getString("userLimits","");
        }
        L.i("zmhuis-------------------------:"+user_type);
        switch (user_type) {
            case USER_TYPE_SUPER_ADMIN:        //超级管理员
                rbMain.setVisibility(View.VISIBLE);
                rbMain.setChecked(true);
                rbAutoLine.setVisibility(View.GONE);
                mainFragment = new MainFragment();
                transaction.replace(R.id.fl_blank, queryFragment);
                transaction.add(R.id.fl_blank, mineFragment);
                transaction.add(R.id.fl_blank, mainFragment);
                transaction.commit();
                rbSearch.setClickable(false);
                break;
            case USER_TYPE_ADMIN:            //管理员权限
                mainFragment = new MainFragment();
                rbMain.setVisibility(View.VISIBLE);
                rbMain.setChecked(true);
                rbAutoLine.setVisibility(View.GONE);
                rbSearch.setClickable(false);
                transaction.replace(R.id.fl_blank, queryFragment);
                transaction.add(R.id.fl_blank, mineFragment);
                transaction.add(R.id.fl_blank, mainFragment);
                transaction.commit();
                break;
            case USER_TYPE_CLIENT_MAIN:      //超级客户权限
                rbMain.setVisibility(View.GONE);
                rbAutoLine.setVisibility(View.VISIBLE);
                rbAutoLine.setChecked(true);
                machineFragment = new MachineFragment();
                transaction.replace(R.id.fl_blank, queryFragment);
                transaction.add(R.id.fl_blank, mineFragment);
                transaction.add(R.id.fl_blank, machineFragment);
                transaction.commit();
                break;
            case USER_TYPE_CLIENT_SECOND:     //普通客户权限
                rbMain.setVisibility(View.GONE);
                rbAutoLine.setVisibility(View.VISIBLE);
                rbAutoLine.setChecked(true);
                machineFragment = new MachineFragment();
                transaction.replace(R.id.fl_blank, queryFragment);
                transaction.add(R.id.fl_blank, mineFragment);
                transaction.add(R.id.fl_blank, machineFragment);
                transaction.commit();
                break;
            case USER_TYPE_CLIENT_WORKER:      //操作工权限
                rbMain.setVisibility(View.GONE);
                rbAutoLine.setVisibility(View.VISIBLE);
                rbAutoLine.setChecked(true);
                machineFragment = new MachineFragment();
                transaction.replace(R.id.fl_blank, queryFragment);
                transaction.add(R.id.fl_blank, mineFragment);
                transaction.add(R.id.fl_blank, machineFragment);
                transaction.commit();
                break;
            default:

                break;
        }
    }

    //删除按钮监听
    @OnClick(R.id.ll_delete)
    public void onClick() {
        if (user_type.equals("1") || user_type.equals("2")) {
            EventBus.getDefault().post(new MyEventBusFromMainFragment(true, true));
        } else if (user_type.equals("3") || user_type.equals("4") || user_type.equals("5")){
            EventBus.getDefault().post(new MyEventBusMachineFragment(true, true));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isExit = false;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mhandler.sendMessageDelayed(new Message(), 2000);
        } else if (isExit) {
            //广播，关闭所有activity资源，退出程序
            MyApplication.quiteApplication();
        }
    }

}
