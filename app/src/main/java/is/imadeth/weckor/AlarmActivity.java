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
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

import is.example.mschroeder.test.R;

public class AlarmActivity extends AppCompatActivity implements TimePicker.OnTimeChangedListener {

    AlarmManager alarmManager;
    private TimePicker timePicker;
    private ToggleButton toggleButton;
    private PendingIntent pendingIntent;
    private static AlarmActivity inst;

    private AlarmConfiguration alarmConfiguration;

    public static AlarmActivity instance() {
        return inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        timePicker.setOnTimeChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        int hour = prefs.getInt("hour", timePicker.getCurrentHour());
        int minute = prefs.getInt("minute", timePicker.getCurrentMinute());
        boolean active = prefs.getBoolean("active", toggleButton.isChecked());

        alarmConfiguration = new AlarmConfiguration(hour, minute, active);

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        toggleButton.setChecked(active);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        prefs.edit().putBoolean("active", alarmConfiguration.shouldSound());
        prefs.edit().putInt("hour", alarmConfiguration.getHour());
        prefs.edit().putInt("minute", alarmConfiguration.getMinute());
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
            alarmManager.cancel(pendingIntent);
            Log.d("MyActivity", "Alarm Off");
        }
    }

    private void updateIntent() {
        Intent myIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmConfiguration.getHour());
        calendar.set(Calendar.MINUTE, alarmConfiguration.getMinute());

        myIntent.putExtra("startAlarm", alarmConfiguration.shouldSound());
        pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
        updateIntent();
    }
}
