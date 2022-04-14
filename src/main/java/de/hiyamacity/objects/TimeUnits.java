package de.hiyamacity.objects;

public enum TimeUnits {

    YEARS("y"),
    MONTHS("m"),
    WEEKS("w"),
    DAYS("d"),
    HOURS("h"),
    MINUTES("min"),
    SECONDS("s");

    private final String unit;

    TimeUnits(String unit) {
        this.unit = unit;
    }

    public static TimeUnits fromString(String text) {
        for (TimeUnits timeUnit : TimeUnits.values()) {
            if (timeUnit.getValue().equalsIgnoreCase(text)) {
                return timeUnit;
            }
        }
        return null;
    }

    public String getValue() {
        return unit;
    }
}
