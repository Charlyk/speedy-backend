package md.speedy.developer.model;

import md.speedy.developer.helpers.DBManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 12/11/15.
 * Speedy-API project
 */
public class Favorites {

    private JSONObject mResponseObject;

    public Favorites() {
        mResponseObject = new JSONObject();
    }

    public JSONObject addToFavorites(String userId, String placeId) {
        String query = "insert into favorites (place_id, user_id) values (\"" + placeId + "\", \"" + userId + "\");";
        String check = "select * from favorites where user_id=\""+ userId + "\";";
        ResultSet set = DBManager.getInstance().query(check);
        try {
            boolean exist = false;
            while (set.next()) {
                if (set.getString("user_id").equalsIgnoreCase(userId) && set.getString("place_id").equalsIgnoreCase(placeId)) {
                    exist = true;
                    mResponseObject.put("Error", "Already exists");
                    mResponseObject.put("Status", false);
                }
            }
            set.close();
            if (!exist) {
                DBManager.getInstance().update(query);
                mResponseObject.put("Error", "Added successfully");
                mResponseObject.put("Status", true);
            }
        } catch (Exception e) {
            mResponseObject.put("Error", e.getMessage());
            mResponseObject.put("Status", false);
        }
        return mResponseObject;
    }

    public JSONObject deleteFromFavorites(String userId, String placeId) {
        String query = "delete from favorites where user_id=\"" + userId + "\" and place_id=\"" + placeId + "\";";
        int i = DBManager.getInstance().update(query);
        if (i >= 1) {
            mResponseObject.put("Error", "Deleted successfully");
            mResponseObject.put("Status", true);
        } else {
            mResponseObject.put("Error", "Something went wrong");
            mResponseObject.put("Status", false);
        }
        return mResponseObject;
    }

    public JSONObject build(String id) {
        String query = "select * from favorites where user_id=\"" + id + "\";";
        ResultSet resultSet = DBManager.getInstance().query(query);
        try {
            JSONArray p = new JSONArray();
            ArrayList<String> places = new ArrayList<>();
            while (resultSet.next()) {
                places.add(resultSet.getString("place_id"));
            }
            resultSet.close();
            for (String pl : places) {
                getPlaces(pl, p);
            }
            mResponseObject.put("ResponseData", p);
            mResponseObject.put("Status", true);
        } catch (Exception e) {
            mResponseObject.put("Error", e.getMessage());
            mResponseObject.put("Status", false);
            e.printStackTrace();
        }
        return mResponseObject;
    }

    private void getPlaces(String placeId, JSONArray p) {
        String query = "select * from places where place_id=\"" + placeId + "\"";
        ResultSet places = DBManager.getInstance().query(query);
        JSONObject object = new JSONObject();
        try {
            while (places.next()) {
                object.put("name", places.getString("name"));
                object.put("placeId", places.getString("place_id"));
                p.put(object);
            }
            places.close();
        } catch (Exception e) {
            mResponseObject.put("Error", e.getMessage());
            mResponseObject.put("Status", false);
            e.printStackTrace();
        }
    }

    public int getUnread(String userId) {
        String query = "select unread from favorites where user_id=\"" + userId + "\"";
        ResultSet set = DBManager.getInstance().query(query);
        ArrayList<Boolean> unread = new ArrayList<>();
        try {
            while (set.next()) {
                unread.add(set.getBoolean("unread"));
            }
            set.close();
        } catch (Exception e) {
            return 0;
        }
        return unread.size();
    }
}
