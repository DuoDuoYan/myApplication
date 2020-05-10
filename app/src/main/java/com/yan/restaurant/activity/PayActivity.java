package com.yan.restaurant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yan.restaurant.R;
import com.yan.restaurant.adapters.CarAdapter;
import com.yan.restaurant.bean.FoodBean;
import com.yan.restaurant.bean.Table;
import com.yan.restaurant.utils.BaseUtils;
import com.yan.restaurant.utils.ConnectService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PayActivity extends AppCompatActivity {

    private TextView price;
    private ImageView payBack;
    private Button payBtn;
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;//存放数据
    private List<Table> tableList;
    private String table;
    private String phone;
    private String payPrice;
    public List<FoodBean> foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);
        //设置初始化视图
        initView();
        //事件监听
        setListener();
    }

    private void initView() {
        price = findViewById(R.id.pay_price);
        payBack = findViewById(R.id.payBack);
        spinner = findViewById(R.id.tables);
        payBtn = findViewById(R.id.payBtn);


        Intent intent = getIntent();
        price.setText(intent.getStringExtra("price"));
        phone = intent.getStringExtra("phone");
        payPrice= intent.getStringExtra("price");

        tableList = BaseUtils.getTables();
        final List<String> tableNum = new ArrayList<>();
        tableNum.add("--------");
        for(int i=0;i<tableList.size();i++){
            tableNum.add(tableList.get(i).getNum());
        }
        //菜单
        Intent intentGet = getIntent();
        foods = (List<FoodBean>) intentGet.getSerializableExtra("data");

        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tableNum);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将arrayAdapter添加到spinner中
        spinner.setAdapter(arrayAdapter);
        //添加事件spinner事件监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                table = tableNum.get(pos);
                //设置显示当前选择的项
                parent.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void setListener() {
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("--------".equals(table)){
                    Toast.makeText(getApplicationContext(),"请选择当前餐桌号！",Toast.LENGTH_LONG).show();
                }else{
                    String balance = BaseUtils.getBalance(phone);
                    if(Double.valueOf(payPrice.substring(1))>Double.valueOf(balance)){
                        Toast.makeText(getApplicationContext(),"余额不足，请充值",Toast.LENGTH_LONG).show();
                    }else{
                        //支付
                        String result = BaseUtils.updateBalance(phone,payPrice);
                        //生成订单
                        StringBuffer ids = new StringBuffer();
                        for(int i=0;i<foods.size();i++){
                            ids.append(foods.get(i).getId());
                            ids.append(",");
                        }
                        int tableId = 0;
                        for(int i=0;i<tableList.size();i++){
                            if(table.equals(tableList.get(i).getNum())){
                                tableId = tableList.get(i).getId();
                            }
                        }
                        String orderResult = BaseUtils.createOrder(ids.toString(),phone,tableId,new Date().getTime(),price.getText().toString().substring(1));


                        if("success".equals(result)){
                            Toast.makeText(getApplicationContext(),"支付成功",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PayActivity.this,OrderDetail.class);
//                            intent.putExtra("orderNum",orderNum);
                        }
                    }
                }
            }
        });

    }
}
