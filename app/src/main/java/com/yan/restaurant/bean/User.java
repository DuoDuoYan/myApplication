package com.yan.restaurant.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private String phone;
    private Integer roleId;
    private Double balance;

}
