package is.imadeth.weckor;

public class AlarmConfiguration {

    private int hour;
    private int minute;
    private boolean shouldSound;

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean shouldSound() {
        return shouldSound;
    }

    public AlarmConfiguration(int hour, int minute, boolean shouldSound) {
        this.hour = hour;
        this.minute = minute;
        this.shouldSound = shouldSound;
    }

    @Override
    public String toString() {
        return Integer.toString(hour) + Integer.toString(minute);
    }
}
