package md.speedy.developer.model;

import md.speedy.developer.helpers.DBManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

/**
 * Created by Eduard Albu on 12.12.15 12 2015
 * project Speedy Backend
 */
public class History {

    private JSONObject mResponseObject;

    public History() {
        mResponseObject = new JSONObject();
    }

    public void addToHistory(String placeId, String userId) {
        String query = "insert into histories (user_id, place_id) values (\"" + userId + "\", \"" + placeId + "\");";
        String checkQuery = "select id from histories where user_id=\"" + userId + "\" and place_id=\"" + placeId + "\";";
        if (!DBManager.getInstance().exists(checkQuery)) {
            DBManager.getInstance().update(query);
        }
    }

    public JSONObject getHistory(String userId, int offset, int limit) {
        String query = "select p.name, p.description, p.rate, p.logo from places p, histories h " +
                "where p.place_id=h.place_id and h.user_id=\"" + userId + "\" limit " + offset + "," + limit + ";";
        ResultSet set = DBManager.getInstance().query(query);
        try {
            JSONArray histories = new JSONArray();
            while (set.next()) {
                JSONObject object = new JSONObject();
                object.put("name", set.getString("name"));
                object.put("description", set.getString("description"));
                object.put("rate", set.getDouble("rate"));
                object.put("photo", set.getString("logo"));
                histories.put(object);
            }
            mResponseObject.put("Status", true).put("ResponseData", histories);
        } catch (Exception e) {
            mResponseObject.put("Status", false).put("Error", e.getCause());
        }
        return mResponseObject;
    }
}
