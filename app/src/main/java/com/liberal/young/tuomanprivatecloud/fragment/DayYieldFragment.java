package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/16.
 */

public class DayYieldFragment extends Fragment {

    @BindView(R.id.tv_today_yield)
    TextView tvTodayYield;
    @BindView(R.id.tv_yestoday_yield)
    TextView tvYestodayYield;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_yield_fgm_layout, container, false);
        initView(view);
        ButterKnife.bind(this, view);
        return view;

    }

    private void initView(View view) {

    }
}
