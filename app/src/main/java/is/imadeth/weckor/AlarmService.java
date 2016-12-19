package is.imadeth.weckor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class AlarmService extends Service {
    private NotificationManager alarmNotificationManager;

    private MediaPlayer mediaPlayer;
    private boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean shouldStart = intent.getBooleanExtra("startAlarm", false);

        if (mediaPlayer != null) mediaPlayer.stop();

        if (shouldStart) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            sendNotification("Good morning!");
        }

        return START_NOT_STICKY;
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
                .setSmallIcon(R.drawable.icon)
                .build();

        alarmNotificationManager.notify(0, notification);
        Log.d("AlarmService", "Notification sent.");
    }
}
