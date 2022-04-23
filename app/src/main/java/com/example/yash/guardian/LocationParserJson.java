package com.example.yash.guardian;

import org.json.JSONException;
import org.json.JSONObject;

public class LocationParserJson {
    public static Location Locationparser(String data){
        Location location = new Location();

        try {
            JSONObject jsonObject = new JSONObject(data);
            location.setUID(jsonObject.getLong("UID"));
            location.setPhoneNumber(jsonObject.getLong("PhoneNumber"));
            location.setLongitude(jsonObject.getDouble("longitude"));
            location.setLatitude(jsonObject.getDouble("latitude"));
            return location;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
