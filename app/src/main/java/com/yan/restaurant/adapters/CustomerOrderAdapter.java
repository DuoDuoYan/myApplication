package com.yan.restaurant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yan.restaurant.R;
import com.yan.restaurant.bean.CustomerOrderBean;
import java.util.List;

public class CustomerOrderAdapter extends BaseAdapter {
    private List<CustomerOrderBean> customerOrderBeans;
    private Context context;

    public CustomerOrderAdapter(List<CustomerOrderBean> customerOrderBeans ,Context context){
        this.customerOrderBeans = customerOrderBeans;
        this.context = context;
    }

    @Override
    public int getCount() {
        return customerOrderBeans.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.customer_order_list,parent,false);
        TextView order = convertView.findViewById(R.id.customer_order);
        TextView date = convertView.findViewById(R.id.customer_date);
        TextView price = convertView.findViewById(R.id.customer_price);
        TextView status = convertView.findViewById(R.id.customer_status);
        order.setText(customerOrderBeans.get(position).getOrderNum());
        date.setText(customerOrderBeans.get(position).getDate());
        price.setText(customerOrderBeans.get(position).getTotalPrice());
        status.setText(customerOrderBeans.get(position).getStatus());
        return convertView;
    }
}
