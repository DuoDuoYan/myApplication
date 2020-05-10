package com.yan.restaurant.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

public class TimeCountUtil extends CountDownTimer {

    private Button button;

    public TimeCountUtil(Button button,long millisInFuture,long countDownInterval){
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //按钮不可用
        button.setClickable(false);
        button.setBackgroundColor(Color.parseColor("#dddddd"));
        String showText = millisUntilFinished / 1000 + "秒后可重新发送";
        button.setText(showText);
    }

    @Override
    public void onFinish() {
        //按钮设置可用
        button.setEnabled(true);
        button.setText("重新获取验证码");
    }
}
