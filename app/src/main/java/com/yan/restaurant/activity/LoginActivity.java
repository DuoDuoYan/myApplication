package com.yan.restaurant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.yan.restaurant.R;
import com.yan.restaurant.bean.Foods;
import com.yan.restaurant.utils.ConnectService;

import java.util.List;

import static com.yan.restaurant.utils.ConnectService.findAllFoods;

public class LoginActivity extends AppCompatActivity {

    private ImageView imgPhone,imgWechat,imgQQ,imgWeibo;
    private EditText txtPhone,txtPassword;
    private Button btnLogin;
    private TextView tips,register,validateCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置初始化视图
        initView();
        //事件监听
        setListener();
    }
    /**
     * 创建初始化视图
     */
    private void initView(){
        //获取控件
        btnLogin = findViewById(R.id.loginBtn);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword =  findViewById(R.id.txtPassword);
        imgPhone = findViewById(R.id.imagePhone);
        imgWechat = findViewById(R.id.imageWechat);
        imgQQ = findViewById(R.id.imageQQ);
        imgWeibo = findViewById(R.id.imageWeibo);
        register = findViewById(R.id.txtRegister);
        validateCode = findViewById(R.id.txtValidateCode);

        Drawable user = getResources().getDrawable(R.drawable.user);
        Drawable lock = getResources().getDrawable(R.drawable.lock);

        user.setBounds(0,0,75,75);
        lock.setBounds(0,0,80,80);

        txtPhone.setCompoundDrawables(user,null,null,null);
        txtPassword.setCompoundDrawables(lock,null,null,null);

        //设置tips样式
        tips = findViewById(R.id.txtTips);
        SpannableString span = new SpannableString("登录代表同意Y&L用户协议、隐私政策，并授权使用您的Y&L账号信息（如昵称、头像、收货地址）以便您同意管理");
        span.setSpan(new ForegroundColorSpan(Color.rgb(255,193,7)),6,13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(Color.rgb(255,193,7)),14,18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new URLSpan("url"),6,12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new URLSpan("url"),14,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tips.setText(span);
    }
    /**
     * 设置事件的监听器的方法
     */
    private void setListener(){
        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String phone = txtPhone.getText().toString();
                final String password = txtPassword.getText().toString();
                new Thread(){
                    @Override
                    public void run() {
                        int roleId= 0;
                        try {
                            roleId = ConnectService.loginService(phone,password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(roleId > 0){   //判断id是否大于1，>1查询到数据，可进入下一界面
                            Intent intent;
                            if(roleId == 4){  //如果用户的角色是厨师，则跳转到厨师的界面
                                intent = new Intent(LoginActivity.this, CookerActivity.class);
                                intent.putExtra("phone",phone);
                            }else{             //否则跳转到菜单界面
                                intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra("phone",phone);
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }else if(roleId == 0){
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }else{
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(),"连接后台失败。。。",Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    }
                }.start();
            }
        });
        //其他登录方式
        imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"该功能还未启用，请稍后。。。",Toast.LENGTH_LONG).show();
            }
        });
        imgWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"该功能还未启用，请稍后。。。",Toast.LENGTH_LONG).show();
            }
        });
        imgQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"该功能还未启用，请稍后。。。",Toast.LENGTH_LONG).show();
            }
        });
        imgWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"该功能还未启用，请稍后。。。",Toast.LENGTH_LONG).show();
            }
        });
        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(it);
            }
        });
        //验证码登录
        validateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this,ValidateLogin.class);
                startActivity(it);
            }
        });

    }
}
