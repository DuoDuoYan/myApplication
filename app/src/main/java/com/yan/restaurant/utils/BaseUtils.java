package com.yan.restaurant.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.yan.restaurant.bean.CommentBean;
import com.yan.restaurant.bean.FoodBean;
import com.yan.restaurant.bean.Foods;
import com.yan.restaurant.bean.OrderDetail;
import com.yan.restaurant.bean.Orders;
import com.yan.restaurant.bean.Table;
import com.yan.restaurant.bean.TypeBean;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BaseUtils {

	public static String getBalance(String phone){
		String balance = ConnectService.findBalance(phone);
		return balance;
	}

	public static String updateBalance(String phone,String price){
		String result = ConnectService.updateBalance(phone,price);
		return result;
	}

	public static int createOrder(String ids,String phone,int tableId,Long date,String price){
		int orderID = ConnectService.createOrder(ids,phone,tableId,date,price);
		return orderID;
	}

	public static List<Orders> getCustomerOrder(String phone){
		List<Orders> ordersList = ConnectService.getOrdersByPhone(phone,0);
		return ordersList;
	}

	public static List<Orders> getCookerOrder(String phone){

		List<Orders> ordersList = ConnectService.getOrdersByPhone(phone,1);
		return ordersList;
	}

	public static List<OrderDetail> getOrderDetailByOrderNum(String orderNum,String tableNum){

		List<OrderDetail> lists = ConnectService.getOrderDetailByOrderNum(orderNum,tableNum);
		return lists;
	}

	public static List<Orders> getOnOrder(){
		List<Orders> ordersList = ConnectService.getOnOrder();
		return ordersList;
	}
	public static String updateOrderStatus(String phone,String orderNum,String robot,int tag){
		String result = ConnectService.updateOrderStatus(phone,orderNum,robot,tag);
		return result;
	}

	public static List<Table> getTables(){
		List<Table> tableList = ConnectService.findTables();
		return tableList;
	}

	public static List<TypeBean> getTypes() {
        List<String> foodsList = ConnectService.findCatalog();
        ArrayList<TypeBean> tList = new ArrayList<>();
		for (int i = 0; i < foodsList.size(); i++) {
			TypeBean typeBean = new TypeBean();
			typeBean.setName(foodsList.get(i));
			tList.add(typeBean);
		}
		return tList;
	}

	public static List<FoodBean> getDatas(Context context) {
	    List<Foods> lists = ConnectService.findAllFoods();
		ArrayList<FoodBean> fList = new ArrayList<>();

		for (int i = 0; i < lists.size(); i++) {
			FoodBean foodBean = new FoodBean();
			foodBean.setId(lists.get(i).getId());
			foodBean.setInfo(lists.get(i).getInfo());
			foodBean.setName(lists.get(i).getFood());
			foodBean.setPrice(new BigDecimal(lists.get(i).getPrice()));
			foodBean.setSale("月售" + lists.get(i).getSelled());
			foodBean.setType(lists.get(i).getCatalogName());
			//图片
			int resID = context.getResources().getIdentifier("food" + new Random().nextInt(8), "drawable", "com.yan.restaurant");
			foodBean.setIcon(resID);
			fList.add(foodBean);
		}
		return fList;
	}

	public static List<FoodBean> getDetails(List<FoodBean> fList) {
		ArrayList<FoodBean> flist = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			if (fList.size() > i * 10) {
				flist.add(fList.get(i * 10 - 1));
				flist.add(fList.get(i * 10));
			} else {
				break;
			}
		}
		return flist;
	}

	public static List<CommentBean> getComment() {
		ArrayList<CommentBean> cList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			cList.add(new CommentBean());
		}
		return cList;
	}


}
