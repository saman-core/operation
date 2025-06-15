package io.samancore.operation.util;

import java.util.Calendar;
import java.util.Date;

public class Util {

    private Util() {
    }

    public static Date getEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
