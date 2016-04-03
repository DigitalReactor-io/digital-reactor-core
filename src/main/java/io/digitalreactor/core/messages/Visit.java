package io.digitalreactor.core.messages;

/**
 * Created by ingvard on 03.04.16.
 */
public class Visit {
    public enum DayType {
        HOLIDAY, WEEKDAY
    }

    private int number;
    private String date;
    private DayType dayType;

    public Visit(int number, String date, DayType dayType) {
        this.number = number;
        this.date = date;
        this.dayType = dayType;
    }

    public int getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public DayType getDayType() {
        return dayType;
    }
}
