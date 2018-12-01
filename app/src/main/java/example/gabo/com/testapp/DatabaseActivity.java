package example.gabo.com.testapp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.service.autofill.UserData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import example.gabo.com.testapp.FishDatabaseController;
import example.gabo.com.testapp.FishEntry;
import example.gabo.com.testapp.R;

public class DatabaseActivity extends AppCompatActivity{

    FishDatabaseController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_screen);


        controller = new FishDatabaseController(getBaseContext());
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        Log.d("DATABASE", "Closing!");
        controller.close();
    }

    void buttonClicked(View view){
    }

    void addButtonClicked(View view){



    }

    void removeButtonClicked(View view){



    }

    void searchButtonClicked(View view){

        Log.d("DATABASE", "Trying!");

        ViewGroup tableGroup = (ViewGroup)findViewById(R.id.fishTable);



        Cursor cursor = controller.select(null,null );
        while(cursor.moveToNext()){
            Log.d("REGISTRY" , cursor.getString( cursor.getColumnIndex(FishEntry.COLUMN_NAME)));
            View instance = getLayoutInflater().inflate(R.layout.fish_row, null);
            TextView textView = (TextView)instance.findViewById(R.id.fishName);
            textView.setText( cursor.getString(cursor.getColumnIndex(FishEntry.COLUMN_NAME)));
            tableGroup.addView(instance);
        }
        cursor.close();


        //EditText text = (EditText) findViewById(R.id.multilineText);
        //text.append("hello");
    }

    void loadWebFile(View view){

        // https://sportsop-soccer-sports-open-data-v1.p.mashape.com/v1/leagues/{league_slug}
    /*
    *
    *  api key : 6011b6ef8071bfb0711351fd9eddd5e1
    *   http://api.themoviedb.org/3/authentication/token/new?api_key=6011b6ef8071bfb0711351fd9eddd5e1
    // exaemple call: http://api.themoviedb.org/3/search/person?api_key=6011b6ef8071bfb0711351fd9eddd5e1&query=smith
    * */

        try{
            URL url =  new URL("https", "api.themoviedb.org", 443, "/3/search/person?api_key=6011b6ef8071bfb0711351fd9eddd5e1&query=smith" );

            new DownloadMovieData().execute(url);
        }catch (MalformedURLException mue){
            Log.d("CANCELLED!!" , "POOR URL");
        }

    }

    void changeImage(View view){


    }

    void setProgressPercent(Integer progress){

        Log.d("PROGRESS " , "We are on  " + progress + " of task" );

    }

    void showDialog(String str){

        Log.d("FINAL PROGRESS " , str );

        ViewGroup tableGroup = (ViewGroup)findViewById(R.id.fishTable);

        try{
            JSONObject json = new JSONObject(str);
            JSONArray map = (JSONArray) json.get("results");

            for(int i = 0; i < map.length() ; i++){

                JSONObject cursor = (JSONObject) map.get(i);

                Log.d("RESULTS " , "-><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + cursor.getString("name") );

                Log.d("REGISTRY" , cursor.getString( "name"));
                View instance = getLayoutInflater().inflate(R.layout.fish_row, null);
                TextView textView = (TextView)instance.findViewById(R.id.fishName);
                textView.setText( cursor.getString("name"));
                tableGroup.addView(instance);

            }
        }catch (JSONException jse){
            Log.e("Json","Json Error " + jse);
        }
    }


    class DownloadMovieData extends AsyncTask<URL, Integer, Long> {

        String finalString = "";

        protected void onPreExecute(){
            Log.d("PROGRESS EXECUTE", "Starting");

        }
        protected Long doInBackground(URL... urls) {

            try{

                if( !isCancelled()){



                    int count = urls.length;
                    long totalSize = 0;



                    URL url = urls[0];


                    finalString = sendGETRequestHttpsTrustAll(url);

                    Log.d("FINAL STRING DONE","READY");

                    publishProgress(new Integer(100));
                    return totalSize;
                }

            }catch (Exception ioe){

                Log.d("ERROR","ERROR ON LOADING " + ioe);
            }
            return Long.valueOf(0);
        }

        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            showDialog(finalString);
        }


        private String sendGETRequestHttp() { // String requestString (If have POST parameters)
            DataInputStream dis = null;
            StringBuffer messagebuffer = new StringBuffer();
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://www.yoururl.com/"); //Simple HttpURLConnection request

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("GET"); //("POST");

                //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                //out.write(requestString.getBytes());
                //out.flush();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                dis = new DataInputStream(in);
                int ch;
                long len = urlConnection.getContentLength();

                if (len != -1) {
                    for (int i = 0; i < len; i++)
                        if ((ch = dis.read()) != -1) {
                            messagebuffer.append((char) ch);
                        }
                } else {
                    while ((ch = dis.read()) != -1)
                        messagebuffer.append((char) ch);
                }
                dis.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return messagebuffer.toString();
        }

        private String sendGETRequestHttpsTrustAll(URL url) { // String requestString (If have POST parameters)
            DataInputStream dis = null;
            StringBuffer messagebuffer = new StringBuffer();
            HttpURLConnection urlConnection = null;
            try {
                //("https://www.codeproject.com/");

                //Connection port HTTPS
                HttpsURLConnection urlHttpsConnection = null;

                if (url.getProtocol().toLowerCase().equals("https")) {

                    trustAllHosts(); //Trust all certificate
                    //Open Connection
                    urlHttpsConnection = (HttpsURLConnection) url.openConnection();
                    //Set Verifier
                    urlHttpsConnection.setHostnameVerifier(DO_NOT_VERIFY);
                    //Assigning value
                    urlConnection = urlHttpsConnection;

                } else {
                    urlConnection = (HttpURLConnection) url.openConnection();
                }

                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("GET"); //("POST");

                //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                //out.write(requestString.getBytes());
                //out.flush();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                dis = new DataInputStream(in);
                int ch;
                long len = urlConnection.getContentLength();

                if (len != -1) {
                    for (int i = 0; i < len; i++)
                        if ((ch = dis.read()) != -1) {
                            messagebuffer.append((char) ch);
                        }
                } else {
                    while ((ch = dis.read()) != -1)
                        messagebuffer.append((char) ch);
                }
                dis.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return messagebuffer.toString();
        }


        final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };


    }

    private static void trustAllHosts() {
        X509TrustManager easyTrustManager = new X509TrustManager() {
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{easyTrustManager};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
