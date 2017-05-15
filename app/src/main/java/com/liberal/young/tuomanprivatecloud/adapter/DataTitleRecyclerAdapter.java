package com.liberal.young.tuomanprivatecloud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */

public class DataTitleRecyclerAdapter extends RecyclerView.Adapter<DataTitleRecyclerAdapter.DateViewHolder>implements View.OnClickListener{
    private LayoutInflater inflater;
    private List<String> dateList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public DataTitleRecyclerAdapter(Context context, List<String> dateList) {
        inflater = LayoutInflater.from(context);
        this.dateList = dateList;
    }

    @Override
    public DateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.forms_date_item,parent,false);
        view.setOnClickListener(this);
        DateViewHolder holder = new DateViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(DateViewHolder holder, int position) {
        holder.tvDate.setText(dateList.get(position));
        holder.itemView.setTag(dateList.get(position));          //设置item点击传出的数据
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(String)v.getTag());
        }
    }

    class DateViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;

        public DateViewHolder(View view) {
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tv_date_item);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
