package prajwal.lftassignment.com.leapfrogassignment.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by prajwal on 7/16/2017.
 */

public class DateUtils {

    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime());

    }
}
