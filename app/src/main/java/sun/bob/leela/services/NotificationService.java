package sun.bob.leela.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import sun.bob.leela.R;
import sun.bob.leela.ui.activities.MainActivity;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.ClipboardUtil;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.StringUtil;
import sun.bob.leela.utils.UserDefault;

public class NotificationService extends Service {

    private String name, account, password, additional, icon;
    private RemoteViews remoteViews;
    private Notification notification;
    private NotificationManager notificationmanager;
    private Handler handler;
    private Runnable autoClearRunnable;

    private enum NotificationElement {
        Account,
        Password,
        Additional,
    }

    public NotificationService() {
        handler = new Handler();
        autoClearRunnable = new Runnable() {
            @Override
            public void run() {
                stopForeground(true);
                stopSelf();
            }
        };
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
            icon = intent.getStringExtra("icon");
            if (StringUtil.isNullOrEmpty(name, account, password, additional))
                return START_NOT_STICKY;
            pingNotification();
            setAutoClear();
        }
        if (cmd.equalsIgnoreCase(AppConstants.SERVICE_CMD_PASTE_ACCT)) {
            pasteText(account);
            updateNotification(NotificationElement.Account);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        }
        if (cmd.equalsIgnoreCase(AppConstants.SERVICE_CMD_PASTE_PSWD)) {
            pasteText(password);
            updateNotification(NotificationElement.Password);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        }
        if (cmd.equalsIgnoreCase(AppConstants.SERVICE_CMD_PASTE_ADDT)) {
            pasteText(additional);
            updateNotification(NotificationElement.Additional);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        }

        if (cmd.equalsIgnoreCase("stop")) {
            stopForeground(true);
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void pingNotification(){
        notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        remoteViews = new RemoteViews(getPackageName(),
                R.layout.remote_notification);


        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, MainActivity.class);
        // Send data to NotificationView Class
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.mipmap.ic_launcher)
                // Set Ticker Message
                .setTicker("Use notification to paste account info")
                // Set PendingIntent into Notification
                .setContentIntent(pIntent);


        notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.bigContentView = remoteViews;
        notification.contentView = remoteViews;

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

        Intent clearIntent = new Intent(this, NotificationService.class);
        clearIntent.setAction("stop");
        PendingIntent clearPendingIntent = PendingIntent.getService(this, 0, clearIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.clear, clearPendingIntent);



        //Setup remote views.
        if (StringUtil.isNullOrEmpty(account))
            remoteViews.setViewVisibility(R.id.account, View.GONE);
        if (StringUtil.isNullOrEmpty(password))
            remoteViews.setViewVisibility(R.id.password, View.GONE);
        if (StringUtil.isNullOrEmpty(additional))
            remoteViews.setViewVisibility(R.id.additional, View.GONE);

        remoteViews.setTextViewText(R.id.remote_name, name);
        Picasso.with(this).load(ResUtil.getInstance(getApplicationContext()).getBmpUri(icon))
                .into(remoteViews, R.id.remote_icon, 22333, notification);

        startForeground(22333, notification);
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

    private void setAutoClear(){
        int sec = UserDefault.getInstance(getApplicationContext()).getAutoClearTimeInSeconds();
        handler.removeCallbacks(autoClearRunnable);
        handler.postDelayed(autoClearRunnable, sec * 1000);
    }

}
