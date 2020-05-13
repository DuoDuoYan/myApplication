package com.yan.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.yan.restaurant.R;
import com.yan.restaurant.adapters.RecycleAdapter;
import com.yan.restaurant.bean.DataBean;
import com.yan.restaurant.bean.Orders;
import com.yan.restaurant.listener.OnItemClickListener;
import com.yan.restaurant.utils.BaseUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CookerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<DataBean> dataBeanList;
    private DataBean dataBean;
    private RecycleAdapter mAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooker);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        initData();

    }

    /**
     * 模拟数据
     */
    private void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Orders> list = BaseUtils.getOnOrder();
                dataBeanList = new ArrayList<>();
                if (list.size()>0){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    for (int i = 0; i < list.size(); i++) {
                        List<DataBean> lists = new ArrayList<>();
                        dataBean = new DataBean();
                        dataBean.setID(i+"");
                        dataBean.setType(0);
                        dataBean.setOrderNum(list.get(i).getId().toString());
                        dataBean.setOrderTable(list.get(i).getTables());
                        dataBean.setOrderDate(sdf.format(new Date(list.get(i).getOrderDate())));
                        dataBean.setBtn("接单");
                        DataBean childTitle = new DataBean();
                        childTitle.setFood("菜品名");
                        childTitle.setNum("数量");
                        lists.add(childTitle);
                        for(int j = 0; j < list.get(i).getOrderDetail().size(); j++){
                            DataBean child = new DataBean();
                            child.setFood(list.get(i).getOrderDetail().get(j).getFoodName());
                            child.setNum(list.get(i).getOrderDetail().get(j).getQuantity().toString());
                            lists.add(child);
                        }
                        dataBean.setChild(lists);
                        dataBeanList.add(dataBean);
                    }
                    Log.v("TestActivity","data:"+dataBeanList.get(0));
                }else{
                    dataBean = new DataBean();
                    dataBean.setID(null);
                    dataBean.setType(0);
                    dataBean.setOrderNum(null);
                    dataBean.setOrderTable(null);
                    dataBean.setOrderDate(null);
                    dataBean.setBtn(null);
                    dataBeanList.add(dataBean);
                }
                //处理主线程的UI更新
                CookerActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        getData();
                    }
                });
            }
        }).start();
    }

    public void getData(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecycleAdapter(this,dataBeanList);
        mRecyclerView.setAdapter(mAdapter);
        //滚动监听
        mAdapter.setOnScrollListener(new RecycleAdapter.OnScrollListener() {
            @Override
            public void scrollTo(int pos) {
                mRecyclerView.scrollToPosition(pos);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, final String postion) {
//                Toast.makeText(getApplicationContext(),postion+"",Toast.LENGTH_LONG).show();
//                String cooker =  intent.getStringExtra("phone");
                final int tag = 1;
                final String[] orderNum = postion.split(",");
                //制作中
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String result = BaseUtils.updateOrderStatus("44",orderNum[0],null,tag);
                        Intent intent = new Intent(CookerActivity.this,OrderDealActivity.class);
                        intent.putExtra("orderInfo",postion);
                        startActivity(intent);
                    }
                }).start();

            }
        });
    }


}
