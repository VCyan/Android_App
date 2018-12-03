package example.gabo.com.testapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddFishScreen extends AppCompatActivity {

    FishDatabaseController controller;
    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private SensorEventListener gyroEventListener;

    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar);

        Log.d("RECEPTION","Received "+ getIntent().getStringExtra(MainActivity.MESSAGE_ID));

        controller = new FishDatabaseController(this.getBaseContext());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (gyroSensor == null){
            Toast.makeText(this, "This Device Has No Gyroscope !", Toast.LENGTH_SHORT).show();
            finish();
        }
        gyroEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
/*
                if (abs(event.values[2]) <= 2.0f) {
                    //getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                }*/

                if (event.values[2] > 5.0f) {
                    //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    goLeft();
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void vib() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
    }

    private void goLeft() {
        Intent myIntent = new Intent(this, DatabaseScreen.class);
        vib();
        startActivity(myIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroEventListener, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroEventListener);
    }

    public void onDestroy()
    {
        super.onDestroy();
        controller.close();
    }

    public void goBack(View view){
        this.finish();
    }

    public void insertInDb(View view)
    {
        FishEntry fishy = new FishEntry();
        fishy.setName("Fly");
        fishy.setAmount(1);
        fishy.setSpecies("NemoType");
        long inserted = controller.insert(fishy);
        Log.d("Database", "Insertion"+inserted);
    }

    public void insertFish(View view)
    {
        FishEntry fishy = new FishEntry();
        TextView name = findViewById(R.id.nameInput);
        TextView amount = findViewById(R.id.amountInput);
        TextView species = findViewById(R.id.speciesInput);
        fishy.setName(name.getText().toString());
        fishy.setAmount(Float.parseFloat(amount.getText().toString()));
        fishy.setSpecies(species.getText().toString());
        long inserted = controller.insert(fishy);
        Log.d("Database", "Insertion "+fishy.getName()+","+fishy.getSpecies()+"," +fishy.getAmount());


    }
}
