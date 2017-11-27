package com.pgg.mywechatem.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.pgg.mywechatem.Domian.Moments;
import com.pgg.mywechatem.R;
import com.pgg.mywechatem.Uitils.Utils;

import java.util.ArrayList;

/**
 * Created by PDD on 2017/11/18.
 */

public class MomentsAdapter extends BaseAdapter {

    private ArrayList<Moments> momentses;
    public MomentsAdapter(ArrayList<Moments> momentses) {
        this.momentses=momentses;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return momentses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MomentsViewHolder viewHolder=null;
        if (convertView==null){
            convertView= Utils.getView(R.layout.list_moments_item);
            viewHolder=new MomentsViewHolder();
            viewHolder.txt_name=convertView.findViewById(R.id.txt_content);
            viewHolder.txt_message=convertView.findViewById(R.id.txt_msg);
            viewHolder.time=convertView.findViewById(R.id.txt_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(MomentsViewHolder)convertView.getTag();
        }
        viewHolder.txt_name.setText(momentses.get(position).user_name);
        viewHolder.txt_message.setText(momentses.get(position).message);
        viewHolder.time.setText(momentses.get(position).time);
        return convertView;
    }
    class MomentsViewHolder {
        public TextView txt_name;
        public TextView txt_message;
        public TextView time;
    }
}
