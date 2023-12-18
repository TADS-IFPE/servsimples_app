/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    public static long dataAndTimeStringToEpochMillis(String dateTimeString) {
        ServSimplesAppLogger.e(TAG, "dataAndTimeStringToEpochMillis():" + dateTimeString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyyHH:mm");
        try {
            Date date = dateFormat.parse(dateTimeString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String calendarToDateString(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(calendar.getTime());
    }

    public static String getTimeFormatString(int hour, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    public static String timestampToDateString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(timestamp);
    }

    public static String timestampToTimeString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone localTimeZone = TimeZone.getDefault();
        calendar.setTimeZone(localTimeZone);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeFormat.setTimeZone(localTimeZone);
        return timeFormat.format(timestamp);
    }

    public interface TimePickerCallback {
        void onGet(int hour, int min);
    }
}