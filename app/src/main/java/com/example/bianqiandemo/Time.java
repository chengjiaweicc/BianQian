package com.example.bianqiandemo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
    public String getTimer() {
        SimpleDateFormat data=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//设置日期格式
        return(data.format(new Date()));//new Date()为获取当前系统时间
    }
}
