package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liberal.young.tuomanprivatecloud.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MonthYieldFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_yield_fgm_layout,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }
}
