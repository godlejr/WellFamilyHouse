package com.demand.well_family.well_family.util;

import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dev-0 on 2017-04-19.
 */

public class CalculateDateUtil {

    private static final Logger logger = LoggerFactory.getLogger(CalculateDateUtil.class);


    public static String calculateDate(String dateTime) {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = transFormat.parse(dateTime);
        } catch (ParseException e) {
            log(e);
        }

        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String message = null;

        if (diffTime < 60) {
            message = diffTime + "초전";
        } else if ((diffTime /= 60) < 60) {
            message = diffTime + "분전";
        } else if ((diffTime /= 60) < 24) {
            message = (diffTime) + "시간전";
        } else if ((diffTime /= 24) < 7) {
            message = (diffTime) + "일전";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yy.M.d aa h:mm");
            message = sdf.format(date);
        }

        return message;
    }


    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
