package com.yan.restaurant.adapters;


import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yan.restaurant.R;
import com.yan.restaurant.Views.AddWidget;
import com.yan.restaurant.bean.FoodBean;

import java.util.List;

public class FoodAdapter extends BaseQuickAdapter<FoodBean, BaseViewHolder> {
    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;
    private List<FoodBean> flist;
    private AddWidget.OnAddClick onAddClick;

    public FoodAdapter(@Nullable List<FoodBean> data, AddWidget.OnAddClick onAddClick) {
        super(R.layout.item_food, data);
        flist = data;
        this.onAddClick = onAddClick;
    }

    @Override
    protected void convert(BaseViewHolder helper, FoodBean item) {
        helper.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_price, item.getStrPrice(mContext))
                .setText(R.id.tv_sale, item.getSale())
                .setText(R.id.tv_summary,item.getInfo())
                .setImageResource(R.id.iv_food, item.getIcon()).addOnClickListener(R.id.addwidget)
                .addOnClickListener(R.id.food_main);

        Log.i("---", item.getName() + "========" + helper.getLayoutPosition());
        AddWidget addWidget = helper.getView(R.id.addwidget);
        addWidget.setData(onAddClick, item);

        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.stick_header, true)
                    .setText(R.id.tv_header, item.getType())
                    .setTag(R.id.food_main, FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(item.getType(), flist.get(helper.getAdapterPosition() - 1).getType())) {
                helper.setVisible(R.id.stick_header, true)
                        .setText(R.id.tv_header, item.getType())
                        .setTag(R.id.food_main, HAS_STICKY_VIEW);
            } else {
                helper.setVisible(R.id.stick_header, false)
                        .setTag(R.id.food_main, NONE_STICKY_VIEW);
            }
        }
        helper.getConvertView().setContentDescription(item.getType());
    }

}
