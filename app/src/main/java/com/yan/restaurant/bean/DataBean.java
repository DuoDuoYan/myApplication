package com.yan.restaurant.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DataBean {

    public static final int PARENT_ITEM = 0;//父布局
    public static final int CHILD_ITEM = 1;//子布局

    private int type;// 显示类型
    private boolean isExpand;// 是否展开

    private String ID;
    private String orderNum;
    private String orderTable;
    private String orderDate;
    private String btn;
    private List<DataBean> child = new ArrayList<>();
    private String food;
    private String num;


}
