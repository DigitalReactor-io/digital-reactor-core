package io.digitalreactor.core.gateway.api.dto;

/**
 * Created by MStepachev on 11.05.2016.
 */
public class VisitDto {
    public enum DayType {
        HOLIDAY, WEEKDAY
    }

    private final int number;
    private final String date;
    private final DayType dayType;

    public VisitDto(
           final int number,
           final String date,
           final DayType dayType
    ) {
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
