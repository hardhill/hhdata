package hh.util.hhdata;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;

public class HUtil {
    public static String getHash256(String string) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(string.getBytes(StandardCharsets.UTF_8));
        return new String(messageDigest.digest());
    }

    /**
     * Возвращает хаш строки в алгоритме MD5
     *
     * @param string - входная строка
     * @return String
     */
    public static String getHashMD5(String string) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.update(string.getBytes(StandardCharsets.UTF_8));
        return new String(messageDigest.digest());
    }

    /**
     * Возвращает год заданной даты
     *
     * @param date
     * @return int - год
     */
    public static int GetYearOfDate(Date date) {
        int year = 1900;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        year = calendar.get(Calendar.YEAR);
        return year;
    }

    /**
     * Первый день года по заданной дате
     *
     * @param date - дата любая
     * @return Date - дата года
     */
    public static Date GetFirstDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, GetYearOfDate(date));
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    /**
     * Возврат Массив дат первого дня года начиная с года указанной даты
     * @param date дата первого года в массиве
     * @return Array dates - массив дат
     */
    public static ArrayList<Date> GetLastYears(Date date) {
        ArrayList<Date> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int year = GetYearOfDate(date);
        int cur_year = GetYearOfDate(new Date());
        for (int i = year; i <= cur_year + 1; i++) {
            cal.set(Calendar.YEAR, i);
            cal.set(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, 1); //
            dates.add(cal.getTime());
        }
        return dates;
    }

    /**
     * Массив последник N дней
     * @param days - количество дней
     * @return Array(Date) массив дней
     */
    public static ArrayList<Date> GetLastDates(int days) {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        ArrayList<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        LocalDateTime ldt = LocalDateTime.now();
        for (int i = days; i >= 0; i--) {
            Date date = new Date(System.currentTimeMillis() - (i * DAY_IN_MS));
            dates.add(date);
        }
        return dates;
    }

    public static ArrayList<Date> GetLastDates(java.sql.Date ldate) {
        LocalDate ldtNow = LocalDate.now();
        LocalDate farDate = ldate.toLocalDate();
        ArrayList<Date> dates = new ArrayList<>();
        while (!ldtNow.isEqual(farDate)) {
            dates.add(asDate(farDate.atStartOfDay()));
            farDate = farDate.plusDays(1);
        }
        return dates;
    }

    /**
     * Возвращает массив дат от и до включительно
     *
     * @param fromDate - начальная дата
     * @param dateTo   - конечная дата
     * @return
     */
    public static ArrayList<Date> GetDates(Date fromDate, Date dateTo) {
        ArrayList<Date> dates = new ArrayList<>();
        while (fromDate.compareTo(dateTo) <= 0) {
            dates.add(fromDate);
            fromDate = addDays(fromDate, 1);
        }
        return dates;
    }

    /**
     * @param curDate
     * @return
     */
    public static ArrayList<Date> GetTimes(Date curDate) {
        ArrayList<Date> times = new ArrayList<>();
        Date startofDay = atStartOfDay(curDate);
        Date endofDay = atEndOfDay(curDate);
        while (startofDay.compareTo(endofDay) < 0) {
            startofDay = new Date(startofDay.getTime() + 60 * 60 * 1000);
            times.add(startofDay);
        }

        return times;
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    public static LocalDate asLocalDate(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    public static LocalDateTime asLocalDateTime(String datetime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDatetime = LocalDateTime.parse(datetime, formatter);
        return localDatetime;
    }

    public static LocalDateTime asLocalDateTime(java.sql.Date datetime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String text = df.format(datetime);
        LocalDateTime localDatetime = LocalDateTime.parse(text + " 00:00:00", formatter);
        return localDatetime;
    }

    public static String getDatebyFormat(Date date, String format) {
        Format formater = new SimpleDateFormat(format);
        String otvet = formater.format(date);
        return otvet;
    }

    public static String getStringDatebyFormat(String date, String format) {
        Format formater = new SimpleDateFormat(format);
        Date d = getDatebyFormat(date, "yyyy-MM-dd");
        String otvet = formater.format(d);
        ;
        return otvet;
    }

    public static String getStringDatebyFormat(LocalDateTime date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String otvet = date.format(formatter);
        return otvet;
    }

    private static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date minusDays(int days) {
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        return new Date(System.currentTimeMillis() - (days * DAY_IN_MS));
    }

    public static Date ToDay() {
        return asDate(LocalDate.now().atTime(12, 0));
    }

    //возвращает дату по строковому формату
    public static Date getDatebyFormat(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date otvet = new Date();

        try {
            otvet = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return otvet;
    }

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String[] StringListtoArray(List<String> arr) {
        String[] array = arr.toArray(new String[0]);
        return array;
    }

    /**
     * создание "соли" из случайных символов
     *
     * @param nsimb количество символов
     */
    public static String getSalt(int nsimb) {
        String s = "";
        // "0123456789" + "ABCDE...Z"
        String validCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        char[] array = validCharacters.toCharArray();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            arrayList.add(i, String.valueOf(array[i]));
        }
        Collections.shuffle(arrayList);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nsimb; i++) {
            sb.append(arrayList.get(i));
        }
        return sb.toString();
    }

    public static Date atStartOfWeek(Date dateFrom) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFrom); // Устанавливаем текущее время
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return calendar.getTime();
    }

    public static Date atEndOfWeek(Date dateTo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTo); // Устанавливаем текущее время
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    public static int GetWeekNumber(LocalDate date) {
        int otvet = 0;
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        otvet = date.get(weekFields.weekOfWeekBasedYear());
        return otvet;
    }
}
