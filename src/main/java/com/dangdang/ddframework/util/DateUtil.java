package com.dangdang.ddframework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cailianjie on 2016-5-5.
 */
public class DateUtil {

    public static long getDays(Date begin_date, Date end_date) {
        long day = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String sdate = format.format(Calendar.getInstance().getTime());

            if (begin_date == null) {
                begin_date = format.parse(sdate);
            }
            if (end_date == null) {
                end_date = format.parse(sdate);
            }
            day = (end_date.getTime() - begin_date.getTime())
                    / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return -1;
        }
        return day;
    }
}
