package com.liberal.young.tuomanprivatecloud.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.fragment.DayYieldFragment;
import com.liberal.young.tuomanprivatecloud.fragment.MainFragment;
import com.liberal.young.tuomanprivatecloud.fragment.MonthYieldFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/16.
 */

public class TableQueryActivity extends BaseActivity {


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
    @BindView(R.id.tv_day)
    RadioButton tvDay;
    @BindView(R.id.tv_month)
    RadioButton tvMonth;
    @BindView(R.id.fl_blank_table)
    FrameLayout flBlankTable;

    private DayYieldFragment dayYieldFragment;
    private MonthYieldFragment monthYieldFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_query_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("几号线，几车间");
        ivTitleLeft.setImageResource(R.mipmap.back);
        ivTitleRight.setImageResource(R.mipmap.more_title);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        dayYieldFragment = new DayYieldFragment();
        transaction.replace(R.id.fl_blank_table, dayYieldFragment);
        transaction.commit();
    }

    @OnClick({R.id.iv_title_left, R.id.iv_title_right, R.id.tv_day, R.id.tv_month})
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.iv_title_left:
                finish();
                break;
            case R.id.iv_title_right:
                break;
            case R.id.tv_day:
                dayYieldFragment = new DayYieldFragment();
                transaction.replace(R.id.fl_blank_table, dayYieldFragment);
                transaction.commit();
                break;
            case R.id.tv_month:
                monthYieldFragment = new MonthYieldFragment();
                transaction.replace(R.id.fl_blank_table, monthYieldFragment);
                transaction.commit();
                break;
        }
    }
}
