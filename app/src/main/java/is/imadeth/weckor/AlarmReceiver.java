package is.imadeth.weckor;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver  {

    @Override
    public void onReceive(final Context context, Intent intent) {

        //this will update the UI with message
        AlarmActivity inst = AlarmActivity.instance();

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        if (ringtone.isPlaying()) {
            startAlarm(ringtone, intent);

            //this will send a notification message
            ComponentName comp = new ComponentName(context.getPackageName(),

            AlarmService.class.getName());
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        } else {
            stopAlarm(ringtone);
        }
    }

    private void startAlarm(Ringtone ringtone, Intent intent) {
        ringtone.play();
    }

    private void stopAlarm(Ringtone ringtone) {
        ringtone.stop();
    }
}
