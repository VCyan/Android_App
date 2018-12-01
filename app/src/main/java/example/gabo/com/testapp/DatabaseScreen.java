package example.gabo.com.testapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static java.lang.Math.abs;

public class DatabaseScreen extends AppCompatActivity implements ExternTables.OnFragmentInteractionListener{

    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private SensorEventListener gyroEventListener;

    private Vibrator v;

    @Override
    public void onFragmentInteraction(Uri uri){ }

    ExternTables externTable;

    FishDatabaseController controller;
    public static String MESSAGE_ID = "com.example.fly.testApp.message";
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_screen);

        Log.d("RECEPTION","Received "+ getIntent().getStringExtra(MainActivity.MESSAGE_ID));

        controller = new FishDatabaseController(this.getBaseContext());

        externTable = new ExternTables();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.add(R.id.externalContainer,externTable);
        ft.commit();

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

                if (event.values[2] < -2.0f) {
                    //getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    goRight();
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

    private void goRight() {
        Intent myIntent = new Intent(this, AddFishScreen.class);
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

    public void loadMof(){}

    public void addScreen(View view){
        Intent newAct = new Intent(this, AddFishScreen.class); //cambia la screen a la de databaseScreen
        //newActivity.putExtra(MESSAGE_ID, value);
        startActivity(newAct);
    }

    public void modifyScreen(View view){
        Intent newAct = new Intent(this, ModifyFishScreen.class); //cambia la screen a la de databaseScreen

        TextView idr = ((View)view.getParent()).findViewById(R.id.idrow);
        //Log.d("Testing", "id:"+idr.getText().toString());
        String idSend = ""+idr.getText();
        newAct.putExtra("id", idSend);

        startActivity(newAct);
    }

    public void deleteScreen(View view){
        Intent newAct = new Intent(this, DeleteFishScreen.class); //cambia la screen a la de databaseScreen
        //newActivity.putExtra(MESSAGE_ID, value);
        TextView idr = ((View)view.getParent()).findViewById(R.id.idrow);
        String idSend = ""+idr.getText();
        newAct.putExtra("id", idSend);
        startActivity(newAct);
    }

    public void exportDb(View view){
        Log.d("","Click");
        try {
            controller.copyAppDbToDownloadFolder(this);
        }catch(IOException ioe){
            Log.d("Database", "Error exporting db");
        }
    }



    public void sendPost(View view) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.1.67/movies/android.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    String json = createJSON();
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(json);

                    os.flush();
                    os.close();

                    InputStream is = new BufferedInputStream(conn.getInputStream());
                    String s = readStream(is);

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());
                    Log.i("RESPONSE",s);


                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }











    public String createJSON(){

        Cursor cursor = controller.selectFishes("",null);
        FishEntry fish = new FishEntry();
        JSONArray ja = new JSONArray();
        try {
            //Log.d("JSON",""+cursor.moveToNext());
            //do{
            while(cursor.moveToNext()){
                fish.setName(cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_NAME)));
                fish.setAmount(Float.parseFloat(cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_AMOUNT))));
                fish.setSpecies(cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_SPECIES)));
                fish.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_ID))));
                //Log.d("JSON","Start");
                JSONObject jo = new JSONObject();
                jo.put("id", fish.getId());
                jo.put("title", fish.getName());
                jo.put("lang", fish.getSpecies());
                jo.put("score", fish.getAmount());
                Log.d("JSON", jo.toString());
                ja.put(jo);
            }//while(cursor.moveToNext());

            JSONObject mainObj = new JSONObject();
            mainObj.put("data",ja);
            Log.d("JSON data", mainObj.toString(1));
            return mainObj.toString(1);
        }catch(JSONException jsoe){
            Log.d("JSON", "Error parsing JSON");
        }

        return "Error";
    }

    public void loadDatabase(View view)
    {
        Cursor cursor = controller.selectFishes("",null);

        ViewGroup table = findViewById(R.id.fishTable);
        table.removeAllViews();

        while(cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_NAME));
            String amount = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_AMOUNT));
            String species = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_SPECIES));
            String id = cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_ID));

            View row = getLayoutInflater().inflate(R.layout.fish_row, null);
            TextView fishName = row.findViewById(R.id.fishName);
            TextView fishSpecies = row.findViewById(R.id.fishSpecies);
            TextView amountNum = row.findViewById(R.id.fishAmount);
            TextView idnum = row.findViewById(R.id.idrow);

            fishName.setText(name);
            idnum.setText(id);
            fishSpecies.setText(species);
            amountNum.setText(amount);

            table.addView(row);

            Log.d("DATABASE", "Database register" +name+","+amount+","+species+","+id);
        }

    }

    public void loadData(View view){
        externTable.loadExternal(this);
    }

    public void insertLocal(View view){
        externTable.insertExternal(this, view);
    }




}
