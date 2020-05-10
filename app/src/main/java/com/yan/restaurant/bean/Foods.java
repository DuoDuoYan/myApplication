package com.yan.restaurant.bean;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;

import com.yan.restaurant.utils.ViewUtils;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class Foods implements Serializable {
    private int id;
    private String food;
    private String info;
    private Double price;
    private Double nowPrice;
    private int quantity;
    private int catalog;
    private String catalogName;
    private int selled;
    private String image;

}
