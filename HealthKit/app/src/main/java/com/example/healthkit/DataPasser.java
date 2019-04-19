package com.example.healthkit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataPasser {

    private HashMap <String,String> getSinglePlaces(JSONObject googlePlaceJSON)
    {
        HashMap<String,String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String Vicinity = "-NA-";
        String latitude="";
        String longitude="";
        String reference="";

        try
        {
            if(!googlePlaceJSON.isNull("name"))
            {
                NameOfPlace = googlePlaceJSON.getString("name");
            }
            if(!googlePlaceJSON.isNull("vicinity"))
            {
                Vicinity = googlePlaceJSON.getString("vicinity");
            }
            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJSON.getString("name");
            googlePlaceMap.put("place_name",NameOfPlace);
            googlePlaceMap.put("vicinity",Vicinity);
            googlePlaceMap.put("lat",latitude);
            googlePlaceMap.put("lng",longitude);
            googlePlaceMap.put("reference",reference);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return googlePlaceMap;
    }

    private List<HashMap<String,String>> getAllPlaces (JSONArray jsonArray)
    {
        int counter = jsonArray.length();
        List<HashMap<String,String>> NearbyPlacesList =new ArrayList<>();
        HashMap<String,String>NearbyplaceMap = null;
        for(int i=0;i<counter;i++)
        {
            try
            {
                NearbyplaceMap = getSinglePlaces((JSONObject) jsonArray.get(i));
                NearbyPlacesList.add(NearbyplaceMap);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return NearbyPlacesList;
    }

    public List<HashMap<String,String >> parse(String jsonData)
    {
        JSONArray jsonArray =null;
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return  getAllPlaces(jsonArray);
    }
}
