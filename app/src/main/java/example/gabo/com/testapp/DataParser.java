package example.gabo.com.testapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser
{

    private HashMap<String, String> getSingleNearbyPlace(JSONObject googlePlaceJSON)
    {
        HashMap<String,String> googlePaceMap = new HashMap<>();
        String placeName = "~NA~";
        String vicinity= "~NA~";
        String latitude = "";
        String longitude= "";
        String reference = "";

        try {

            if(!googlePlaceJSON.isNull("name"))
                placeName = googlePlaceJSON.getString("name");
            if(!googlePlaceJSON.isNull("vicinity"))
                vicinity = googlePlaceJSON.getString("vicinity");

            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJSON.getString("reference");
            googlePaceMap.put("place_name", placeName);
            googlePaceMap.put("vicinity", vicinity);
            googlePaceMap.put("lat", latitude);
            googlePaceMap.put("lng", longitude);
            googlePaceMap.put("reference", reference);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePaceMap;
    }


    private List<HashMap<String,String>> getAllNearbyPlaces(JSONArray jsonArray){

        int counter = jsonArray.length();
        List<HashMap<String,String>> nearbyPlacesList = new ArrayList<>();
        HashMap<String,String> nearbyPlaceMap = null;

        for(int i =0; i < counter; i++){
            try {
                nearbyPlaceMap = getSingleNearbyPlace((JSONObject) jsonArray.get(i));
                nearbyPlacesList.add(nearbyPlaceMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return nearbyPlacesList;
    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;


        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllNearbyPlaces(jsonArray);
    }
}
