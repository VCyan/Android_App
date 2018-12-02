package example.gabo.com.testapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ModifyFishScreen extends AppCompatActivity{

    FishDatabaseController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar);
        controller = new FishDatabaseController(this.getBaseContext());
        Log.d("RECEPTION","Received "+ getIntent().getStringExtra(MainActivity.MESSAGE_ID));
        String[] arg = {getIntent().getStringExtra("id")};
        FishEntry fish = searchNameFish(FishEntry.COLUMN_ID+"=?",arg);
        displayInfo(fish);

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


    public void modifyFish(View view){

        TextView nameSearch = findViewById(R.id.idField);
        String[] arg = {nameSearch.getText().toString()};
        FishEntry fishy = searchNameFish(FishEntry.COLUMN_ID+"=?",arg);

        TextView name = findViewById(R.id.searchNameField);
        TextView amount = findViewById(R.id.amountInput);
        TextView species = findViewById(R.id.speciesInput);
        fishy.setName(name.getText().toString());
        fishy.setAmount(Float.parseFloat(amount.getText().toString()));
        fishy.setSpecies(species.getText().toString());

        Long d = controller.update(fishy);
        Log.d("Update", "Modified: "+d);
        goBack(view);
    }

    public void displayInfo(FishEntry fish){

        if(fish != null) {

            TextView fishName = findViewById(R.id.searchNameField);
            TextView fishSpecies = findViewById(R.id.speciesInput);
            TextView fishID = findViewById(R.id.idField);
            TextView amountNum = findViewById(R.id.amountInput);
            fishName.setText(fish.getName());
            fishID.setText(Integer.toString(fish.getId()));
            fishSpecies.setText(fish.getSpecies());
            amountNum.setText(Float.toString(fish.getAmount()));
        }

    }

    public FishEntry searchNameFish(String selection,String[] arg){
        FishEntry fisho = new FishEntry();
        Log.d("database","fishy y arg"+arg[0]);
        Cursor cursor = controller.selectFishes(selection,arg);
        if(cursor != null) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_NAME));
            String amount = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_AMOUNT));
            String species = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_SPECIES));
            String id = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_ID));
            Log.d("Query Result", "Found: "+name+ ", "+amount+ ", "+species);

            fisho.setName(name);
            fisho.setAmount(Float.parseFloat(amount));
            fisho.setSpecies(species);
            fisho.setId(Integer.parseInt(id));
            return fisho;
        }
        return null;


    }

    public void searchNameFish(View view){
        TextView nameToSearch = findViewById(R.id.searchNameField);
        String[] arg ={nameToSearch.getText().toString()};
        Log.d("DEBUG,"," arg:"+arg[0]);
        Cursor cursor = controller.selectFishes(FishEntry.COLUMN_NAME+"=?",arg);
        if(cursor != null) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_NAME));
            String amount = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_AMOUNT));
            String species = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_SPECIES));
            String id = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_ID));
            Log.d("Query Result", "Found: "+name+ ", "+amount+ ", "+species);

            TextView fishName = findViewById(R.id.searchNameField);
            TextView fishSpecies = findViewById(R.id.speciesInput);
            TextView amountNum = findViewById(R.id.amountInput);
            fishName.setText(name);
            fishSpecies.setText(species);
            amountNum.setText(amount);
        }

    }
}
