package example.gabo.com.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class PatternScreen extends AppCompatActivity {

    FishDatabaseController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_screen);

        controller = new FishDatabaseController(this.getBaseContext());

        final PatternLockView patternLockView = findViewById(R.id.patternView);
        final Bundle extras = getIntent().getExtras();
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            boolean confirmPattern = true;
            String userToInsert = extras.getString("userToR");
            String passToInsert = extras.getString("passToR");
            String patternValue = "012";
            @Override
            public void onStarted() {
                Toast.makeText(PatternScreen.this, "Please create a Pattern", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(List progressPattern) {

            }

            @Override
            public void onComplete(List pattern) {
                Log.d(getClass().getName(), "Pattern complete: " + PatternLockUtils.patternToString(patternLockView, pattern));
                if(confirmPattern){
                    patternValue = PatternLockUtils.patternToString(patternLockView, pattern);
                    Toast.makeText(PatternScreen.this, "Please repeat Pattern", Toast.LENGTH_SHORT).show();
                    confirmPattern = false;
                }
                else{
                    if(patternValue.equalsIgnoreCase(PatternLockUtils.patternToString(patternLockView, pattern))){
                        UserEntry usr = new UserEntry();
                        usr.setUser(userToInsert);
                        usr.setPassword(passToInsert);
                        usr.setPattern(patternValue);
                        Long c = controller.insert(usr);
                        if(c < 0) {
                            Log.d("Database", "Error creating user");
                            PatternScreen.this.finish();
                        }
                        else {
                            Log.d("Database", "Created user: " + c);
                            Toast.makeText(PatternScreen.this, "User already registered...", Toast.LENGTH_LONG).show();
                            PatternScreen.this.finish();
                        }
                    }else{
                        Toast.makeText(PatternScreen.this, "Incorrect Pattern. Repeat Twice", Toast.LENGTH_SHORT).show();
                        confirmPattern = true;
                    }
                }
            }

            @Override
            public void onCleared() {

            }
        });
    }

    public void openApp(){
        Intent newActivity = new Intent(this, DatabaseScreen.class); // Cambia la screen a la de databaseScreen
        startActivity(newActivity);
    }
}
