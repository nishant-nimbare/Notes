package com.example.mohan.notes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class myAlarm extends BroadcastReceiver {

    NotificationCompat.Builder notiBulder;
    @Override
    public void onReceive(Context context, Intent intent) {

        //making the notification
        notiBulder = new NotificationCompat.Builder(context,"notes");
        notiBulder.setSmallIcon(R.mipmap.ic_launcher_round);
        notiBulder.setTicker("Reminder for your note");
        notiBulder.setWhen(System.currentTimeMillis());
        notiBulder.setContentTitle(intent.getStringExtra("Title"));
        notiBulder.setContentText(intent.getStringExtra("desc"));
        notiBulder.setAutoCancel(true);

        //making the intent
        Intent intent1=new Intent(context,MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,1,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        notiBulder.setContentIntent(pendingIntent);

        //showing the notification
        Notification notification= notiBulder.build();
        notification.flags =  Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1,notification);

 //       Toast.makeText(context,"alarm works",Toast.LENGTH_SHORT).show();
    }
}
