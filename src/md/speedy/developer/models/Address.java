package md.speedy.developer.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.speedy.developer.helpers.DBManager;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;

/**
 * Created by Eduard Albu on 16.01.16 01 2016
 * project Speedy Backend
 */
public class Address {

    private String street;
    private String building;
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getStreet() {
        return street;
    }

    public String getBuilding() {
        return building;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public static JSONObject writeJSON(Address address) {
        try {
            String strJson = new ObjectMapper().writeValueAsString(address);
            return new JSONObject(strJson);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("Status", false);
        }
    }

    public static class Builder {
        private String street;
        private String building;
        private String city;
        private String country;
        private double latitude;
        private double longitude;

        public Builder getPlaceAddress(String placeId) {
            // create a database query to get the address from place table
            String query = "select street, building_number, city, latitude, longitude, country from places where place_id=\"" + placeId + "\";";
            ResultSet resultSet = DBManager.getInstance().query(query);
            try {
                while (resultSet.next()) {
                    this.street = resultSet.getString("street");
                    this.building = resultSet.getString("building_number");
                    this.city = resultSet.getString("city");
                    this.country = resultSet.getString("country");
                    this.latitude = resultSet.getDouble("latitude");
                    this.longitude = resultSet.getDouble("longitude");
                }
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Address build() {
            Address address = new Address();
            address.street = this.street;
            address.building = this.building;
            address.city = this.city;
            address.country = this.country;
            address.latitude = this.latitude;
            address.longitude = this.longitude;
            return address;
        }
    }
}
