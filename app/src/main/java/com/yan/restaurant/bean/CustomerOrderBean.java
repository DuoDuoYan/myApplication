package com.yan.restaurant.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class CustomerOrderBean implements Serializable {
    private String orderNum;
    private String table;
    private String totalPrice;
    private String date;
    private String status;
}
