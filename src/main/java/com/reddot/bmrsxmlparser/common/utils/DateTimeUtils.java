package com.reddot.bmrsxmlparser.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateTimeUtils {

    public static final String APP_DATE_FORMAT = "dd-MMM-yyyy";
    public static final String ADI_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String TRANSACTION_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ON_BEHALF_DATE_FORMAT = "yyyy/MM/dd";


    public static String formatDate(Date date, String dateFormat) {
        return (date == null || StringUtils.isBlank(dateFormat))
                ? ""
                : new SimpleDateFormat(dateFormat).format(date);
    }

    public static String formatDate(Date date) {
        return formatDate(date, ADI_DATE_FORMAT);
    }

    public static String convertToString(Date date, String dateFormat) {
        return formatDate(date, dateFormat);
    }

    public static String getCurrentDateString(String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(new Date());
    }

    public static Date convertToDate(String dateStr, String dateFormat) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(dateFormat))
            return null;
        try {
            return new SimpleDateFormat(dateFormat).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date addHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    public static Date addMinutes(Date date, int minuets) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minuets);

        return calendar.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    public static Date getFirstDayOfMonth() {
        return Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    public static long getDateDifference(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillis = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public static long getDateDifferenceInDays(Date date1, Date date2) {
        return getDateDifference(date1, date2, TimeUnit.DAYS);
    }

    public static Date expireAtHour(int hour) {
        return addHour(new Date(), hour);
    }

    public static int convertToMilli(int minute, int calenderFlag) {
        if (Calendar.HOUR == calenderFlag) return 1000 * 60 * 60 * minute;
        if (Calendar.MINUTE == calenderFlag) return 1000 * 60 * minute;
        return 0;
    }

    public static long millisecondsToMinute(long milliseconds) {
        return TimeUnit.MILLISECONDS.toMinutes(milliseconds);
    }

    public static long millisecondsToSeconds(long milliseconds) {
        return TimeUnit.MILLISECONDS.toSeconds(milliseconds);
    }

    public static String convertMillisecondsToMinute(long milliseconds) {
        long minutes = millisecondsToMinute(milliseconds);
        long seconds = millisecondsToSeconds(milliseconds % 60);
        return minutes + "m " + seconds + "s";
    }

    public static long minuteToMillis(Integer minute) {
        return TimeUnit.MINUTES.toMillis(minute);
    }

    public static boolean validateStringDate(String date) {
        Pattern pattern = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");
        Matcher matcher = pattern.matcher(date);
        return !matcher.matches();
    }

    public static long getCurrentTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    public static Date convertToDateWithoutTime(Date dateToConvert) {
        if (dateToConvert == null)
            return null;

        String formattedDate = formatDate(dateToConvert, APP_DATE_FORMAT);
        return convertToDate(formattedDate, APP_DATE_FORMAT);
    }

    public static Date convertFirstDayOfTheMonth(final Date date) {
        if (date == null)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.MINUTE, 0);
        calendar.add(Calendar.SECOND, 0);
        calendar.add(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public static String normalizeDateFormat(String inputDate) {
        if (StringUtils.isBlank(inputDate)) {
            return null;
        }
        List<String> possibleFormats = Arrays.asList(ON_BEHALF_DATE_FORMAT, DEFAULT_DATE_FORMAT);

        for (String format : possibleFormats) {
            try {
                Date date = new SimpleDateFormat(format).parse(inputDate);
                return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
            } catch (ParseException ignored) {}
        }
        return inputDate;
    }

    public static String formatLocalDate(LocalDate date, String dateFormat) {
        return (date == null || StringUtils.isBlank(dateFormat))
                ? ""
                : date.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String formatLocalDate(LocalDate date) {
        return formatLocalDate(date, DEFAULT_DATE_FORMAT);
    }

    public static String convertToString(LocalDate date, String dateFormat) {
        return formatLocalDate(date, dateFormat);
    }

    // Convert LocalDateTime to String with specific format
    public static String formatLocalDateTime(LocalDateTime dateTime, String dateFormat) {
        return (dateTime == null || StringUtils.isBlank(dateFormat))
                ? ""
                : dateTime.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return formatLocalDateTime(dateTime, ADI_DATE_FORMAT);
    }

    public static String convertToString(LocalDateTime dateTime, String dateFormat) {
        return formatLocalDateTime(dateTime, dateFormat);
    }

    // Convert String to LocalDate with specific format
    public static LocalDate convertToLocalDate(String dateStr, String dateFormat) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(dateFormat))
            return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Convert String to LocalDateTime with specific format
    public static LocalDateTime convertToLocalDateTime(String dateStr, String dateFormat) {
        if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(dateFormat))
            return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            return LocalDateTime.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Convert LocalDate to Date
    public static Date convertToDateFromLocalDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    // Convert LocalDateTime to Date
    public static Date convertToDateFromLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // Convert Date to LocalDate
    public static LocalDate convertToLocalDateFromDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // Convert Date to LocalDateTime
    public static LocalDateTime convertToLocalDateTimeFromDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // Get Current LocalDate
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    // Get Current LocalDateTime
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    // Add days to LocalDate
    public static LocalDate addDays(LocalDate date, int days) {
        return (date == null) ? null : date.plusDays(days);
    }

    // Add hours to LocalDateTime
    public static LocalDateTime addHours(LocalDateTime dateTime, int hours) {
        return (dateTime == null) ? null : dateTime.plusHours(hours);
    }

    // Add minutes to LocalDateTime
    public static LocalDateTime addMinutes(LocalDateTime dateTime, int minutes) {
        return (dateTime == null) ? null : dateTime.plusMinutes(minutes);
    }

    // Get the first day of the current month as LocalDate
    public static LocalDate getLocalDateFirstDayOfMonth() {
        return LocalDate.now().withDayOfMonth(1);
    }

    // Get the first day of the month as LocalDateTime
    public static LocalDateTime getFirstDayOfMonthAsLocalDateTime() {
        return LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    // Date difference in days for LocalDate
    public static long getDateDifferenceInDays(LocalDate date1, LocalDate date2) {
        return (date1 != null && date2 != null) ? java.time.temporal.ChronoUnit.DAYS.between(date1, date2) : 0;
    }

    // Date difference in hours for LocalDateTime
    public static long getDateDifferenceInHours(LocalDateTime date1, LocalDateTime date2) {
        return (date1 != null && date2 != null) ? java.time.temporal.ChronoUnit.HOURS.between(date1, date2) : 0;
    }

    // Add minutes to current date-time
    public static LocalDateTime addMinutesToCurrentTime(int minutes) {
        return LocalDateTime.now().plusMinutes(minutes);
    }

    // Normalize LocalDate format
    public static String normalizeLocalDateFormat(String inputDate) {
        if (StringUtils.isBlank(inputDate)) {
            return null;
        }
        List<String> possibleFormats = Arrays.asList(ON_BEHALF_DATE_FORMAT, DEFAULT_DATE_FORMAT);

        for (String format : possibleFormats) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                LocalDate date = LocalDate.parse(inputDate, formatter);
                return date.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
            } catch (DateTimeParseException ignored) {}
        }
        return inputDate;
    }

}

