package example.gabo.com.testapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static java.lang.System.currentTimeMillis;


public class ReminderService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    //private static final String ACTION_SEND_NOTIF = "example.gabo.com.testapp.action.SEND_NOTIF";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "example.gabo.com.testapp.extra.PARAM1";
    public static final String ACTION_NOTIFICATION = "com.example.joe.startapp.action.NOTIFICATION";

    public static final String RECEIVE_ACTION = "com.example.joe.startapp.action.RECEIVE_NOTIFICATION_ACTION";

    public static final String EXTRA_MESSAGE = "com.example.joe.startapp.extra.MESSAGE";

    public static final String ACTION_MESSAGE = "com.example.joe.startapp.action.MESSAGE_RECEPTION";

    public ReminderService() {
        super("ReminderService");
    }

    public static void sendNotificationAndSleep(Context context, String message){

        Intent intent = new Intent(context, ReminderService.class);
        intent.setAction(ACTION_NOTIFICATION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("REMINDEr SERVICE", "Service initiated onHandleIntent");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }


        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFICATION.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleNotification(param1);//intent.getStringExtra(EXTRA_MESSAGE));
            }
        }
    }

        /**
         * Handle action Foo in the provided background thread with the provided
         * parameters.
         */
        private void handleNotification(String param){

            Intent localIntent =  new Intent(ReminderService.ACTION_NOTIFICATION).putExtra(ReminderService.EXTRA_PARAM1, param);
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);


        }

}
