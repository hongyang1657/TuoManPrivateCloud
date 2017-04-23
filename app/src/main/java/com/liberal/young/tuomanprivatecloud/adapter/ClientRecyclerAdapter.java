package com.liberal.young.tuomanprivatecloud.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;
import com.liberal.young.tuomanprivatecloud.utils.CircleTransformPicasso;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class ClientRecyclerAdapter extends RecyclerView.Adapter<ClientRecyclerAdapter.ClientViewHolder>implements View.OnClickListener{

    private Context context;
    private LayoutInflater inflater;
    private List<String> clientNameList;
    private List<String> clientHeadList;
    private List<Integer> clientLinkNum;
    private boolean toDeleteClient = false;
    private List<Boolean> selectList;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ClientRecyclerAdapter(Context context,List<String> clientHeadList, List<String> clientNameList,List<Integer> clientLinkNum) {
        inflater = LayoutInflater.from(context);
        this.clientHeadList = clientHeadList;
        this.clientNameList = clientNameList;
        this.clientLinkNum = clientLinkNum;
        this.context = context;
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.client_item,parent,false);
        view.setOnClickListener(this);
        ClientViewHolder holder = new ClientViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        holder.tvClientName.setText(clientNameList.get(position));
        holder.tvConnectNum.setText("连接数:"+clientLinkNum.get(position));
        Picasso.with(context).load(clientHeadList.get(position))
                .transform(new CircleTransformPicasso())
                .placeholder(R.mipmap.head)
                .error(R.mipmap.head).into(holder.ivClientHead);
        if (toDeleteClient){
            holder.ivClientDelete.setVisibility(View.VISIBLE);
            holder.tvConnectNum.setVisibility(View.INVISIBLE);
            if (selectList!=null&&selectList.get(position)){
                holder.ivClientDelete.setImageResource(R.mipmap.delete_select_on);
            }else if (selectList!=null&&!selectList.get(position)){
                holder.ivClientDelete.setImageResource(R.mipmap.delete_select_off);
            }else {
                holder.ivClientDelete.setImageResource(R.mipmap.delete_select_off);
            }
        }else {
            holder.ivClientDelete.setVisibility(View.INVISIBLE);
            holder.tvConnectNum.setVisibility(View.VISIBLE);
        }

        holder.itemView.setTag(position);          //设置item点击传出的数据
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return clientNameList.size();
    }

    //开始删除客户
    public void deleteClient(boolean toDeleteClient){
        this.toDeleteClient = toDeleteClient;
        notifyDataSetChanged();
    }

    //选择删除项
    public void selectItemToDelete(List<Boolean> selectList){
        this.selectList = selectList;
        notifyDataSetChanged();
    }

    //添加item
    public void addItem(int position){
        clientHeadList.add(position,"");
        clientNameList.add(position,"添加项");
        notifyItemInserted(position);      //产生动画的刷新
    }

    //删除item项
    public void removeItem(int position){
        clientNameList.remove(position);
        clientHeadList.remove(position);
        notifyItemRemoved(position);
    }

    public void getNotify(List<String> clientNameList,List<String> clientHeadList,List<Integer> clientLinkNum){
        this.clientHeadList = clientHeadList;
        this.clientNameList = clientNameList;
        this.clientLinkNum = clientLinkNum;
        notifyDataSetChanged();
    }


    class ClientViewHolder extends RecyclerView.ViewHolder{
        ImageView ivClientHead;
        TextView tvClientName;
        TextView tvConnectNum;
        ImageView ivClientDelete;


        public ClientViewHolder(View itemView) {
            super(itemView);
            ivClientHead = (ImageView) itemView.findViewById(R.id.iv_client_item);
            tvClientName = (TextView) itemView.findViewById(R.id.tv_client_item);
            tvConnectNum = (TextView) itemView.findViewById(R.id.tv_connect_num);
            ivClientDelete = (ImageView) itemView.findViewById(R.id.iv_client_delete_select);
        }
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
