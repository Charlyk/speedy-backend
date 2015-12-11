package md.speedy.developer.model;

import md.speedy.developer.helpers.DBManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;

/**
 * Created by Eduard Albu on 12/11/15.
 * Speedy-API project
 */
public class Comment {

    private JSONObject mResponseObject;

    public Comment() {
        mResponseObject = new JSONObject();
    }

    public JSONArray build(String placeId) {
        String query = "select c.comment, c.date, u.name, u.photo from comments c, users u where c.place_id=\"" + placeId + "\" and c.user_id=u.user_id;";
        ResultSet set = DBManager.getInstance().query(query);
        JSONArray comments = new JSONArray();
        try {
            while (set.next()) {
                JSONObject object = new JSONObject();
                object.put("comment", set.getString("comment"));
                object.put("date", set.getString("date"));
                object.put("author", set.getString("name"));
                object.put("authorImage", set.getString("photo"));
                comments.put(object);
            }
        } catch (Exception e) {
            mResponseObject.put("Status", false);
            mResponseObject.put("Error", e.getMessage());
            comments.put(mResponseObject);
        }
        return comments;
    }
}
