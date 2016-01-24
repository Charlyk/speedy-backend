package md.speedy.developer.models;

import md.speedy.developer.helpers.DBManager;
import md.speedy.developer.helpers.DistanceCalculator;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Eduard Albu on 24.01.16 01 2016
 * project Speedy Backend
 */
public class NearbyPlaces extends Place {

    private double latitude;
    private double longitude;
    private double userLatitude;
    private double userLongitude;
    private double distance;
    private String userId;
    private Builder builder;

    private NearbyPlaces() {

    }

    public NearbyPlaces(double latitude, double longitude, String userId) {
        this.userLatitude = latitude;
        this.userLongitude = longitude;
        this.builder = new Builder();
        this.userId = userId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Will write a json array from a Places array only with data for the mai page
     */
    public static JSONArray writePlace(ArrayList<NearbyPlaces> places) {
        JSONArray placeArray = new JSONArray();
        try {
            for (NearbyPlaces place : places) {
                JSONObject object = new JSONObject();
                object.put("name", place.getName());
                object.put("commentsCounter", place.getCommentsCounter());
                object.put("isInFavorites", place.isInFavorites());
                object.put("rate", place.getRate());
                object.put("placeId", place.getPlaceId());
                object.put("logo", place.getLogo());
                object.put("phone", place.getContacts().getPhone());
                object.put("distance", place.getDistance());
                placeArray.put(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            placeArray.put(Arrays.toString(e.getStackTrace())).put("error: " + e.toString());
        }
        return placeArray;
    }

    public ArrayList<NearbyPlaces> getPlaces() {
        String query = "select place_id, latitude, longitude, from places";
        ArrayList<NearbyPlaces> nearbyPlaces = new ArrayList<>();
        ResultSet set = DBManager.getInstance().query(query);
        try {
            while (set.next()) {
                NearbyPlaces nearbyPlace = new NearbyPlaces();
                nearbyPlace.setLatitude(set.getDouble("latitude"));
                nearbyPlace.setLongitude(set.getDouble("longitude"));
                nearbyPlace.setPlaceId(set.getString("place_id"));
                nearbyPlaces.add(nearbyPlace);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getNearby(nearbyPlaces);
    }

    private ArrayList<NearbyPlaces> getNearby(ArrayList<NearbyPlaces> places) {
        ArrayList<NearbyPlaces> nearbyPlaces = new ArrayList<>();
        for (NearbyPlaces place : places) {
            double distance = DistanceCalculator.distance(this.userLatitude, this.userLongitude,
                    place.getLatitude(), place.getLongitude(), "K");
            if (distance < 3) {
                NearbyPlaces nearbyPlace = (NearbyPlaces) builder.getPlaceDataFromDB(place.getPlaceId(), userId).buildSearchResult();
                nearbyPlace.setDistance(distance);
                nearbyPlaces.add(nearbyPlace);
            }
        }
        return nearbyPlaces;
    }
}
