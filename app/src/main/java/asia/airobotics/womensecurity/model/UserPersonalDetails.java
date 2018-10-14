package asia.airobotics.womensecurity.model;

import android.location.Location;

public class UserPersonalDetails {

    private String Name;
    private String phoneNumber;
    private String City;
    private static String Emergency_phoneNumber;
    private static Location currentLocation;
    public static boolean isMessageSend;
    public static String currentCityName;
    public static String currentRoadName;
    public static String mapsUrl;

    public static String getMapsUrl() {
        return mapsUrl;
    }

    public static void setMapsUrl(String mapsUrl) {
        UserPersonalDetails.mapsUrl = mapsUrl;
    }

    public static String getCurrentCityName() {
        return currentCityName;
    }

    public static void setCurrentCityName(String currentCityName) {
        UserPersonalDetails.currentCityName = currentCityName;
    }

    public static void setCurrentLocation(Location currentLocation) {
        UserPersonalDetails.currentLocation = currentLocation;
    }

    public static String getCurrentRoadName() {
        return currentRoadName;
    }

    public static void setCurrentRoadName(String currentRoadName) {
        UserPersonalDetails.currentRoadName = currentRoadName;
    }

    public UserPersonalDetails(String name, String phoneNumber, String city, String emergency_phoneNumber) {
        Name = name;
        this.phoneNumber = phoneNumber;
        City = city;
        Emergency_phoneNumber = emergency_phoneNumber;
    }

    public UserPersonalDetails() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public static String getEmergency_phoneNumber() {
        return Emergency_phoneNumber;
    }

    public void setEmergency_phoneNumber(String emergency_phoneNumber) {
        Emergency_phoneNumber = emergency_phoneNumber;
    }
}
