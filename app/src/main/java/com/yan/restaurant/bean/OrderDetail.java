package com.yan.restaurant.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderDetail implements Serializable {

    private Integer id;

    private Integer orderNum;

    //餐桌id
    private Integer tableNum;

    //餐桌名
    private String tables;

    private String position;

    private String food;

    private String foodName;

    private Double price;

    private Integer quantity;
}
