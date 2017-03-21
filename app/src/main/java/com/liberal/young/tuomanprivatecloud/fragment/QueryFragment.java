package com.liberal.young.tuomanprivatecloud.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.adapter.DataTitleRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.adapter.ProducLineRecyclerAdapter;
import com.liberal.young.tuomanprivatecloud.view.MyFormScrollView;
import com.liberal.young.tuomanprivatecloud.view.ScrollViewListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/10.
 */

public class QueryFragment extends Fragment implements ScrollViewListener{


    @BindView(R.id.tv_switch_month)
    TextView tvSwitchMonth;
    @BindView(R.id.tv_switch_workshop)
    TextView tvSwitchWorkshop;
    @BindView(R.id.iv_rank)
    ImageView ivRank;
    @BindView(R.id.ll_title_forms)
    LinearLayout llTitleForms;
    @BindView(R.id.tv_corner_title)
    TextView tvCornerTitle;
    @BindView(R.id.rv_forms_title)
    RecyclerView rvFormsTitle;
    @BindView(R.id.rv_forms_production_line)
    RecyclerView rvFormsProductionLine;
    @BindView(R.id.rv_recycler)
    RecyclerView rvRecycler;
    private MyFormScrollView svForms;

    private List<String> mDatas;
    private HomeAdapter mAdapter;

    private List<String> lineList;
    private List<String> dateList;

    public QueryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report_forms_layout, container, false);
        initData();
        initView(view);
        ButterKnife.bind(this, view);
        syncScroll(rvFormsTitle,rvRecycler);  //两个横向的RecyclerView联动
        return view;
    }

    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 0; i < 18*30; i++) {
            mDatas.add("" +i);
        }
        lineList = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            lineList.add(i + "号线");
        }
        dateList = new ArrayList<>();
        for (int i = 1;i < 31; i++){
            dateList.add(i+"日");
        }

    }

    private void initView(View view) {
        svForms = (MyFormScrollView) view.findViewById(R.id.sv_forms);
        svForms.setScrollViewListener(this);

        //三个RecyclerView
        rvRecycler = (RecyclerView) view.findViewById(R.id.rv_recycler);
        rvRecycler.setLayoutManager(new StaggeredGridLayoutManager(18, StaggeredGridLayoutManager.HORIZONTAL));
        rvRecycler.setAdapter(mAdapter = new HomeAdapter());

        rvFormsProductionLine = (RecyclerView) view.findViewById(R.id.rv_forms_production_line);
        rvFormsProductionLine.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        ProducLineRecyclerAdapter lineAdapter = new ProducLineRecyclerAdapter(getActivity(),lineList);
        lineAdapter.setOnItemClickListener(new ProducLineRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                
            }
        });
        rvFormsProductionLine.setAdapter(lineAdapter);

        rvFormsTitle = (RecyclerView) view.findViewById(R.id.rv_forms_title);
        rvFormsTitle.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        DataTitleRecyclerAdapter adapter = new DataTitleRecyclerAdapter(getActivity(),dateList);
        adapter.setOnItemClickListener(new DataTitleRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {

            }
        });
        rvFormsTitle.setAdapter(adapter);
    }

    @OnClick({R.id.tv_switch_month, R.id.tv_switch_workshop, R.id.iv_rank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_switch_month:
                break;
            case R.id.tv_switch_workshop:
                break;
            case R.id.iv_rank:
                break;
        }
    }



    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.FormsViewHolder> {

        @Override
        public FormsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FormsViewHolder holder = new FormsViewHolder(LayoutInflater.from(getActivity())
                    .inflate(R.layout.report_forms_item, parent, false));

            return holder;
        }

        @Override
        public void onBindViewHolder(FormsViewHolder holder, int position) {
            holder.tvYield.setText(mDatas.get(position));
            holder.tvPercent.setText("" + position);
        }


        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class FormsViewHolder extends RecyclerView.ViewHolder {

            RelativeLayout rlItemBack;
            TextView tvYield;
            TextView tvPercent;

            public FormsViewHolder(View view) {
                super(view);
                rlItemBack = (RelativeLayout) view.findViewById(R.id.rl_report_form_item_back);
                tvYield = (TextView) view.findViewById(R.id.tv_yield_numb);
                tvPercent = (TextView) view.findViewById(R.id.tv_yield_percent);
            }
        }
    }


    private void syncScroll(final RecyclerView topRecycler, final RecyclerView bottomRecycler) {
        topRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    bottomRecycler.scrollBy(dx, dy);
                }
            }
        });

        bottomRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    topRecycler.scrollBy(dx, dy);
                }
            }
        });

    }

    //自定义的滑动监听接口
    @Override
    public void onScrollChanged(MyFormScrollView scrollView, int x, int y, int oldx, int oldy) {
        Log.i("result", "onScrollChanged。。。ScrollView的监听: "+"----y:"+y+"  oldy:"+oldy);
        rvFormsProductionLine.scrollBy(x,y-oldy);
    }
}
