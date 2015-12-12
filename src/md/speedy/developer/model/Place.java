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
        String query = "select name, description, logo, rate, place_id from places limit " + offset + "," + limit + ";";
        JSONArray places = new JSONArray();
        ResultSet set = DBManager.getInstance().query(query);
        try {
            while (set.next()) {
                JSONObject place = new JSONObject();
                place.put("name", set.getString("name"));
                place.put("description", set.getString("description"));
                place.put("logo", set.getString("logo"));
                place.put("rate", set.getDouble("rate"));
                place.put("id", set.getString("place_id"));
                places.put(place);
            }
            mResponseObject.put("Status", true);
            mResponseObject.put("ResponseData", places);
        } catch (Exception e) {
            mResponseObject.put("Status", false);
            mResponseObject.put("Error", e.getMessage());
        }
        return mResponseObject;
    }

    // TODO: 12/11/15 add query to include the current user rate in response
    public JSONObject getDetailedPlace(String placeId) {
        String query = "select * from places where place_id=\"" + placeId + "\";";
        JSONObject place = new JSONObject();
        JSONObject response = new JSONObject();
        ResultSet set = DBManager.getInstance().query(query);
        try {
            while (set.next()) {
                place.put("name", set.getString("name"));
                place.put("description", set.getString("description"));
                place.put("address", set.getString("address"));
                place.put("phone", set.getString("phone"));
                place.put("email", set.getString("email"));
                place.put("logo", set.getString("logo"));
                place.put("rate", set.getDouble("rate"));
                place.put("id", set.getString("place_id"));
            }
            Comment comment = new Comment();
            response.put("place", place).put("comments", comment.getFourComments(placeId)).put("photos", getImageGallery(placeId));
            mResponseObject.put("ResponseData", response);
            mResponseObject.put("Status", true);
        } catch (Exception e) {
            mResponseObject.put("Status", false);
            mResponseObject.put("Error", e.getMessage());
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
            mResponseObject.put("Error", e.getMessage());
        }
        return photos;
    }
}
