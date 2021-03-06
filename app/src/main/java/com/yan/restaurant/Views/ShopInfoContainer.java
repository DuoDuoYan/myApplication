package com.yan.restaurant.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yan.restaurant.R;
import com.yan.restaurant.utils.ViewUtils;

public class ShopInfoContainer extends RelativeLayout {

    public TextView shop_name, shop_sum, shop_type, shop_send;
    private ImageView shop_arrow, iv_pin;
    public SimpleDraweeView iv_shop;

    public ShopInfoContainer(Context context) {
        super(context);
    }

    public ShopInfoContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_shopinfo, this);
        shop_name = findViewById(R.id.tv_shop_name);
        shop_sum = findViewById(R.id.tv_shop_summary);
        shop_type = findViewById(R.id.tv_shop_type);
        shop_send = findViewById(R.id.tv_shop_send);
        ViewUtils.getBlurFresco(context, (SimpleDraweeView) findViewById(R.id.iv_shop_bg), "res:///" + R.drawable.bg);
        iv_shop = findViewById(R.id.iv_shop);
        ViewUtils.getFrescoController(context, iv_shop, "res:///" + R.drawable.logo, 40, 40);
    }

    public void setWgAlpha(float alpha) {
        shop_sum.setAlpha(alpha);
        shop_type.setAlpha(alpha);
        shop_send.setAlpha(alpha);
        iv_shop.setAlpha(alpha);
    }
}
