package com.yan.restaurant.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class Table implements Serializable {

    private int id;
    private String num;
    private int status;

}
