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
 * Created by Administrator on 2017/3/20.
 */

public class ManageClientBaseAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> mClientNameList;
    private List<String> mClientPhoneList;

    public ManageClientBaseAdapter(Context context,List<String> mClientNameList,List<String> mClientPhoneList){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mClientNameList = mClientNameList;
        this.mClientPhoneList = mClientPhoneList;
    }

    @Override
    public int getCount() {
        return mClientNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mClientNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.manage_client_item,parent,false);
            holder = new ViewHolder();
            holder.ivClientHead = (ImageView) convertView.findViewById(R.id.iv_manage_client_head);
            holder.tvClientName = (TextView) convertView.findViewById(R.id.tv_manage_client_name);
            holder.tvClientPhoneNumber = (TextView) convertView.findViewById(R.id.tv_manage_client_phonenumber);
            holder.tvClientDetail = (TextView) convertView.findViewById(R.id.tv_manage_client_detail);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvClientName.setText(mClientNameList.get(position));
        holder.tvClientPhoneNumber.setText(mClientPhoneList.get(position));
        return convertView;
    }

    public void getDataNotify(List<String> mClientNameList,List<String> mClientPhoneList){
        this.mClientNameList = mClientNameList;
        this.mClientPhoneList = mClientPhoneList;
    }

    class ViewHolder{
        ImageView ivClientHead;
        TextView tvClientName;
        TextView tvClientPhoneNumber;
        TextView tvClientDetail;
    }
}
