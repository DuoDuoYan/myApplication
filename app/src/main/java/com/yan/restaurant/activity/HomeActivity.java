package com.yan.restaurant.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewanimator.ViewAnimator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.yan.restaurant.R;
import com.yan.restaurant.Views.AddWidget;
import com.yan.restaurant.Views.ShopCarView;
import com.yan.restaurant.adapters.CarAdapter;
import com.yan.restaurant.bean.FoodBean;
import com.yan.restaurant.behaviors.AppBarBehavior;
import com.yan.restaurant.fragments.FirstFragment;
import com.yan.restaurant.fragments.SecondFragment;
import com.yan.restaurant.utils.ViewUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeActivity extends BaseActivity implements AddWidget.OnAddClick {

    public static final String CAR_ACTION = "handleCar";
    public static final String CLEARCAR_ACTION = "clearCar";
    private CoordinatorLayout rootview;
    public BottomSheetBehavior behavior;
    public View scroll_container;
    private FirstFragment firstFragment;
    public static CarAdapter carAdapter;
    private ShopCarView shopCarView;
    private TextView car_limit;
    private TextView tv_amount;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //请求网络操作
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initViews();
        IntentFilter intentFilter = new IntentFilter(CAR_ACTION);
        intentFilter.addAction(CLEARCAR_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    //加入购物车或清空购物车
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case CAR_ACTION:
                    FoodBean foodBean = (FoodBean) intent.getSerializableExtra("foodbean");
                    FoodBean fb = foodBean;
                    int p = intent.getIntExtra("position", -1);
                    if (p >= 0 && p < firstFragment.getFoodAdapter().getItemCount()) {
                        fb = firstFragment.getFoodAdapter().getItem(p);
                        fb.setSelectCount(foodBean.getSelectCount());
                        firstFragment.getFoodAdapter().setData(p, fb);
                    } else {
                        for (int i = 0; i < firstFragment.getFoodAdapter().getItemCount(); i++) {
                            fb = firstFragment.getFoodAdapter().getItem(i);
                            if (fb.getId() == foodBean.getId()) {
                                fb.setSelectCount(foodBean.getSelectCount());
                                firstFragment.getFoodAdapter().setData(i, fb);
                                break;
                            }
                        }
                    }
                    dealCar(fb);
                    break;
                case CLEARCAR_ACTION:
                    clearCar();
                    break;
            }
            if (CAR_ACTION.equals(intent.getAction())) {

            }
        }
    };

    private void initViews() {
        rootview = (CoordinatorLayout) findViewById(R.id.rootview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViewpager();
        initShopCar();
        //总金额
        tv_amount = findViewById(R.id.tv_amount);
    }

    private void initShopCar() {


        Intent it = getIntent();
        final String phone = it.getStringExtra("phone");

        behavior = BottomSheetBehavior.from(findViewById(R.id.car_container));
        shopCarView = (ShopCarView) findViewById(R.id.car_mainfl);
        View blackView = findViewById(R.id.blackview);
        shopCarView.setBehavior(behavior, blackView);
        RecyclerView carRecView = (RecyclerView) findViewById(R.id.car_recyclerview);
//		carRecView.setNestedScrollingEnabled(false);
        carRecView.setLayoutManager(new LinearLayoutManager(mContext));
        ((DefaultItemAnimator) carRecView.getItemAnimator()).setSupportsChangeAnimations(false);
        carAdapter = new CarAdapter(new ArrayList<FoodBean>(), this);
        carAdapter.bindToRecyclerView(carRecView);

        car_limit = findViewById(R.id.car_limit);
        car_limit.setEnabled(false);
        car_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<FoodBean> flist = carAdapter.getData();
                Log.v("HomeActivity","datas:"+flist.size());
                Log.v("HomeActivity","phone:"+phone);
                String price = (String) tv_amount.getText();
                Intent intent = new Intent(HomeActivity.this, PayActivity.class);
                intent.putExtra("price",price);
                intent.putExtra("phone",phone);
                intent.putExtra("data", (Serializable) flist);
                startActivity(intent);

            }
        });
    }



    private void initViewpager() {
        //商品列表上的tab导航
        scroll_container = findViewById(R.id.scroll_container);
        ScrollIndicatorView mSv = (ScrollIndicatorView) findViewById(R.id.indicator);
        ColorBar colorBar = new ColorBar(mContext, ContextCompat.getColor(mContext, R.color.dicator_line_blue), 6,
                ScrollBar.Gravity.BOTTOM);
        colorBar.setWidth(ViewUtils.dip2px(mContext, 30));
        mSv.setScrollBar(colorBar);
        mSv.setSplitAuto(false);
        mSv.setOnTransitionListener(new OnTransitionTextListener().setColor(ContextCompat.getColor(mContext, R.color.dicator_line_blue),
                ContextCompat.getColor(mContext, R.color.black)));
        //商品列表
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //tab与viewPAge绑定
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(mSv, mViewPager);
        firstFragment = new FirstFragment();
        ViewpagerAdapter myAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        indicatorViewPager.setAdapter(myAdapter);
    }

    private class ViewpagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private LayoutInflater inflater;
        private int padding;
        private String[] tabs = new String[]{"商品", "评价"};

        ViewpagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(mContext);
            padding = ViewUtils.dip2px(mContext, 20);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            convertView = inflater.inflate(R.layout.item_textview, container, false);
            TextView textView = (TextView) convertView;
            textView.setText(tabs[position]); //名称
            textView.setPadding(padding, 0, padding, 0);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            switch (position) {
                case 0:
                    return firstFragment;
            }
            return new SecondFragment();
        }
    }

    @Override
    public void onAddClick(View view, FoodBean fb) {
        dealCar(fb);
        ViewUtils.addTvAnim(view, shopCarView.carLoc, mContext, rootview);
    }

    public void onSettle(View view,FoodBean fd){

    }

    @Override
    public void onSubClick(FoodBean fb) {
        dealCar(fb);
    }

    private void dealCar(FoodBean foodBean) {
        HashMap<String, Long> typeSelect = new HashMap<>();//更新左侧类别badge用
        BigDecimal amount = new BigDecimal(0.0);
        int total = 0;
        boolean hasFood = false;
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            firstFragment.getFoodAdapter().notifyDataSetChanged();
        }
        List<FoodBean> flist = carAdapter.getData();
        int p = -1;
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            if (fb.getId() == foodBean.getId()) {
                fb = foodBean;
                hasFood = true;
                if (foodBean.getSelectCount() == 0) {
                    p = i;
                } else {
                    carAdapter.setData(i, foodBean);
                }
            }
            total += fb.getSelectCount();
            if (typeSelect.containsKey(fb.getType())) {
                typeSelect.put(fb.getType(), typeSelect.get(fb.getType()) + fb.getSelectCount());
            } else {
                typeSelect.put(fb.getType(), fb.getSelectCount());
            }
            amount = amount.add(fb.getPrice().multiply(BigDecimal.valueOf(fb.getSelectCount())));
        }
        if (p >= 0) {
            carAdapter.remove(p);
        } else if (!hasFood && foodBean.getSelectCount() > 0) {
            carAdapter.addData(foodBean);
            if (typeSelect.containsKey(foodBean.getType())) {
                typeSelect.put(foodBean.getType(), typeSelect.get(foodBean.getType()) + foodBean.getSelectCount());
            } else {
                typeSelect.put(foodBean.getType(), foodBean.getSelectCount());
            }
            amount = amount.add(foodBean.getPrice().multiply(BigDecimal.valueOf(foodBean.getSelectCount())));
            total += foodBean.getSelectCount();
        }
        shopCarView.showBadge(total);
        firstFragment.getTypeAdapter().updateBadge(typeSelect);
        shopCarView.updateAmount(amount);

    }

    public void expendCut(View view) {
        float cty = scroll_container.getTranslationY();
        if (!ViewUtils.isFastClick()) {
            ViewAnimator.animate(scroll_container)
                    .translationY(cty, cty == 0 ? AppBarBehavior.cutExpHeight : 0)
                    .decelerate()
                    .duration(100)
                    .start();
        }
    }

    public void clearCar(View view) {
        ViewUtils.showClearCar(mContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearCar();
            }
        });
    }

    private void clearCar() {
        List<FoodBean> flist = carAdapter.getData();
        for (int i = 0; i < flist.size(); i++) {
            FoodBean fb = flist.get(i);
            fb.setSelectCount(0);
        }
        carAdapter.setNewData(new ArrayList<FoodBean>());
        firstFragment.getFoodAdapter().notifyDataSetChanged();
        shopCarView.showBadge(0);
        firstFragment.getTypeAdapter().updateBadge(new HashMap<String, Long>());
        shopCarView.updateAmount(new BigDecimal(0.0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        System.exit(0);
    }

    public void toShopDetail(View view) {
        firstFragment.getFoodAdapter().notifyItemChanged(0);
//        ShopInfoContainer shopInfoContainer = (ShopInfoContainer) findViewById(R.id.shopcontainer);
//        if (android.os.Build.VERSION.SDK_INT > 20) {
//            startActivity(new Intent(mContext, ShopDetailActivity.class), ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, shopInfoContainer.iv_shop, "transitionShopImg").toBundle());
//        } else {
//            startActivity(new Intent(mContext, ShopDetailActivity.class));
//        }

    }
}
