package com.example.yash.guardian;

public class FunctionURLs {

    public static final String localhost = "192.168.43.113";

    public static String getGETrequestURL(long UID, long PhoneNumber){
        String getURL = "http://"+localhost+":8080/GuardianWS/gws/location?pnum="+PhoneNumber+"&uid="+UID;
        return getURL;
    }

    public static String getPOSTrequestBody(long UID,long PhoneNumber ,double Longitude, double Latitude){
        String postURL = "{\"UID\":"+UID+",\"latitude\":"+Latitude+",\"longitude\":"+Longitude+",\"phoneNumber\":"+PhoneNumber+"}";
        return postURL;
    }

    public static String getPUTrequestBody(long UID,long PhoneNumber ,double Longitude, double Latitude){
        String putURL = "{\"UID\":"+UID+",\"latitude\":"+Latitude+",\"longitude\":"+Longitude+",\"phoneNumber\":"+PhoneNumber+"}";
        return putURL;
    }

    public static String getDELETErequestBody(long PhoneNumber){
        String deleteURL = "http://"+localhost+":8080/GuardianWS/gws/location/"+PhoneNumber;
        return deleteURL;
    }
}
