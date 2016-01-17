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

    /**
     * Get all comments for given placeId
     *
     * @param placeId for which place to get comments
     * @return JSONObject with all founded comments
     */
    public JSONObject build(String placeId) {
        String query = "select c.comment, c.date, u.name, u.photo from comments c, users u where c.place_id=\""
                + placeId + "\" and c.user_id=u.user_id order by date desc;";
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
            set.close();
            mResponseObject.put("Status", true);
            mResponseObject.put("ResponseData", comments);
        } catch (Exception e) {
            mResponseObject.put("Status", false);
            mResponseObject.put("Error", "Error getting comments");
        }
        return mResponseObject;
    }


    /**
     * Gets the last two comments
     *
     * @param placeId for which place to get comments
     * @return JSONObject with last four comments (or less)
     */
    public JSONArray getFourComments(String placeId) {
        String query = "select c.comment, c.date, u.name, u.photo, u.gender from comments c, users u where c.place_id=\""
                + placeId + "\" and c.user_id=u.user_id order by date desc;";
        ResultSet set = DBManager.getInstance().query(query);
        JSONArray comments = new JSONArray();
        JSONArray lastFour = new JSONArray();
        try {
            while (set.next()) {
                JSONObject object = new JSONObject();
                object.put("comment", set.getString("comment"));
                object.put("date", set.getString("date"));
                object.put("author", set.getString("name"));
                object.put("authorImage", set.getString("photo"));
                object.put("authorGender", set.getString("gender"));
                comments.put(object);
            }
            set.close();
            for (int i = 0; i < comments.length(); i++) {
                if (i < 4) {
                    lastFour.put(comments.get(i));
                }
            }
        } catch (Exception e) {
            mResponseObject.put("Error", e.getMessage());
            lastFour.put(mResponseObject);
        }
        return lastFour;
    }


    /**
     * Add a coment to database
     * @param income the user request, it must contain the comment, placeId, userId and the date
     * @return JSONObject with comment status
     */
    public JSONObject addComment(JSONObject income) {
        String comment = income.getString("comment");
        String userId = income.getString("userId");
        String placeId = income.getString("placeId");
        String date = income.getString("date");
        String query = "insert into comments (comment, place_id, user_id, date) values (\"" + comment + "\", \""
                + placeId + "\", \"" + userId + "\", \"" + date + "\");";
        if (!DBManager.getInstance().exists("comments", "comment", comment)) {
            int rows = DBManager.getInstance().update(query);
            if (rows > 0) {
                String getCommentsCounter = "select comments from places where place_id=\"" + placeId + "\";";
                ResultSet set = DBManager.getInstance().query(getCommentsCounter);
                int counter = 0;
                try {
                    while (set.next()) {
                        counter = set.getInt("comments");
                    }
                    set.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                counter++;
                String updateCounter = "update places set comments=" + counter + " where place_id=\"" + placeId + "\";";
                DBManager.getInstance().update(updateCounter);
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
