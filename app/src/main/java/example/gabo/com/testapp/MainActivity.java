package example.gabo.com.testapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import example.gabo.com.testapp.DatabaseActivity;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener{

    @Override
    public void onFragmentInteraction(Uri uri){


    }

    FishDatabaseController controller;

    public static String MESSAGE_ID = "com.example.fly.testApp.message";

    //creacion del fragmento
    MenuFragment menuFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //IntentFilter filter = new IntentFilter(ReminderService.ACTION_NOTIFICATION);
        //LocalBroadcastManager.getInstance(this).registerReceiver( new MainBroadcastReceiver(this, filter) );

        controller = new FishDatabaseController(this.getBaseContext());
        //Creacion del fragmento
        //menuFragment = new MenuFragment();
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //ft.add(R.id.fragmentContainer,menuFragment);
        //ft.commit();
    }

    public void buttonClicked(View view){
        menuFragment.doAction(this);
    }
/*
    public void buttonClicked(View view)
    {
        Log.d("Database App", "Hey. clicked!");

        Intent newActivity = new Intent(this, DatabaseScreen.class); //cambia la screen a la de databaseScreen

        EditText myText = (EditText)findViewById(R.id.editText); // R tiene toda la info de de los id
        EditText pwd = findViewById(R.id.pwdField);
        String value = myText.getText().toString();
        String pass = pwd.getText().toString();
        //String value = ""+myText.getText();
        newActivity.putExtra(MESSAGE_ID, value);
        startActivity(newActivity);
    }
*/
    public void createUser(View view){

        UserEntry usr = new UserEntry();
        EditText myText = findViewById(R.id.editText);
        EditText pwd = findViewById(R.id.pwdField);
        usr.setUser(myText.getText().toString());
        usr.setPassword(pwd.getText().toString());
        Long c = controller.insert(usr);
        if(c < 0)
            Log.d("Database", "Error creating user");
        else
            Log.d("Database App", "Created user: "+c);
    }

    public void loginAction(View view){

        Log.d("Database App", "Login check");
        UserEntry usr = new UserEntry();
        EditText myText = (EditText)findViewById(R.id.editText);
        EditText pwd = findViewById(R.id.pwdField);
        usr.setUser(myText.getText().toString());
        usr.setPassword(pwd.getText().toString());
        String[] args ={usr.getUser(),usr.getPassword()};
        Cursor cursor = controller.selectUsers(UserEntry.COLUMN_PASS+"=? AND "+UserEntry.COLUMN_PASS+"=?", args);
        if(!cursor.equals(null)) {
            Log.d("Login", "Valid");
            Intent newActivity = new Intent(this, LockScreen.class); //cambia la screen a la de databaseScreen
            //String value = ""+myText.getText();
            //newActivity.putExtra(MESSAGE_ID, value);
            startActivity(newActivity);

        }else{
            Log.d("Login", "User not found");
        }
    }

    public void startService(View view){
        FinalButtonFragment fbf = new FinalButtonFragment();
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.fragmentContainer, fbf);
        trans.addToBackStack(null);
        trans.commit();
        //ReminderService.sendNotificationAndSleep(this.getApplicationContext(),"Notification to be sent");
    }
/*
    public void sendNotification(String message){
        Log.d("Reminder Service", "Received message: " + message);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Title of notification")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("2 messsages " ))
                .setContentText("dfdsfdsfasdf")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1200, mBuilder.build());
    }
*/






}
