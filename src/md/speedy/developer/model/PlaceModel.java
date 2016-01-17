package md.speedy.developer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Eduard Albu on 16.01.16 01 2016
 * project Speedy Backend
 */
public class PlaceModel {

    private String name;
    private String description;

    private String phone;
    private String email;
    private String site;

    private String logoUrl;
    private double rate;
    private String id;

    private String street;
    private String city;
    private String country;
    private String buildingNumber;

    private int comments;
    private int userRate;
    private int galleryCount;
    private boolean isInFavorites;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStreet() {
        return street;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public double getRate() {
        return rate;
    }

    public String getId() {
        return id;
    }

    public int getComments() {
        return comments;
    }

    public String getSite() {
        return site;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public int getUserRate() {
        return userRate;
    }

    public int getGalleryCount() {
        return galleryCount;
    }

    public boolean isInFavorites() {
        return isInFavorites;
    }

    public static class Builder {
        private String name;
        private String description;
        private String street;
        private String phone;
        private String email;
        private String logoUrl;
        private double rate;
        private String id;
        private int comments;
        private String site;
        private String city;
        private String country;
        private String buildingNumber;
        private int userRate;
        private int galleryCount;
        private boolean isInFavorites;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public Builder rate(double rate) {
            this.rate = rate;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder comments(int comments) {
            this.comments = comments;
            return this;
        }

        public Builder site(String site) {
            this.site = site;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder userRate(int userRate) {
            this.userRate = userRate;
            return this;
        }

        public Builder galleryCount(int galleryCount) {
            this.galleryCount = galleryCount;
            return this;
        }

        public Builder isInFavorites(boolean isInFavorites) {
            this.isInFavorites = isInFavorites;
            return this;
        }

        public Builder buildingNumber(String buildingNumber) {
            this.buildingNumber = buildingNumber;
            return this;
        }

        public PlaceModel build() {
            PlaceModel placeModel = new PlaceModel();
            placeModel.name = this.name;
            placeModel.description = this.description;
            placeModel.street = this.street;
            placeModel.phone = this.phone;
            placeModel.email = this.email;
            placeModel.logoUrl = this.logoUrl;
            placeModel.rate = this.rate;
            placeModel.id = this.id;
            placeModel.comments = this.comments;
            placeModel.site = this.site;
            placeModel.city = this.city;
            placeModel.country = this.country;
            placeModel.buildingNumber = this.buildingNumber;
            placeModel.userRate = this.userRate;
            placeModel.galleryCount = this.galleryCount;
            placeModel.isInFavorites = this.isInFavorites;
            return placeModel;
        }
    }

    public static JSONObject writeJSON(PlaceModel place) {
        try {
            String jsonAsString = objectMapper.writeValueAsString(place);
            return new JSONObject(jsonAsString);
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject().put("Status", false).put("Error", "Error writing place " + place.getName());
        }
    }
}
