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

public class LockScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern_screen);
        final PatternLockView patternLockView = findViewById(R.id.patternView);

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List progressPattern) {

            }

            @Override
            public void onComplete(List pattern) {
                Log.d(getClass().getName(), "Pattern complete: " + PatternLockUtils.patternToString(patternLockView, pattern));
                if(PatternLockUtils.patternToString(patternLockView, pattern).equalsIgnoreCase("123")){
                    Toast.makeText(LockScreen.this, "Welcome back!", Toast.LENGTH_LONG).show();
                    openApp();
                }else{
                    Toast.makeText(LockScreen.this, "Incorrect password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCleared() {

            }
        });
    }

    public void openApp(){
        Intent newActivity = new Intent(this, DatabaseScreen.class); //cambia la screen a la de databaseScreen
        //String value = ""+myText.getText();
        //newActivity.putExtra(MESSAGE_ID, value);
        startActivity(newActivity);
    }
}
