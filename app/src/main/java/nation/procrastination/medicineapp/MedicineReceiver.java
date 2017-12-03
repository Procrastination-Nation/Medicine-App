package nation.procrastination.medicineapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jakei_000 on 11/28/2017.
 */

public class MedicineReceiver extends BroadcastReceiver {
    private static int MID = 0;
    private DBHelper helper;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("MedicinePrefs", Context.MODE_PRIVATE);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        helper = new DBHelper(context);
        int medID = intent.getIntExtra(context.getString(R.string.timer_med_id), -1);
        if(medID == -1) return;
        int alarmID = intent.getIntExtra(context.getString(R.string.timer_alarm_id), -1);
        String timeString = intent.getStringExtra(context.getString(R.string.timer_alarm_time));
        boolean refill = false;
        MedicineInfo info = helper.getMedicineByID(medID);
        int amt = info.getAmount();
        int newAmount = amt - info.getDosage();
        if(newAmount <= 0) {
            //Refill required
            refill = true;
            newAmount = 0;
        }
        info = new MedicineInfo(
                medID,
                info.getName(),
                newAmount,
                info.getDosage(),
                info.getDays(),
                info.getTimes(),
                info.getAlarmIDs()
        );
        helper.updateMedicine(info);
        String medName = helper.getMedicineByID(medID).getName();
        String notificationMessage = (!refill) ? "It is time to take " + medName : "It is time to take and refill " + medName;
        if(!refill) {
            resetTimer(context, medID, alarmID, timeString);
        }
        else if(preferences.getBoolean("MEDICINE_REFILL", false) && refill) {
            notificationMessage = "It is time to take " + medName + "(AUTOREFILL)";
            info = new MedicineInfo(
                    medID,
                    info.getName(),
                    helper.getMedicineOriginal(medID),
                    info.getDosage(),
                    info.getDays(),
                    info.getTimes(),
                    info.getAlarmIDs()
            );
            resetTimer(context, medID, alarmID, timeString);
            helper.updateMedicine(info);
        }

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        boolean push = preferences.getBoolean("MEDICINE_NOTIFY", true);
        if(push) {
            NotificationCompat.Builder mNotifyBuilder = (android.support.v7.app.NotificationCompat.Builder)
                    new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_action_gear)
                            .setAutoCancel(true)
                            .setContentTitle("Medicine Alert!")
                            .setContentText(notificationMessage)
                            .setSound(alarmSound)
                            .setContentIntent(pendingIntent);
            notificationManager.notify(MID, mNotifyBuilder.build());
            MID++;
        }
    }
    private void resetTimer(Context context, int medID, int alarmID, String time) {
        if(medID == -1 || alarmID == -1 || time.length() < 1) return;
        Intent alarmIntent = new Intent(context, MedicineReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.date_format));
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        try {
            Date d = format.parse(time);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(d);
            c.set(Calendar.HOUR_OF_DAY, c2.get(Calendar.HOUR_OF_DAY));
            c.set(Calendar.MINUTE, c2.get(Calendar.MINUTE));
            am.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }
}
