package sun.bob.leela.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import sun.bob.leela.R;
import sun.bob.leela.ui.activities.MainActivity;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.ClipboardUtil;
import sun.bob.leela.utils.StringUtil;

public class NotificationService extends Service {

    private String name, account, password, additional;
    private RemoteViews remoteViews;
    private Notification notification;
    private NotificationManager notificationmanager;

    private enum NotificationElement {
        Account,
        Password,
        Additional,
    }

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        String cmd = intent.getAction();
        if (cmd.equalsIgnoreCase(AppConstants.SERVICE_CMD_START)){
            name = intent.getStringExtra("name");
            account = intent.getStringExtra("account");
            password = intent.getStringExtra("password");
            additional = intent.getStringExtra("additional");
            if (StringUtil.isNullOrEmpty(name, account, password, additional))
                return START_NOT_STICKY;
            pingNotification();
        }
        if (cmd.equalsIgnoreCase(AppConstants.SERVICE_CMD_PASTE_ACCT)) {
            pasteText(account);
            updateNotification(NotificationElement.Account);
        }
        if (cmd.equalsIgnoreCase(AppConstants.SERVICE_CMD_PASTE_PSWD)) {
            pasteText(password);
            updateNotification(NotificationElement.Password);
        }
        if (cmd.equalsIgnoreCase(AppConstants.SERVICE_CMD_PASTE_ADDT)) {
            pasteText(additional);
            updateNotification(NotificationElement.Additional);
        }

        return START_NOT_STICKY;
    }

    private void pingNotification(){
        remoteViews = new RemoteViews(getPackageName(),
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


        notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.bigContentView = remoteViews;

        //Wire up actions.
        Intent pasteAccountIntent = new Intent(this, NotificationService.class);
        pasteAccountIntent.setAction(AppConstants.SERVICE_CMD_PASTE_ACCT);
        PendingIntent pasteAccountPendingIntent = PendingIntent.getService(this, 0, pasteAccountIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.account, pasteAccountPendingIntent);

        Intent pastePasswordIntent = new Intent(this, NotificationService.class);
        pastePasswordIntent.setAction(AppConstants.SERVICE_CMD_PASTE_PSWD);
        PendingIntent pastePasswordPendingIntent = PendingIntent.getService(this, 0, pastePasswordIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.password, pastePasswordPendingIntent);

        Intent pasteAddtIntent = new Intent(this, NotificationService.class);
        pasteAddtIntent.setAction(AppConstants.SERVICE_CMD_PASTE_ADDT);
        PendingIntent pasteAddtPendingIntent = PendingIntent.getService(this, 0, pasteAddtIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.additional, pasteAddtPendingIntent);

        //Setup remote views.
        if (StringUtil.isNullOrEmpty(account))
            remoteViews.setViewVisibility(R.id.account, View.GONE);
        if (StringUtil.isNullOrEmpty(password))
            remoteViews.setViewVisibility(R.id.password, View.GONE);
        if (StringUtil.isNullOrEmpty(additional))
            remoteViews.setViewVisibility(R.id.additional, View.GONE);


        startForeground(22333, notification);

        notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        // Create Notification Manager
//        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        // Build Notification with Notification Manager
//        notificationmanager.notify(0, notification);


    }

    private void updateNotification(NotificationElement removed){
        switch (removed) {
            case Account:
                account = "";
                remoteViews.setViewVisibility(R.id.account, View.GONE);
                notificationmanager.notify(22333, notification);
                break;
            case Password:
                password = "";
                remoteViews.setViewVisibility(R.id.password, View.GONE);
                notificationmanager.notify(22333, notification);
                break;
            case Additional:
                additional = "";
                remoteViews.setViewVisibility(R.id.additional, View.GONE);
                notificationmanager.notify(22333, notification);
                break;
            default:
                break;
        }

        if (StringUtil.isNullOrEmpty(account, password, additional)) {
            stopForeground(true);
            stopSelf();
        }
    }

    private void pasteText(final String text) {
        ClipboardUtil.getInstance(this.getApplicationContext()).setText(text);
    }
}
