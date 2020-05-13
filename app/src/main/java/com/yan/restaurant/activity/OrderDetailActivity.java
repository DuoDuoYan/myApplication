package com.yan.restaurant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yan.restaurant.R;
import com.yan.restaurant.adapters.CustomerOrderAdapter;
import com.yan.restaurant.bean.CustomerOrderBean;
import com.yan.restaurant.bean.Orders;
import com.yan.restaurant.utils.BaseUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView back;
    private Context context;
    private CustomerOrderAdapter customerOrderAdapter;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        context = OrderDetailActivity.this;
        listView = findViewById(R.id.customer_order_list);

        //初始化控件
        initView();
        //设置监听
        initListener();
    }

    private void initView() {
        back = findViewById(R.id.detail_back);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                String phone = intent.getStringExtra("phone");
                List<Orders> ordersList = BaseUtils.getCustomerOrder(phone);
                Log.v("OrderDetailActivity","ordersList"+ordersList);
                List<CustomerOrderBean> customerOrderBeans = new ArrayList<>();
                if(!ordersList.isEmpty()){
                    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //填充数据
                    for(int i=0;i<ordersList.size();i++){
                        CustomerOrderBean customerOrderBean = new CustomerOrderBean();
                        customerOrderBean.setOrderNum(ordersList.get(i).getId().toString());
                        customerOrderBean.setDate(sdff.format(new Date(ordersList.get(i).getOrderDate())));
                        customerOrderBean.setTotalPrice(ordersList.get(i).getTotalPrice().toString());
                        customerOrderBean.setStatus(ordersList.get(i).getStatus());
                        customerOrderBeans.add(customerOrderBean);
                    }
                    customerOrderAdapter = new CustomerOrderAdapter(customerOrderBeans,context);
                    OrderDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(customerOrderAdapter);
                        }
                    });
                }
            }
        }).start();
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
