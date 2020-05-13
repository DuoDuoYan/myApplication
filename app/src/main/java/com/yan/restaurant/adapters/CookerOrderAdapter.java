package com.yan.restaurant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yan.restaurant.R;
import com.yan.restaurant.bean.CookerOrderBean;

import java.util.List;

public class CookerOrderAdapter extends BaseAdapter {

    private List<CookerOrderBean> cookerOrderBeans;
    private Context context;

    public CookerOrderAdapter(List<CookerOrderBean> orderBeans,Context context){
        this.cookerOrderBeans = orderBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cookerOrderBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.cooker_order_detail,parent,false);
        TextView food = convertView.findViewById(R.id.deal_food);
        TextView table = convertView.findViewById(R.id.deal_table);
        TextView num = convertView.findViewById(R.id.deal_num);
        TextView operate = convertView.findViewById(R.id.deal_operate);
        food.setText(cookerOrderBeans.get(position).getFood());
        table.setText(cookerOrderBeans.get(position).getNum());
        num.setText(cookerOrderBeans.get(position).getNum());
        operate.setText(cookerOrderBeans.get(position).getOperate());
        return convertView;
    }
}
