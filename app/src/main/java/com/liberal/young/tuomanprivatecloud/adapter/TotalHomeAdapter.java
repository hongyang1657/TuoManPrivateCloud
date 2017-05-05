package com.liberal.young.tuomanprivatecloud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import java.util.List;

/**
 * Created by Administrator-23:07 on 2017/4/25.
 * Description:
 * Blog：www.qiuchengjia.cn
 * Author: Young_H
 */
public class TotalHomeAdapter extends RecyclerView.Adapter<TotalHomeAdapter.FormsViewHolder> {

    private LayoutInflater inflater;
    private List<Integer> mTotalDatas;
    private Context context;
    private int dayOfMonth;

    public TotalHomeAdapter(Context context,List<Integer> mTotalDatas,int dayOfMonth) {
        this.mTotalDatas = mTotalDatas;
        this.context = context;
        this.dayOfMonth = dayOfMonth;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TotalHomeAdapter.FormsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TotalHomeAdapter.FormsViewHolder holder = new TotalHomeAdapter.FormsViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.report_forms_item, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(TotalHomeAdapter.FormsViewHolder holder, int position) {
        holder.tvYield.setText(mTotalDatas.get(position)+"");
        holder.tvPercent.setText("" + position);
        if (position==dayOfMonth-1){
            holder.rlItemBack.setBackground(context.getResources().getDrawable(R.drawable.round_rect_back_red));
        }else {
            holder.rlItemBack.setBackground(context.getResources().getDrawable(R.drawable.round_rect_back));
        }
    }


    @Override
    public int getItemCount() {
        return mTotalDatas.size();
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

    public void notifyDate(List<Integer> mTotalDatas){
        this.mTotalDatas = mTotalDatas;
        notifyDataSetChanged();
    }

}