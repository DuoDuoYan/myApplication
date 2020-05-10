package com.yan.restaurant.utils;

import com.yan.restaurant.bean.FoodBean;
import com.yan.restaurant.bean.Foods;

import java.util.List;

public class UserUtils {


    public static List<Foods> findAllFoods(){
        try {
            List<Foods> list = ConnectService.findAllFoods();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
