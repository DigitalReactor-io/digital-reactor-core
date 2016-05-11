package io.digitalreactor.core.gateway.api.dto;

/**
 * Created by MStepachev on 11.05.2016.
 */
public class VisitDto {
    public enum DayType {
        HOLIDAY, WEEKDAY
    }

    private int number;
    private String date;
    private DayType dayType;

    public VisitDto(int number, String date, DayType dayType) {
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
