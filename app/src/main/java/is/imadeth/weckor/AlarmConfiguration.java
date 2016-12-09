package is.imadeth.weckor;

/**
 * Created by mschroeder on 09/12/2016.
 */

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
}
