package is.imadeth.weckor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener {

    AlarmManager alarmManager;
    private TimePicker timePicker;
    private ToggleButton toggleButton;
    private PendingIntent pendingIntent;
    private Button snoozeButton;

    private AlarmConfiguration alarmConfiguration;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        snoozeButton = (Button) findViewById(R.id.snooze);

        timePicker.setOnTimeChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        int hour = prefs.getInt("hour", 1);
        int minute = prefs.getInt("minute", 1);
        boolean active = prefs.getBoolean("active", toggleButton.isChecked());

        alarmConfiguration = new AlarmConfiguration(hour, minute, active);

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setIs24HourView(true);
        toggleButton.setChecked(active);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor prefs = getPreferences(Context.MODE_PRIVATE).edit();
        prefs.putBoolean("active", alarmConfiguration.shouldSound());
        prefs.putInt("hour", alarmConfiguration.getHour());
        prefs.putInt("minute", alarmConfiguration.getMinute());
        prefs.commit();
    }

    private void updateStateFromUI() {
        alarmConfiguration = new AlarmConfiguration(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), toggleButton.isChecked());
    }

    public void onToggleClicked(View view) {
        updateStateFromUI();
        updateIntent();

        if (toggleButton.isChecked()) {
            Log.d("MyActivity", "Alarm On");
        } else {
            Log.d("MyActivity", "Alarm Off");
        }
    }

    public void onSnooze(View view) {
        Intent myIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
        myIntent.putExtra("startAlarm", false);
        sendBroadcast(myIntent);

        if (toggleButton.isChecked()) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 5);

            alarmConfiguration = new AlarmConfiguration(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);

            updateIntent();
        }
    }

    private void updateIntent() {
        Intent myIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmConfiguration.getHour());
        calendar.set(Calendar.MINUTE, alarmConfiguration.getMinute());

        Calendar now = Calendar.getInstance();

        // Check if the alarm date is in the past
        if (calendar.getTimeInMillis() <= now.getTimeInMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        Log.d("alarm", alarmConfiguration.toString());

        myIntent.putExtra("startAlarm", alarmConfiguration.shouldSound());

        if (toggleButton.isChecked()) {
            pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);

            sendBroadcast(myIntent);
        }
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
        updateStateFromUI();
        updateIntent();
    }
}
