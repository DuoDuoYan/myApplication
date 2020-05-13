package com.yan.restaurant.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mob.wrappers.PaySDKWrapper;
import com.yan.restaurant.bean.Foods;
import com.yan.restaurant.bean.OrderDetail;
import com.yan.restaurant.bean.Orders;
import com.yan.restaurant.bean.Table;
import com.yan.restaurant.bean.User;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ConnectService {

    private static final String urlPath = "http://192.168.31.30:8080/";
    //用户
    private static final String loginPath = urlPath + "user/login";
    private static final String registerPath = urlPath + "user/register";
    private static final String findByPhone = urlPath + "user/findByPhone";
    private static final String updatePath = urlPath + "user/update";
    private static final String tables = urlPath + "user/findTables";
    private static final String MenuPath = urlPath + "user/findAllFoods";
    private static final String catalogPath = urlPath + "user/catalogs";
    private static final String findBalance = urlPath + "user/findBalance";
    private static final String updateBalance = urlPath + "user/updateBalance";
    private static final String createOrder = urlPath + "user/createOrder";
    private static final String getOrderByCustomer = urlPath + "user/getOrderByCustomer";                       //顾客订单
    private static final String getOnOrders = urlPath + "user/getOnOrders";                 //代接订单
    private static final String getOrdersByCooker = urlPath + "user/getOrdersByCooker";     //厨师所有已接订单
    private static final String getOrderDetailByOrderNum = urlPath + "user/getOrderDetailByOrderNum";     //厨师已接订单
    private static final String updateOrderStatus = urlPath + "user/updateOrderStatus";     //订单状态

    public static String handleData(JSONObject jsonObject, URL url) throws Exception{
        int code;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //开启连接
        conn.setConnectTimeout(5000);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("ser-Agent","Fiddler");
        conn.setRequestProperty("Content-Type","application/json");
        if(jsonObject != null){
            //json串转String类型
            String content = String.valueOf(jsonObject);
            Log.v("handleData",content);
            //写输出流，将参数写入流里
            OutputStream os = conn.getOutputStream();
            os.write(content.getBytes());   //字符串写进二进流
            os.close();
            code = conn.getResponseCode();  //与后台交互成功返回200
            Log.v("handleData","code:"+String.valueOf(code));
            if(code == 200) {
                InputStream inputStream = conn.getInputStream();    //读取返回的json数据
                String str = NetUtils.readString(inputStream);     //将流转成String类型
                return str;
            }else{
                return null;
            }
        }
        code = conn.getResponseCode();  //与后台交互成功返回200
        Log.v("handleData","code:"+String.valueOf(code));
        if(code == 200) {
            InputStream inputStream = conn.getInputStream();    //读取返回的json数据
            String str = NetUtils.readString(inputStream);     //将流转成String类型
            return str;
        }else{
            return null;
        }
    }

    public static int loginService(String phone,String password)throws Exception{
        URL url = new URL(loginPath);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone",phone);
        jsonObject.put("password",password);
        String json = handleData(jsonObject,url);
        if(json != null){
            User u = new Gson().fromJson(json, User.class);  //将string类型转换成对应的对象类型
            if(u == null){
                return 0;  //登录失败
            }
            int roleId = u.getRoleId().intValue();
            return roleId;
        }else{
            return -1;  //请求失败
        }
    }

    public static String register(String username,String password,String phone)throws Exception{
        URL url = new URL(registerPath);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        jsonObject.put("phone",phone);
        String json = handleData(jsonObject,url);
        return json;
    }

    public static Boolean exist(String phone)throws Exception{
        URL url = new URL(findByPhone);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone",phone);
        String json = handleData(jsonObject,url);
        if(json.length()>0){
            return true;
        }else{
            return false;
        }
    }
    public static String findBalance(String phone){
        try{
            URL url = new URL(findBalance);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone",phone);
            String json = handleData(jsonObject,url);
            return json;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String updateBalance(String phone,String price){
        try{
            URL url = new URL(updateBalance);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone",phone);
            jsonObject.put("balance",price.substring(1));
            String json = handleData(jsonObject,url);
            return json;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Table> findTables() {
        try {
            URL url = new URL(tables);
            String str = handleData(null, url);
            if (str != null) {
                Log.v("Connect", "msg:" + str);
                String result = str.substring(1, str.length() - 1);
                String[] arr = result.split("\\},");
                List<Table> tableList = new ArrayList<>();
                for (int i = 0; i < arr.length - 1; i++) {
                    String table = arr[i] + "}";
                    Table tables = JSONObject.parseObject(table, Table.class);
                    tableList.add(tables);
                }
                Table table = JSONObject.parseObject(arr[arr.length - 1], Table.class);
                tableList.add(table);
                return tableList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Foods> findAllFoods(){
        try {
            URL url = new URL(MenuPath);
            String str = handleData(null,url);
            if(str != null) {
                Log.v("Connect","msg:"+str);
                JsonArray jsonArray = new JsonParser().parse(str).getAsJsonArray();
                Gson gson = new Gson();
                List<Foods> foodsList = new ArrayList<>();
                for(int i=0;i<jsonArray.size();i++){
                    Foods foods = gson.fromJson(jsonArray.get(i),Foods.class);
                    foodsList.add(foods);
                }
                return foodsList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> findCatalog(){
        try {
            URL url = new URL(catalogPath);
            String str = handleData(null,url);
            if(str != null){
                List<String> list = JSONObject.parseArray(str,String.class);
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int createOrder(String ids,String phone,int table,Long date,String price){
        try{
            URL url = new URL(createOrder);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ids",ids);
            jsonObject.put("customer",phone);
            jsonObject.put("tableNum",String.valueOf(table));
            jsonObject.put("orderDate",String.valueOf(date));
            jsonObject.put("totalPrice",price);
            String json = handleData(jsonObject,url);
            return Integer.valueOf(json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Orders> getOrdersByPhone(String phone,int tag){
        try{
            URL url = null;
            JSONObject jsonObject = new JSONObject();
            if(tag == 0){
                url = new URL(getOrderByCustomer);
                jsonObject.put("customer",phone);
            }else if(tag == 1){
                url = new URL(getOrdersByCooker);
                jsonObject.put("cooker",phone);
            }
            String json = handleData(jsonObject,url);
            if(json != null) {
                Log.v("Connect","msg:"+json);
                JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
                Gson gson = new Gson();
                List<Orders> ordersList = new ArrayList<>();
                for(int i=0;i<jsonArray.size();i++){
                    Orders order = gson.fromJson(jsonArray.get(i),Orders.class);
                    ordersList.add(order);
                }
                return ordersList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<OrderDetail> getOrderDetailByOrderNum(String orderNum,String tableNum){
        try{
            URL url = new URL(getOrderDetailByOrderNum);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("orderNum",orderNum);
            jsonObject.put("tables",tableNum);
            String json = handleData(jsonObject,url);
            if(json != null) {
                Log.v("Connect","msg:"+json);
                JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
                Gson gson = new Gson();
                List<OrderDetail> lists = new ArrayList<>();
                for(int i=0;i<jsonArray.size();i++){
                    OrderDetail orderDetail = gson.fromJson(jsonArray.get(i),OrderDetail.class);
                    lists.add(orderDetail);
                }
                return lists;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Orders> getOnOrder(){
        try{
            URL url = new URL(getOnOrders);
            JSONObject jsonObject = new JSONObject();
            String json = handleData(jsonObject,url);
            if(json != null) {
                Log.v("Connect","msg:"+json);
                List<Orders> ordersList = new ArrayList<>();
                JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
                Gson gson = new Gson();
                for(int i=0;i<jsonArray.size();i++){
                    Orders order = gson.fromJson(jsonArray.get(i),Orders.class);
                    Log.v("Connect","getOrder:"+order);
                    ordersList.add(order);
                }
                return ordersList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String updateOrderStatus(String phone,String orderNum,String robot,int tag){
        try{
            URL url = new URL(updateOrderStatus);
            JSONObject jsonObject = new JSONObject();
            if(tag == 1){
                jsonObject.put("cooker",phone);
                jsonObject.put("id",orderNum);
            }else if(tag == 2){
                jsonObject.put("robotNum",robot);
                jsonObject.put("id",orderNum);
            }else{
                jsonObject.put("id",orderNum);
            }
            jsonObject.put("tag",tag);
            String json = handleData(jsonObject,url);
            return json;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
