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
 * Created by Administrator on 2017/3/15.
 */

public class ProducLineRecyclerAdapter extends RecyclerView.Adapter<ProducLineRecyclerAdapter.LineViewHolder>implements View.OnClickListener{
    private LayoutInflater inflater;
    private List<String> lineList;
    private List<Integer> lineTotalList;
    private List<Integer> mDatas;
    private List<Integer> lineForecast;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ProducLineRecyclerAdapter(Context context,List<String> lineList,List<Integer> lineTotalList,List<Integer> mDatas,List<Integer> lineForecast) {
        inflater = LayoutInflater.from(context);
        this.lineList = lineList;
        this.mDatas = mDatas;
        this.lineTotalList = lineTotalList;
        this.lineForecast = lineForecast;
    }

    @Override
    public LineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.production_line_item,parent,false);
        view.setOnClickListener(this);
        LineViewHolder holder = new LineViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LineViewHolder holder, int position) {
        holder.tvProducLine.setText(lineList.get(position));
        //holder.tvPercent.setText("99%");
        if (position==0){
            int sum = 0;
            for (int i=0;i<mDatas.size();i++){
                sum = sum+mDatas.get(i);
            }
            holder.tvYield.setText(sum+"");  //总
        }else {

            int sum = 0;
            /*for (int i=0+(31*(position-1));i<31*(position);i++){
                sum = sum+mDatas.get(i);
                L.i(i+"aaaaaaaaaaa"+mDatas.get(i));
            }*/
            for (int i=position-1;i<mDatas.size();i = i+mDatas.size()/31){
                sum = sum+mDatas.get(i);
                //L.i(i+"asdfw"+mDatas.get(i));
            }
            holder.tvYield.setText(sum+"");
        }
        holder.itemView.setTag(lineList.get(position));
    }

    @Override
    public int getItemCount() {
        return lineList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null){
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(String)v.getTag());
        }
    }

    class LineViewHolder extends RecyclerView.ViewHolder {

        TextView tvProducLine;
        TextView tvYield;
        TextView tvPercent;

        public LineViewHolder(View view) {
            super(view);
            tvProducLine = (TextView) view.findViewById(R.id.tv_production_line_num);
            tvYield = (TextView) view.findViewById(R.id.tv_yield_numb);
            tvPercent = (TextView) view.findViewById(R.id.tv_yield_percent);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void notifyLineDate(List<String> lineList,List<Integer> lineTotalList,List<Integer> mDatas,List<Integer> lineForecast){
        this.lineList = lineList;
        this.lineTotalList = lineTotalList;
        this.mDatas = mDatas;
        this.lineForecast = lineForecast;
        notifyDataSetChanged();
    }
}
