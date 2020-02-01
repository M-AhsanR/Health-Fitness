package com.giggly.app.Models;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {




    // getting dat time--------------------------
    public static String getCurrentDateTimeComment() {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());


        return formattedDate;
    }

    // getting dat time--------------------------
}
