package com.yan.restaurant.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class CookerOrderBean implements Serializable {
    private String food;
    private String table;
    private String num;
    private String operate;

}
