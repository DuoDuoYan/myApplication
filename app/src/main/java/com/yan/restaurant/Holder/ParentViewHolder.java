package com.yan.restaurant.Holder;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseViewHolder;
import com.yan.restaurant.listener.ItemClickListener;
import com.yan.restaurant.R;
import com.yan.restaurant.bean.DataBean;
import com.yan.restaurant.listener.OnItemClickListener;

public class ParentViewHolder extends BaseViewHolder implements View.OnClickListener {
    private Context mContext;
    private View view;
    private RelativeLayout containerLayout;
    private TextView orderNum,orderTable,orderDate,btn;
    private ImageView expand,robot;
    private View parentDashedView;
    private OnItemClickListener onItemClickListener;

    public ParentViewHolder(Context context, View itemView,OnItemClickListener onItemClickListener) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
        this.onItemClickListener = onItemClickListener;
    }

    public void bindView(final DataBean dataBean, final int pos, final ItemClickListener listener){

        containerLayout = (RelativeLayout) view.findViewById(R.id.container);
        orderNum = (TextView) view.findViewById(R.id.c_order);
        orderTable = (TextView) view.findViewById(R.id.c_table);
        orderDate = (TextView) view.findViewById(R.id.c_date);
        btn = (TextView) view.findViewById(R.id.c_btn);
        expand = (ImageView) view.findViewById(R.id.expend);
        parentDashedView = view.findViewById(R.id.parent_dashed_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) expand
                .getLayoutParams();
        expand.setLayoutParams(params);
        orderNum.setText(dataBean.getOrderNum());
        orderTable.setText(dataBean.getOrderTable());
        orderDate.setText(dataBean.getOrderDate());
        btn.setText(dataBean.getBtn());

        if (dataBean.isExpand()) {
            expand.setRotation(90);
            parentDashedView.setVisibility(View.INVISIBLE);
        } else {
            expand.setRotation(0);
            parentDashedView.setVisibility(View.VISIBLE);
        }

        //父布局icon OnClick监听
        orderNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    if (dataBean.isExpand()) {
                        listener.onHideChildren(dataBean);
                        parentDashedView.setVisibility(View.VISIBLE);
                        dataBean.setExpand(false);
                        rotationExpandIcon(90, 0);
                    } else {
                        listener.onExpandChildren(dataBean);
                        parentDashedView.setVisibility(View.INVISIBLE);
                        dataBean.setExpand(true);
                        rotationExpandIcon(0, 90);
                    }
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,orderNum.getText().toString()+","+orderTable.getText().toString());
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon(float from, float to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
            valueAnimator.setDuration(500);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    expand.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }

    @Override
    public void onClick(View v) {
        onItemClickListener.onItemClick(v,orderNum.getText().toString());
    }



}
