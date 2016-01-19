package md.speedy.developer.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.speedy.developer.helpers.DBManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Eduard Albu on 16.01.16 01 2016
 * project Speedy Backend
 */
public class Place {
    private String logo;
    private String name;
    private boolean isInFavorites;
    private double rate;
    private int commentsCounter;
    private int galleryCounter;
    private String description;
    private String placeId;
    private int userRate;
    private Address address;
    private Contacts contacts;
    private ArrayList<Comments> comments;
    private ArrayList<Image> images;

    public Address getAddress() {
        return address;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public ArrayList<Comments> getComments() {
        return comments;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public double getRate() {
        return rate;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public boolean isInFavorites() {
        return isInFavorites;
    }

    public int getCommentsCounter() {
        return commentsCounter;
    }

    public int getGalleryCounter() {
        return galleryCounter;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public int getUserRate() {
        return userRate;
    }


    /**
     * Will write a json array from a Places array only with data for the mai page
     */
    public static JSONArray writeShortPlace(ArrayList<Place> places) {
        JSONArray placeArray = new JSONArray();
        try {
            for (Place place : places) {
                JSONObject object = new JSONObject();
                object.put("name", place.getName());
                object.put("commentsCounter", place.getCommentsCounter());
                object.put("isInFavorites", place.isInFavorites());
                object.put("rate", place.getRate());
                object.put("placeId", place.getPlaceId());
                object.put("logo", place.getLogo());
                object.put("phone", place.getContacts().getPhone());
                placeArray.put(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            placeArray.put(Arrays.toString(e.getStackTrace())).put("error: " + e.toString());
        }
        return placeArray;
    }


    /**
     * Will write a Place with all variables from Place class
     */
    public static JSONObject writeDetailed(Place place) {
        try {
            String jsonString = new ObjectMapper().writeValueAsString(place);
            return new JSONObject(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject().put("Status", false);
        }
    }


    /**
     * Will write a json array from a Places array only with data for search results
     */
    public static JSONObject writeSearchResult(Place place) {
        JSONObject object = new JSONObject();
        try {
            object.put("street", place.getAddress().getStreet());
            object.put("building", place.getAddress().getBuilding());
            object.put("city", place.getAddress().getCity());
            object.put("country", place.getAddress().getCountry());
            object.put("name", place.getName());
            object.put("placeId", place.getPlaceId());
            object.put("logo", place.getLogo());
            object.put("phone", place.getContacts().getPhone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static class Builder {
        private String logo;
        private String name;
        private boolean isInFavorites;
        private double rate;
        private int commentsCounter;
        private int galleryCounter;
        private String description;
        private String placeId;
        private int userRate;
        private Address address;
        private Contacts contacts;
        private ArrayList<Comments> comments;
        private ArrayList<Image> images;
        private ArrayList<Builder> builders;

        /**
         * Collect a place from database
         *
         * @param placeId for which place to get the data
         * @param userId  to check if place is in favorites and other stuff
         */
        public Builder getPlaceDataFromDB(String placeId, String userId) {
            String query = "select * from places where place_id=\"" + placeId + "\";";
            ResultSet resultSet = DBManager.getInstance().query(query);
            try {
                while (resultSet.next()) {
                    this.logo = resultSet.getString("logo");
                    this.name = resultSet.getString("name");
                    this.commentsCounter = resultSet.getInt("comments");
                    this.description = resultSet.getString("description");
                    this.placeId = resultSet.getString("place_id");
                    this.rate = resultSet.getDouble("rate");
                }
                // TODO: update user history (save visited place to history table)
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            addMoreData(this, userId);
            return this;
        }

        /**
         * Get a specified number of places from database
         *
         * @param offset where to start
         * @param limit  the results number
         * @param userId to check favorites
         */
        public ArrayList<Place> getPlacesFromDB(int offset, int limit, String userId) {
            String query = "select * from places order by rate desc limit " + offset + "," + limit + ";";
            ResultSet resultSet = DBManager.getInstance().query(query);
            try {
                ArrayList<Builder> builders = setCommonData(resultSet);
                for (Builder builder : builders) {
                    addMoreData(builder, userId);
                }
                return buildShort(builders);
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }


        /**
         * Add common data to given builder
         */
        private ArrayList<Builder> setCommonData(ResultSet resultSet) {
            ArrayList<Builder> builders = new ArrayList<>();
            try {
                while (resultSet.next()) {
                    Builder builder = new Builder();
                    builder.logo = resultSet.getString("logo");
                    builder.name = resultSet.getString("name");
                    builder.commentsCounter = resultSet.getInt("comments");
                    builder.description = resultSet.getString("description");
                    builder.placeId = resultSet.getString("place_id");
                    builder.rate = resultSet.getDouble("rate");
                    builders.add(builder);
                }
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return builders;
        }


        /**
         * Add data which is specified in other classes
         */
        private Builder addMoreData(Builder builder, String userId) {
            builder.contacts = new Contacts.Builder().getContactsFromDB(builder.placeId).build();
            builder.address = new Address.Builder().getPlaceAddress(builder.placeId).build();
            builder.contacts = new Contacts.Builder().getContactsFromDB(builder.placeId).build();
            builder.comments = new Comments.Builder().getCommentsFromDB(builder.placeId).build();
            builder.images = new Image.Builder().getImageGalleryFromDB(builder.placeId).build();
            builder.userRate = User.getUserRate(builder.placeId, userId);

            // check if the user added place to favorites
            String checkFavorites = "select * from favorites where place_id=\"" + builder.placeId + "\" and user_id=\"" + userId + "\";";
            builder.isInFavorites = DBManager.getInstance().exists(checkFavorites);
            return builder;
        }


        // build places for main page
        public ArrayList<Place> buildShort(ArrayList<Builder> builders) {
            ArrayList<Place> places = new ArrayList<>();
            for (Builder builder : builders) {
                Place place = new Place();
                place.logo = builder.logo;
                place.name = builder.name;
                place.rate = builder.rate;
                place.commentsCounter = builder.commentsCounter;
                place.isInFavorites = builder.isInFavorites;
                place.placeId = builder.placeId;
                place.contacts = builder.contacts;
                places.add(place);
            }
            return places;
        }

        // build a place to show the detailed place
        public Place buildDetailed() {
            Place place = new Place();
            place.logo = this.logo;
            place.name = this.name;
            place.commentsCounter = this.commentsCounter;
            place.description = this.description;
            place.placeId = this.placeId;
            place.rate = this.rate;
            place.address = this.address;
            place.contacts = this.contacts;

            // we need here just the last four comments
            ArrayList<Comments> fourComments = new ArrayList<>();
            for (Comments comment : this.comments) {
                if (fourComments.size() < 4) {
                    fourComments.add(comment);
                }
            }
            place.comments = fourComments;
            place.images = this.images;
            place.userRate = this.userRate;
            place.galleryCounter = this.images.size();
            place.isInFavorites = this.isInFavorites;
            return place;
        }


        // build a place for search results
        public Place buildSearchResult() {
            Place place = new Place();
            place.placeId = this.placeId;
            place.logo = this.logo;
            place.name = this.name;
            place.address = this.address;
            place.contacts = this.contacts;
            return place;
        }
    }
}
