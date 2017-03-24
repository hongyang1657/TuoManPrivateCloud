package com.liberal.young.tuomanprivatecloud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class MachineRecyclerAdapter extends RecyclerView.Adapter<MachineRecyclerAdapter.MachineViewHolder>implements View.OnClickListener{

    private Context context;
    private LayoutInflater inflater;
    private List<String> machineNameList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private boolean isBatching = false;
    private List<Boolean> selectList;

    public MachineRecyclerAdapter(Context context, List<String> machineNameList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.machineNameList = machineNameList;
    }

    @Override
    public MachineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.machine_item,parent,false);
        view.setOnClickListener(this);
        MachineViewHolder holder = new MachineViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MachineViewHolder holder, int position) {
        holder.tvMachine.setText(machineNameList.get(position));
        if (isBatching){
            holder.ivSelectMachine.setVisibility(View.VISIBLE);
            holder.tvIsMachineOn.setVisibility(View.GONE);
            if (selectList!=null&&selectList.get(position)){
                holder.ivSelectMachine.setImageResource(R.mipmap.selecter_on);
            }else if (selectList!=null&&!selectList.get(position)){
                holder.ivSelectMachine.setImageResource(R.mipmap.selecter_off);
            }else {
                holder.ivSelectMachine.setImageResource(R.mipmap.selecter_off);
            }
        }else {
            holder.ivSelectMachine.setVisibility(View.GONE);
            holder.tvIsMachineOn.setVisibility(View.VISIBLE);
        }

        holder.itemView.setTag(position);          //设置item点击传出的数据
    }

    @Override
    public int getItemCount() {
        return machineNameList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(Integer)v.getTag());
        }
    }

    class MachineViewHolder extends RecyclerView.ViewHolder{

        ImageView ivMachine;
        TextView tvMachine;
        TextView tvStandardYield;
        TextView tvIsMachineOn;
        ImageView ivSelectMachine;

        public MachineViewHolder(View itemView) {
            super(itemView);
            ivMachine = (ImageView) itemView.findViewById(R.id.iv_machine_item);
            tvMachine = (TextView) itemView.findViewById(R.id.tv_machine_item);
            tvStandardYield = (TextView) itemView.findViewById(R.id.tv_standard_yield);
            tvIsMachineOn = (TextView) itemView.findViewById(R.id.tv_is_machine_on_item);
            ivSelectMachine = (ImageView) itemView.findViewById(R.id.iv_select_machine);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(MachineRecyclerAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void batchProcessing(boolean isBatching){
        this.isBatching = isBatching;
        notifyDataSetChanged();
    }

    //选择项目批量处理
    public void selectItemToBatch(List<Boolean> selectList){
        this.selectList = selectList;
        notifyDataSetChanged();
    }
}
