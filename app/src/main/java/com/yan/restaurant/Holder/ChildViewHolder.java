package com.yan.restaurant.Holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yan.restaurant.R;
import com.yan.restaurant.bean.DataBean;

public class ChildViewHolder extends BaseViewHolder {

    private Context mContext;
    private View view;
    private TextView food;
    private TextView num;

    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final DataBean dataBean, final int pos) {

        food = (TextView) view.findViewById(R.id.food);
        num = (TextView) view.findViewById(R.id.num);
        food.setText(dataBean.getFood());
        num.setText(dataBean.getNum());

    }
}