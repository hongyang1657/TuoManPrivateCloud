package com.liberal.young.tuomanprivatecloud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.L;

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
    private List<Integer> mDatas;
    private List<Integer> lineForecast;
    private Context context;
    private int dayOfMonth;

    public TotalHomeAdapter(Context context,List<Integer> mTotalDatas,int dayOfMonth,List<Integer> mDatas,List<Integer> lineForecast) {
        this.mTotalDatas = mTotalDatas;
        this.context = context;
        this.dayOfMonth = dayOfMonth;
        this.mDatas = mDatas;
        this.lineForecast = lineForecast;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public FormsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FormsViewHolder holder = new FormsViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.report_forms_item, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(FormsViewHolder holder, int position) {
        int[] s = new int[31];
        for (int i=0;i<mDatas.size();i = i+(mDatas.size()/31)){
            int sum = 0;
            for (int n=i;n<i+(mDatas.size()/31);n++){
                sum = sum+mDatas.get(n);
                //L.i(i+"----"+n+"summmm"+sum);
                s[n/(mDatas.size()/31)] = sum;
            }
        }
        int sumFC = 0;
        for (int i=0;i<lineForecast.size();i++){
            sumFC = sumFC+lineForecast.get(i);
        }
        double a = (double) s[position]/(double) sumFC;

        holder.tvYield.setText(s[position]+"");
        holder.tvPercent.setText((int)(a*100)+"%");
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

    public void notifyDate(List<Integer> mTotalDatas,List<Integer> mDatas,List<Integer> lineForecast){
        this.mTotalDatas = mTotalDatas;
        this.mDatas = mDatas;
        this.lineForecast = lineForecast;
        notifyDataSetChanged();
    }

}