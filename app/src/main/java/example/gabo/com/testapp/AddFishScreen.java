package example.gabo.com.testapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    }

    public void onDestroy()
    {
        super.onDestroy();
        controller.close();
    }

    public void goBack(View view){
        Intent mainMenu = new Intent(this, DatabaseScreen.class);
        startActivity(mainMenu);
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
