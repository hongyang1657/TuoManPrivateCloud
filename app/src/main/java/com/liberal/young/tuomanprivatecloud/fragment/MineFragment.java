package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.MyApplication;
import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.activity.LoginActivity;
import com.liberal.young.tuomanprivatecloud.activity.ManageClientActivity;
import com.liberal.young.tuomanprivatecloud.activity.ModifiPwdActivity;
import com.liberal.young.tuomanprivatecloud.activity.SettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * com.liberal.young.tuomanprivatecloud.fragment.MineFragment
 * Created by Administrator on 2017/3/10.
 */

public class MineFragment extends Fragment {


    @BindView(R.id.iv_mine_account)
    ImageView ivMineAccount;
    @BindView(R.id.tv_admin)
    TextView tvAdmin;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.rl_mine_account)
    RelativeLayout rlMineAccount;
    @BindView(R.id.tv_user_management)
    TextView tvUserManagement;
    @BindView(R.id.tv_user_modifi_pwd)
    TextView tvUserModifiPwd;
    @BindView(R.id.tv_user_help)
    TextView tvUserHelp;
    @BindView(R.id.tv_user_setting)
    TextView tvUserSetting;
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
    @BindView(R.id.tv_hehe)
    TextView tvHehe;

    private MyApplication application;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment_layout, container, false);
        initView(view);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view) {
        ivTitleLeft = (ImageView) view.findViewById(R.id.iv_title_left);
        ivTitleRight = (ImageView) view.findViewById(R.id.iv_title_right);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvUserManagement = (TextView) view.findViewById(R.id.tv_user_management);
        tvHehe = (TextView) view.findViewById(R.id.tv_hehe);
        ivTitleLeft.setVisibility(View.GONE);
        ivTitleRight.setVisibility(View.GONE);
        tvTitle.setText("我的");
        tvTitle.setTextColor(getResources().getColor(R.color.colorBlueShade));
        application = (MyApplication) getActivity().getApplication();
        String userLimits = application.getUserLimits();
        if (userLimits.equals("1")||userLimits.equals("2")) {     //管理员
            tvUserManagement.setText("客户列表");
        } else if (userLimits.equals("3")||userLimits.equals("4")) {     //客户
            tvUserManagement.setText("操作工管理");
        } else if (userLimits.equals("5")) {
            tvHehe.setVisibility(View.GONE);
            tvUserManagement.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right, R.id.tv_user_management, R.id.tv_user_modifi_pwd, R.id.tv_user_help, R.id.tv_user_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_left:
                break;
            case R.id.iv_title_right:
                break;
            case R.id.tv_user_management:
                Intent intent = new Intent(getActivity(), ManageClientActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_user_modifi_pwd:
                Intent intent2 = new Intent(getActivity(), ModifiPwdActivity.class);
                startActivity(intent2);
                break;
            case R.id.tv_user_help:
                break;
            case R.id.tv_user_setting:
                Intent intent1 = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent1,100);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==-1){
            switch (requestCode){
                case 100:
                    Intent intentOut = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intentOut);
                    getActivity().finish();
                    System.gc();
                    break;
            }
        }
    }
}
