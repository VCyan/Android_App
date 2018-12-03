package example.gabo.com.testapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import example.gabo.com.testapp.DatabaseActivity;

public class MainActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener{
    int attempt_counter=5;
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

    @Override
    protected void onDestroy(){
        super.onDestroy();
        controller.close();
    }

    @Override
    public void onFragmentInteraction(Uri uri){


    }

    public void buttonClicked(View view){
        menuFragment.doAction(this);
    }

    public void createUser(View view){
//        UserEntry usr = new UserEntry();
        EditText myText = findViewById(R.id.editText_Username);
        EditText pwd = findViewById(R.id.editText_Password);

        Bundle extras = new Bundle();
        extras.putString("userToR",myText.getText().toString());
        extras.putString("passToR",pwd.getText().toString());
//        usr.setUser(myText.getText().toString());
//        usr.setPassword(pwd.getText().toString());

//        Long c = controller.insert(usr);
//        if(c < 0)
//            Log.d("Database", "Error creating user");
//        else
//            Log.d("Database", "Created user: "+c);
        Intent newActivity = new Intent(this, PatternScreen.class);
        newActivity.putExtras(extras);
        startActivity(newActivity);
    }

    public void loginAction(View view){
        Log.d("Database App", "Login check");
        UserEntry usr = new UserEntry();
        EditText myText = findViewById(R.id.editText_Username);
        EditText pwd = findViewById(R.id.editText_Password);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView attemptTextView = findViewById(R.id.textView_attempt);

        usr.setUser(myText.getText().toString());
        usr.setPassword(pwd.getText().toString());
        String[] args ={usr.getUser(),usr.getPassword()};
        Cursor cursor = controller.selectUsers(UserEntry.COLUMN_USER+"=? AND "+UserEntry.COLUMN_PASS+"=?", args);


        if(cursor!=null && cursor.getCount()>0) {
            cursor.moveToFirst();
            Log.d("DENTRO CURSOR",  DatabaseUtils.dumpCurrentRowToString(cursor));
//            String a1 = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_USER));
//            String a2 = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PASS));
            String userPattern = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PATTERN));
//            Log.d("Database App", userPattern);
            cursor.close();
            Log.d("Login", "Valid");
            Toast.makeText(this,"Username and password is correct",
                    Toast.LENGTH_SHORT).show();

            Bundle extras = new Bundle();
            extras.putString("patternToR",userPattern);

            Intent newActivity = new Intent(this, LockScreen.class);
            newActivity.putExtras(extras);
            startActivity(newActivity);

        }else{
            Log.d("Login", "User not found");
            Toast.makeText(this,"Username and password is NOT correct",
                    Toast.LENGTH_SHORT).show();
            attempt_counter--;
            attemptTextView.setText(Integer.toString(attempt_counter));
            if(attempt_counter==0)
                buttonLogin.setEnabled(false);
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
}