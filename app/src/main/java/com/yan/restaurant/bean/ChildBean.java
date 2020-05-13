package com.yan.restaurant.bean;

import lombok.Data;

@Data
public class ChildBean {

    public static final int CHILD_ITEM = 1;//子布局

    private int type;// 显示类型
    private boolean isExpand;// 是否展开
    private DataBean childBean;

    private String ID;
    private String orderNum;
    private String orderTable;
    private String orderDate;
    private String btn;
    private String food;
    private String num;
}
