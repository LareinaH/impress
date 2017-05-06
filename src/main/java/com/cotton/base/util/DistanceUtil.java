package com.cotton.base.util;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class DistanceUtil {

    private static double  DEF_PI =Math.PI;// 3.14159265359
    private static double  DEF_2PI =2 *Math.PI;// 6.28318530712
    private static double  DEF_PI180 =Math.PI/ 180.0;// 0.01745329252
    private static double  DEF_R =6370996.81;// 地球的半径

    /**
     * 通过两点的经纬度计算两点直线距离
     * @param lng1Str  终端经度
     * @param lat1Str  终端纬度
     * @param lng2Str  商家经度
     * @param lat2Str  商家纬度
     * @return 返回的值四舍五入并保留两位小数,单位米
     */
    public static Double getTwopointsDistance(String lng1Str,String lat1Str, String lng2Str,String lat2Str) {
        if ((StringUtils.isBlank(lat1Str)) || (StringUtils.isBlank(lng1Str))
                || (StringUtils.isBlank(lat2Str))
                || (StringUtils.isBlank(lng2Str))){return null;}

        Double lon1 = Double.parseDouble(lng1Str);
        Double lat1 = Double.parseDouble(lat1Str);
        Double lon2 = Double.parseDouble(lng2Str);
        Double lat2 = Double.parseDouble(lat2Str);

        // 角度转换为弧度
        double ew1 =  lon1 * DEF_PI180;
        double ns1 =  lat1 * DEF_PI180;
        double ew2 =  lon2 * DEF_PI180;
        double ns2 =  lat2 * DEF_PI180;

        // 求大圆劣弧与球心所夹的角(弧度)
        double distance = Math.sin(ns1) * Math.sin(ns2)+ Math.cos(ns1)* Math.cos(ns2)* Math.cos(ew1- ew2);
        // 调整到[-1..1]范围内，避免溢出
        if(distance >1.0) distance = 1.0;
        else if(distance <-1.0) distance = -1.0;
        distance = DEF_R *Math.acos(distance);

        //格式化返回值
        BigDecimal bd = new BigDecimal(distance);
        return distance = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
