package com.yan.restaurant.detail;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yan.restaurant.R;
import com.yan.restaurant.activity.BaseActivity;
import com.yan.restaurant.utils.ViewUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by k on 2019/10/30.
 */

public class ShopDetailActivity extends BaseActivity implements View.OnClickListener {

    private SimpleDraweeView iv_shop;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iv_shop = (SimpleDraweeView) findViewById(R.id.iv_shop);
        ViewUtils.getBlurFresco(mContext, (SimpleDraweeView) findViewById(R.id.iv_shop_bg), "res:///" + R.drawable.bg);

        //  ViewUtils.getFrescoController(mContext, iv_shop, "res:///" + R.drawable.icon_shop, 40, 40);
    }

    @Override
    public void onClick(View v) {

    }

}


