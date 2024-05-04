package com.sekolah.masterdata.util;

public class CheckUtils {
    public static boolean IsNullOrEmpty(String param){
        return (param == null) || (param.equals(""));
    }
}
