package com.liberal.young.tuomanprivatecloud.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liberal.young.tuomanprivatecloud.R;

import java.util.List;

/**
 * Created by Administrator-17:19 on 2017/4/20.
 * Description:
 * Blog：www.qiuchengjia.cn
 * Author: Young_H
 */
public class WorkShopBaseAdapter extends BaseAdapter{

    private Context context;
    private LayoutInflater inflater;
    private List<String> mWorkshopList;

    public WorkShopBaseAdapter(List<String> mWorkshopList, Context context) {
        inflater = LayoutInflater.from(context);
        this.mWorkshopList = mWorkshopList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mWorkshopList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWorkshopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.work_shop_item,parent,false);
            holder = new ViewHolder();
            holder.tvWorkShop = (TextView) convertView.findViewById(R.id.tv_pop_item);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvWorkShop.setText(mWorkshopList.get(position));
        return convertView;
    }

    class ViewHolder{
        TextView tvWorkShop;
    }
}
