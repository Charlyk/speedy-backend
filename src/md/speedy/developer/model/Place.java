package md.speedy.developer.model;

import md.speedy.developer.helpers.DBManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

/**
 * Created by Eduard Albu on 12/11/15.
 * Speedy-API project
 */
public class Place {

    private JSONObject mResponseObject;

    public Place() {
        mResponseObject = new JSONObject();
    }

    public JSONObject build(int offset, int limit, String userId) {
        String query = "select name, logo, rate, place_id, phone, comments from places limit " + offset + "," + limit + ";";
        JSONArray places = new JSONArray();
        ResultSet set = DBManager.getInstance().query(query);
        try {
            while (set.next()) {
                JSONObject place = new JSONObject();
                String id = set.getString("place_id");
                place.put("name", set.getString("name"));
                place.put("logo", set.getString("logo"));
                place.put("rate", set.getDouble("rate"));
                place.put("phone", set.getString("phone"));
                place.put("id", id);
                place.put("comments", set.getInt("comments"));
                places.put(place);
            }
            set.close();
            JSONArray finalPlace = new JSONArray();
            for (int i = 0; i < places.length(); i++) {
                JSONObject object = places.getJSONObject(i);
                object.put("isInFavorites", isInFavorites(object.getString("id"), userId));
                finalPlace.put(object);
            }
            mResponseObject.put("Status", true);
            mResponseObject.put("ResponseData", finalPlace);
        } catch (Exception e) {
            mResponseObject.put("Status", false);
            mResponseObject.put("Error", "Error while getting places");
        }
        return mResponseObject;
    }

    public static JSONObject getPlace(String placeId) {
        String searchPlaces = "select name, place_id, phone, street, building_number, city, country, logo from places where place_id=\""
                + placeId + "\";";
        ResultSet set = DBManager.getInstance().query(searchPlaces);
        JSONObject place = new JSONObject();
        try {
            while (set.next()) {
                place.put("id", set.getString("place_id"));
                place.put("name", set.getString("name"));
                place.put("logo", set.getString("logo"));
                place.put("phone", set.getString("phone"));
                place.put("street", set.getString("street"));
                place.put("building", set.getString("building_number"));
                place.put("city", set.getString("city"));
                place.put("country", set.getString("country"));
            }
            set.close();
        } catch (Exception e) {
            e.printStackTrace();
            place.put("Error", e.getMessage() + " Place at id: " + placeId);
        }
        return place;
    }

    public boolean isInFavorites(String placeId, String userId) {
        String query = "select * from favorites where place_id=\"" + placeId + "\" and user_id=\"" + userId + "\";";
        return DBManager.getInstance().exists(query);
    }

    public JSONObject getDetailedPlace(String placeId, String userId) {
        String query = "select * from places where place_id=\"" + placeId + "\";";
        PlaceModel.Builder placeBuilder = new PlaceModel.Builder();
        ResultSet set = DBManager.getInstance().query(query);
        try {
            while (set.next()) {
                placeBuilder.name(set.getString("name"))
                        .description(set.getString("description"))
                        .logoUrl(set.getString("logo"))
                        .rate(set.getDouble("rate"))
                        .id(set.getString("place_id"))
                        .street(set.getString("street"))
                        .buildingNumber(set.getString("building_number"))
                        .city(set.getString("city"))
                        .country(set.getString("country"))
                        .phone(set.getString("phone"))
                        .email(set.getString("email"))
                        .site(set.getString("site"))
                        .comments(set.getInt("comments"))
                        .build();
            }
            set.close();
            placeBuilder.isInFavorites(isInFavorites(placeId, userId))
                    .galleryCount(getImageGallery(placeId).length())
                    .userRate(Rate.getCuerrentUserRate(userId, placeId));
            History.addToHistory(placeId, userId);
//            History history = new History();
//            history.addToHistory(placeId, userId);
//            Comment comment = new Comment();
//            Rate rate = new Rate();
//            place.put("currentUserRate", rate.getCuerrentUserRate(userId, placeId));
//            place.put("isInFavorites", isInFavorites(placeId, userId));
//            JSONArray images = getImageGallery(placeId);
//            JSONObject gallery = new JSONObject();
//            gallery.put("galleryCount", images.length())
//                    .put("images", images);
//            place.put("galleryCount", gallery.length());
//            response.put("place", place)
//                    .put("address", address)
//                    .put("contacts", contacts)
//                    .put("comments", comment.getFourComments(placeId))
//                    .put("photos", gallery);
            mResponseObject.put("ResponseData", PlaceModel.writeJSON(placeBuilder.build()));
            mResponseObject.put("Status", true);
        } catch (Exception e) {
            mResponseObject.put("Status", false);
            mResponseObject.put("Error", "Error getting the place for id " + placeId);
        }
        return mResponseObject;
    }

    private JSONArray getImageGallery(String placeId) {
        String query = "select path_to_photo from place_images where place_id=\"" + placeId + "\";";
        ResultSet set = DBManager.getInstance().query(query);
        JSONArray photos = new JSONArray();
        try {
            while (set.next()) {
                photos.put(set.getString("path_to_photo"));
            }
            set.close();
        } catch (Exception e) {
            mResponseObject.put("Status", false);
            mResponseObject.put("Error", "Error while getting the image gallery for place " + placeId);
        }
        return photos;
    }

    public JSONObject getTopPlaces() {
        String query = "select * from places where rate between 3.5 and 5;";
        ResultSet set = DBManager.getInstance().query(query);
        try {
            JSONArray places = new JSONArray();
            while (set.next()) {
                JSONObject place = new JSONObject();
                place.put("name", set.getString("name"));
                place.put("description", set.getString("description"));
                place.put("logo", set.getString("logo"));
                place.put("rate", set.getDouble("rate"));
                place.put("id", set.getString("place_id"));
                places.put(place);
            }
            set.close();
            mResponseObject.put("Status", true).put("ResponseData", places);
        } catch (Exception e) {
            mResponseObject.put("Status", false).put("Error", "Error while getting the top places");
        }
        return mResponseObject;
    }
}
