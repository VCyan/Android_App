package example.gabo.com.testapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.time.LocalDate;

public class MainBroadcastReceiver extends BroadcastReceiver {

    MainActivity main;

    public MainBroadcastReceiver(MainActivity m){
        main = m;
    }

    @Override
    public void onReceive(Context context, Intent intent){
        String message = intent.getStringExtra(ReminderService.EXTRA_PARAM1);
        //main.sendNotification(message);
    }



}
