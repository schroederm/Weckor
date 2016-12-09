package is.imadeth.weckor;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import is.example.mschroeder.test.R;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0,
                new Intent(this, AlarmActivity.class), 0);

        Notification notification = new Notification.Builder(
                this).setContentTitle("Alarm")
                .setAutoCancel(true)
                .setContentText(msg)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        alarmNotificationManager.notify(0, notification);
        Log.d("AlarmService", "Notification sent.");
    }
}
