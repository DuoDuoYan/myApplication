package com.yan.restaurant.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.yan.restaurant.R;
import com.yan.restaurant.utils.ConnectService;
import com.yan.restaurant.utils.TimeCountUtil;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView regBack;
    private EditText txtUserName,txtPassword,txtPhone,txtVerify;
    private Button btnRegister,btnVerify;
    EventHandler eventHandler;
    private boolean flag = true;
    private TimeCountUtil timeCountUtil;
    private String verifyCode;
    private String phone;
    private String username;
    private String password;
    private String serviceResult;
    private ConnectService connectService = new ConnectService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //设置初始化视图
        initView();

        //设置监听事件
        setListener();

        //这里的倒计时的时间 是 ：用第二参数 / 第三个三参数 = 倒计时为60秒
        timeCountUtil = new TimeCountUtil(btnVerify, 60000, 1000);

        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void setListener() {
        regBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent it = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(it);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    if(smart) {
                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",Toast.LENGTH_LONG).show();
                        txtPhone.requestFocus();
                        return;
                    }
                }
            }
            Log.v("Result","======"+result);
            if(result == SMSSDK.RESULT_COMPLETE){
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    boolean result = connectService.exist(phone);
                                    if(!result){
                                        serviceResult = connectService.register(username, password, phone);
                                        if ("success".equals(serviceResult)) {
                                            Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(it);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "注册失败。。。", Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), "该手机号已注册。。。", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                }
            }else{
                if(flag){
                    btnVerify.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                    txtPhone.requestFocus();
                }else{
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public void initView(){
        regBack = findViewById(R.id.regBack);
        txtUserName = findViewById(R.id.regUserName);
        txtPassword = findViewById(R.id.regPassword);
        txtPhone = findViewById(R.id.regPhone);
        txtVerify = findViewById(R.id.regVerifyCode);
        btnRegister = findViewById(R.id.btnRegister);
        btnVerify = findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        Drawable user = getResources().getDrawable(R.drawable.user);
        Drawable lock = getResources().getDrawable(R.drawable.lock);
        Drawable phone = getResources().getDrawable(R.drawable.phoneimg);

        user.setBounds(0,0,80,80);
        lock.setBounds(0,0,80,80);
        phone.setBounds(0,0,80,80);

        txtUserName.setCompoundDrawables(user,null,null,null);
        txtPassword.setCompoundDrawables(lock,null,null,null);
        txtPhone.setCompoundDrawables(phone,null,null,null);
    }

    /**
     * 按钮点击事件
     */
    public void onClick(View v){
        Intent it;
        phone = txtPhone.getText().toString().trim();
        username = txtUserName.getText().toString().trim();
        password = txtPassword.getText().toString().trim();
        verifyCode = txtVerify.getText().toString().trim();

        switch (v.getId()){
            case R.id.btnVerify:
                if(checkPhone(phone)){
                    timeCountUtil.start();
                    SMSSDK.getVerificationCode("86",phone);
                    txtVerify.requestFocus();
                }
                break;
            case R.id.btnRegister:
                if(checkText(username,password,phone,verifyCode)){
                    SMSSDK.submitVerificationCode("86",phone,verifyCode);
                    flag = false;
                }
                break;
            case R.id.regBack:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private boolean checkPhone(String phone){
        String num = "[1][358]\\d{9}";
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(RegisterActivity.this,"请输入您的手机号",Toast.LENGTH_LONG).show();
            txtPhone.requestFocus();
            return false;
        }else if(!phone.matches(num)){
            Toast.makeText(RegisterActivity.this,"请输入正确的手机号",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    private boolean checkText(String username,String password,String phone,String code){
        checkPhone(phone);
        if(TextUtils.isEmpty(username)){
            Toast.makeText(RegisterActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
            txtUserName.requestFocus();
            return false;
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
            txtPassword.requestFocus();
            return false;
        }else if(TextUtils.isEmpty(code)){
            Toast.makeText(RegisterActivity.this,"请输入验证码",Toast.LENGTH_LONG).show();
            txtVerify.requestFocus();
            return false;
        }else{
            return true;
        }
    }
}
