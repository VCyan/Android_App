package example.gabo.com.testapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExternTables.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExternTables#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExternTables extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Activity activity;

    public ExternTables() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExternTables.
     */
    // TODO: Rename and change types and number of parameters
    public static ExternTables newInstance(String param1, String param2) {
        ExternTables fragment = new ExternTables();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extern_data, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void insertExternal(Activity activity, View view){

        FishDatabaseController controller = new FishDatabaseController(activity.getBaseContext());
        FishEntry fishy = new FishEntry();
        TextView name = ((View)view.getParent()).findViewById(R.id.nameInput);
        Log.d("DDD","NAME: "+name.getText().toString());
        TextView amount = ((View)view.getParent()).findViewById(R.id.amountInput);
        TextView species = ((View)view.getParent()).findViewById(R.id.speciesInput);
        fishy.setName(name.getText().toString());
        fishy.setAmount(Float.parseFloat(amount.getText().toString()));
        fishy.setSpecies(species.getText().toString());
        long inserted = controller.insert(fishy);
        Log.d("Database", "Insertion "+fishy.getName()+","+fishy.getSpecies()+"," +fishy.getAmount());
        controller.close();

        //ViewGroup tableGroup = (ViewGroup)activity.findViewById(R.id.externTable);
        //tableGroup.removeViewAt(R.id.idrow);

    }

    public void loadExternal(Activity activity){

        // https://sportsop-soccer-sports-open-data-v1.p.mashape.com/v1/leagues/{league_slug}
    /*
    *
    *  api key : 6011b6ef8071bfb0711351fd9eddd5e1
    *   http://api.themoviedb.org/3/authentication/token/new?api_key=6011b6ef8071bfb0711351fd9eddd5e1
    // exaemple call: http://api.themoviedb.org/3/search/person?api_key=6011b6ef8071bfb0711351fd9eddd5e1&query=smith
    * */
    this.activity = activity;
        try{
            URL url =  new URL("https", "api.themoviedb.org", 443, "/3/search/movie?api_key=b33a3f6a319f7329e9aef66430525ec2&query=Hunger+Games" );

            new ExternTables.DownloadMovieData().execute(url);
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

        ViewGroup tableGroup = (ViewGroup)activity.findViewById(R.id.externTable);
        tableGroup.removeAllViews();

        try{
            JSONObject json = new JSONObject(str);
            JSONArray map = (JSONArray) json.get("results");

            for(int i = 0; i < map.length() ; i++){

                JSONObject cursor = (JSONObject) map.get(i);

                Log.d("RESULTS " , "-><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + cursor.getString("title") );

                Log.d("REGISTRY" , cursor.getString( "title"));
                View instance = getLayoutInflater().inflate(R.layout.external_data_layout, null);
                TextView textView = (TextView)instance.findViewById(R.id.nameInput);
                TextView dat2 = (TextView)instance.findViewById(R.id.speciesInput);
                TextView dat3 = (TextView)instance.findViewById(R.id.amountInput);
                textView.setText( cursor.getString("title"));
                dat2.setText(cursor.getString("original_language"));
                dat3.setText(cursor.getString("vote_average"));
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
