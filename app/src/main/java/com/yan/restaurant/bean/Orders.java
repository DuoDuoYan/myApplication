package com.yan.restaurant.bean;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Orders implements Serializable {
    private Integer id;

    private String ids;

    private String tables;

    private String robotNum;

    private String robot;

    private Long orderDate;

    private String date;

    private Double totalPrice;

    private Integer orderStatus;

    private String status;

    private String customer;

    private String cooker;

    private List<OrderDetail> orderDetail = new ArrayList<>();

}
