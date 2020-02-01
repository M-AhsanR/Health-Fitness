package com.giggly.app.Models;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.giggly.app.Activity.FeedDetailedActivity;
import com.giggly.app.Activity.HomeActivity;
import com.giggly.app.Fragments.FeedFragment;
import com.giggly.app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.leolin.shortcutbadger.ShortcutBadger;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG="";
    String feed_id;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        sendNotification(remoteMessage);






    }
    private void sendNotification(RemoteMessage message) {

        String title=message.getData().get("title");
        String msg=message.getData().get("message");
        feed_id=message.getData().get("feed_id");
//        Log.e(TAG, "GET_ID" + feed_id);




        // set badge count
        SharedPreferences mPrefs2=getSharedPreferences("Notification_badges_count",Context.MODE_PRIVATE);

        int count=mPrefs2.getInt("count",0);
        count=count+1;

        SharedPreferences.Editor editor2=mPrefs2.edit();
        editor2.putInt("count",count);
        editor2.apply();


        // set badge count



        Intent intent=new Intent(this,FeedDetailedActivity.class);

        intent.putExtra("KEYBOADR_STATUS", "feed_img");
        intent.putExtra("Feed_id", feed_id);
        intent.putExtra("Notification", "true");


        int color = 0xff123456;
        color = getResources().getColor(R.color.giggly_status_bar_color);
        color = ContextCompat.getColor(MyFirebaseMessagingService.this, R.color.giggly_status_bar_color);


        // set response code new
        SharedPreferences mPrefs=getSharedPreferences("Response_Code",Context.MODE_PRIVATE);

        int code = mPrefs2.getInt("code",0);
        code=code+1;

        if( code >= 100 ){
            code = 0;
        }
        SharedPreferences.Editor editor=mPrefs.edit();
        editor.putInt("code",code);
        editor.apply();

        // set response code new

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this, code ,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificatin_builder = new NotificationCompat.Builder(this);
        notificatin_builder
                .setSmallIcon(R.drawable.notification_small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_))
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(msg)
                .setSound(defaultSoundUri)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setColor(color)


//                .setCustomContentView(normal_layout)


                .setPriority(android.app.Notification.PRIORITY_HIGH);

        NotificationManager notification_manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notification_manager.notify(0,notificatin_builder.build());



        // app icon badge
        ShortcutBadger.applyCount(MyFirebaseMessagingService.this, count); //for 1.1.4+
//        ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3
        // app icon badge


    }
}

