package com.saike.grape.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saike.grape.base.cons.Constants;
/**
 * 时间戳Datetime工具类
 *
 */
public class DatetimeUtils {

    private static final Logger logger = LoggerFactory.getLogger(DatetimeUtils.class);

    private static long ONE_DAY_IN_MILISECONDS = 24 * 60 * 60 * 1000;

    /**
     * 获取一周之后于的时间
     */
    public static Timestamp afterOneWeek(Timestamp datetime) {
        if (datetime == null) {
            throw new IllegalArgumentException("Argument datetime is null!!");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(datetime);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        return new Timestamp(c.getTime().getTime());
    }

    /**
     * 获取两个时间之间的间隔天数，不传递值代表当前时间
     */
    public static long daysBetween(Timestamp datetime1, Timestamp datetime2) {
        Timestamp currentTimestamp = currentTimestamp();
        datetime1 = (datetime1 == null) ? currentTimestamp : datetime1;
        datetime2 = (datetime2 == null) ? currentTimestamp : datetime2;
        long delta = Math.abs(datetime2.getTime() - datetime1.getTime());
        return (long) Math.ceil(delta * 1.0 / ONE_DAY_IN_MILISECONDS);
    }

    /**
     * 获取当前时间戳
     */
    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 按照默认格式，格式化字符串为日期时间格式
     */
    public static Timestamp parseTimestamp(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATETIME_PATTERN);
        try {
            return new Timestamp(sdf.parse(timestamp).getTime());
        } catch (Exception ex) {
            logger.error("\"" + timestamp + "\" is invalid,"
                    + " it should be in pattern " + " \""
                    + Constants.DEFAULT_DATETIME_PATTERN + "\"", ex);
        }
        return null;
    }

    /**
     * 按照默认格式，格式化时间为字符串格式
     */
    public static String formatTimestamp(Timestamp timestamp) {
        return timestamp == null ? "" : new SimpleDateFormat(
                Constants.DEFAULT_DATETIME_PATTERN).format(timestamp);
    }

}
