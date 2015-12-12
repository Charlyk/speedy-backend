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

    public JSONArray getFourComments(String placeId) {
        String query = "select c.comment, c.date, u.name, u.photo from comments c, users u where c.place_id=\"" + placeId + "\" and c.user_id=u.user_id limit 0,3;";
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
            mResponseObject.put("Error", e.getMessage());
            comments.put(mResponseObject);
        }
        return comments;
    }

    public JSONObject addComment(JSONObject income) {
        String comment = income.getString("comment");
        String userId = income.getString("userId");
        String placeId = income.getString("placeId");
        String query = "insert into comments (comment, place_id, user_id) values (\"" + comment + "\", \""
                + placeId + "\", \"" + userId + "\");";
        if (!DBManager.getInstance().exists("comments", "comment", comment)) {
            int rows = DBManager.getInstance().update(query);
            if (rows > 0) {
                mResponseObject.put("Status", true).put("ResponseData", "Comment added successfully");
            } else {
                mResponseObject.put("Status", false).put("ResponseData", "Something went wrong, please try again");
            }
        } else {
            mResponseObject.put("Status", false).put("ResponseData", "Comment already exists");
        }
        String updateUnread = "update favorites set unread=true where place_id=\"" + placeId + "\";";
        DBManager.getInstance().update(updateUnread);
        String updateAuthorUnread = "update favorites set unread=false where place_id=\"" + placeId + "\" and user_id=\"" + userId + "\";";
        DBManager.getInstance().update(updateAuthorUnread);
        return mResponseObject;
    }
}
