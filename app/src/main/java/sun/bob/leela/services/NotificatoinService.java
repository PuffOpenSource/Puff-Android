package sun.bob.leela.services;

import android.app.Instrumentation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.widget.RemoteViews;

import sun.bob.leela.R;
import sun.bob.leela.ui.activities.MainActivity;

public class NotificatoinService extends Service {

    private String name, account, password, additional;

    private enum NotificationElement {
        Account,
        Password,
        Additional,
    }

    public NotificatoinService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        String cmd = intent.getAction();
        if (cmd.equalsIgnoreCase("start"))
            pingNotification();
        if (cmd.equalsIgnoreCase("paste"))
            pasteText("lalala");
        return START_NOT_STICKY;
    }

    private void pingNotification(){
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.remote_notification);

        // Set Notification Title
        String strtitle = "title";
        // Set Notification Text
        String strtext = "text";

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, MainActivity.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.ic_lock)
                // Set Ticker Message
                .setTicker("Ticker?")
                // Set PendingIntent into Notification
                .setContentIntent(pIntent);


        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.bigContentView = remoteViews;


        Intent pasteIntent = new Intent(this, NotificatoinService.class);
        pasteIntent.setAction("paste");
        PendingIntent pastePendingIntent = PendingIntent.getService(this, 0, pasteIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.account, pastePendingIntent);

        startForeground(22333, notification);

//        // Create Notification Manager
//        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // Build Notification with Notification Manager
//        notificationmanager.notify(0, notification);


    }

    private void updateNotification(NotificationElement removed){
        switch (removed) {
            case Account:
                break;
            case Password:
                break;
            case Additional:
                break;
            default:
                break;
        }
    }

    private void removeNotification(){

    }

    private void pasteText(final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                try {
                    Thread.sleep(3000);
                    inst.sendStringSync(text);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
