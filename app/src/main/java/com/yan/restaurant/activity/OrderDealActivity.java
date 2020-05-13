package com.yan.restaurant.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.yan.restaurant.R;
import com.yan.restaurant.adapters.CookerOrderAdapter;
import com.yan.restaurant.bean.CookerOrderBean;
import com.yan.restaurant.bean.OrderDetail;
import com.yan.restaurant.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderDealActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private CookerOrderAdapter cookAdapter = null;
    private ListView listView;
    private Intent intent;
    private TextView robotNotice,robotDeliver,orderFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_deal);

        //初始化界面
        initView();

        context = OrderDealActivity.this;
        listView = findViewById(R.id.order_detail_list);

        new Thread(new Runnable() {
            @Override
            public void run() {
                intent = getIntent();
                String[] orderInfo = intent.getStringExtra("orderInfo").split(",");
                List<OrderDetail> list = BaseUtils.getOrderDetailByOrderNum(orderInfo[0],orderInfo[1]);
                List<CookerOrderBean> cookerOrderBeans = new ArrayList<>();
                for(int i=0;i<list.size();i++){
                    CookerOrderBean orderBean = new CookerOrderBean();
                    orderBean.setFood(list.get(i).getFood());
                    orderBean.setTable(list.get(i).getTables());
                    orderBean.setNum(list.get(i).getQuantity().toString());
                    orderBean.setOperate("完成");
                    cookerOrderBeans.add(orderBean);
                }
                cookAdapter = new CookerOrderAdapter(cookerOrderBeans,context);

                OrderDealActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(cookAdapter);
                    }
                });
            }
        }).start();
    }

    private void initView() {
        robotNotice = findViewById(R.id.robotNotice);
        robotDeliver = findViewById(R.id.robotDeliver);
        orderFinish = findViewById(R.id.orderFinish);
    }

    @Override
    public void onClick(View v) {
        robotNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知robot

            }
        });
        robotDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String robot = "2";
                //修改订单状态
                intent = getIntent();
                String[] orderInfo = intent.getStringExtra("orderInfo").split(",");
                String result = BaseUtils.updateOrderStatus(null,orderInfo[1],robot,2);
            }
        });
        orderFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = getIntent();
                String[] orderInfo = intent.getStringExtra("orderInfo").split(",");
                String result = BaseUtils.updateOrderStatus(null,orderInfo[1],null,3);
                Intent it = new Intent(OrderDealActivity.this,CookerActivity.class);
                startActivity(it);
            }
        });

    }
}
