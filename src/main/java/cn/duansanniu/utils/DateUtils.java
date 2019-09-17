package cn.duansanniu.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author duansanniu
 * @create 2019-09-17 10:55 上午
 */
@Component
public class DateUtils {
    public String getYear(String date){
        try{
            Date d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
            return new SimpleDateFormat("yyyy").format(d);
        }catch(Exception e){
            return "";
        }


    }
}
