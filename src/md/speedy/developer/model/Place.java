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

    public JSONObject build(int offset, int limit) {
        String query = "select name, logo, rate, place_id, phone, comments from places limit " + offset + "," + limit + ";";
        JSONArray places = new JSONArray();
        ResultSet set = DBManager.getInstance().query(query);
        try {
            while (set.next()) {
                JSONObject place = new JSONObject();
                place.put("name", set.getString("name"));
                place.put("logo", set.getString("logo"));
                place.put("rate", set.getDouble("rate"));
                place.put("phone", set.getString("phone"));
                place.put("id", set.getString("place_id"));
                place.put("comments", set.getInt("comments"));
                places.put(place);
            }
            mResponseObject.put("Status", true);
            mResponseObject.put("ResponseData", places);
        } catch (Exception e) {
            mResponseObject.put("Status", false);
            mResponseObject.put("Error", "Error while getting places");
        }
        return mResponseObject;
    }

    public JSONObject getDetailedPlace(String placeId, String userId) {
        String query = "select * from places where place_id=\"" + placeId + "\";";
        JSONObject place = new JSONObject();
        JSONObject response = new JSONObject();
        ResultSet set = DBManager.getInstance().query(query);
        try {
            while (set.next()) {
                place.put("name", set.getString("name"));
                place.put("description", set.getString("description"));
                place.put("street", set.getString("street"));
                place.put("city", set.getString("city"));
                place.put("country", set.getString("country"));
                place.put("phone", set.getString("phone"));
                place.put("email", set.getString("email"));
                place.put("logo", set.getString("logo"));
                place.put("rate", set.getDouble("rate"));
                place.put("id", set.getString("place_id"));
            }
            History history = new History();
            history.addToHistory(placeId, userId);
            Comment comment = new Comment();
            Rate rate = new Rate();
            response.put("place", place).put("comments", comment.getFourComments(placeId))
                    .put("photos", getImageGallery(placeId))
                    .put("currentUserRate", rate.getCuerrentUserRate(userId, placeId));
            mResponseObject.put("ResponseData", response);
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
            mResponseObject.put("Status", true).put("ResponseData", places);
        } catch (Exception e) {
            mResponseObject.put("Status", false).put("Error", "Error while getting the top places");
        }
        return mResponseObject;
    }
}
