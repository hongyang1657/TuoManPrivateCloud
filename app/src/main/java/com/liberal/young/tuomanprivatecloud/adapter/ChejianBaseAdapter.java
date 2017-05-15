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

public class ChejianBaseAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> mChejianNameList;

    public ChejianBaseAdapter(Context context, List<String> mChejianNameList){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mChejianNameList = mChejianNameList;
    }

    @Override
    public int getCount() {
        return mChejianNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChejianNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.chejian_item,parent,false);
            holder = new ViewHolder();
            holder.tvChejianName = (TextView) convertView.findViewById(R.id.tv_chejian);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvChejianName.setText(mChejianNameList.get(position));
        return convertView;
    }

    public void getDataNotify(List<String> mChejianNameList){
        this.mChejianNameList = mChejianNameList;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvChejianName;
    }
}
