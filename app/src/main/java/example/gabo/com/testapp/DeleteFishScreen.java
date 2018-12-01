package example.gabo.com.testapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DeleteFishScreen extends AppCompatActivity{

    FishDatabaseController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrar);

        Log.d("RECEPTION","Received "+ getIntent().getStringExtra(MainActivity.MESSAGE_ID));

        controller = new FishDatabaseController(this.getBaseContext());

        String[] arg = {getIntent().getStringExtra("id")};
        FishEntry fishtemp = searchNameFish(FishEntry.COLUMN_ID+"=?",arg);
        ((TextView)(findViewById(R.id.speciesInput))).setText(fishtemp.getSpecies());;
        ((TextView)findViewById(R.id.amountInput)).setText(Float.toString(fishtemp.getAmount()));
        ((TextView)findViewById(R.id.searchNameField)).setText(fishtemp.getName());
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

    public void deleteFish(View view){
        TextView nameToSearch = findViewById(R.id.searchNameField);
        String[] arg ={nameToSearch.getText().toString()};
        Log.d("DEBUG,"," arg:"+arg[0]);
        FishEntry fish = new FishEntry();
        fish.setName(nameToSearch.getText().toString());
        FishEntry fish2 = searchNameFish(FishEntry.COLUMN_NAME+"=?", arg);
        if(fish2 != null){
            long deletion = controller.delete(fish2);
            Log.d("Database", "Deleted: "+deletion);
            goBack(view);
        }
    }

    public FishEntry searchNameFish(String selection, String[] arg){
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

    public void displayFish(View view){
        TextView fishName = findViewById(R.id.searchNameField);
        String[] arg = {fishName.getText().toString()};
        FishEntry fishy = searchNameFish(FishEntry.COLUMN_NAME+"=?", arg);
        Log.d("Query Result", "Found: "+fishy.getName()+ ", "+fishy.getAmount()+ ", "+fishy.getSpecies());
        if(fishy != null) {
            TextView fishSpecies = findViewById(R.id.speciesInput);
            TextView amountNum = findViewById(R.id.amountInput);
            fishName.setText(fishy.getName());
            fishSpecies.setText(fishy.getSpecies());
            amountNum.setText(Float.toString(fishy.getAmount()));
        }
    }
}
