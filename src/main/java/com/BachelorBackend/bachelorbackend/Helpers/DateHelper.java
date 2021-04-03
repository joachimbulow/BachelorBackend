package com.BachelorBackend.bachelorbackend.Helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public static long convertDateToEpochMillis(String date){
        System.out.println(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try{
            Date dateInstance = formatter.parse(date);
            return dateInstance.getTime();
        }
        catch (Exception e){
            System.out.println("Date parse error");
            return 0;
        }
    }
}
