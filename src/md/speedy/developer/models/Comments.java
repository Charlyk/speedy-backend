package md.speedy.developer.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.speedy.developer.helpers.DBManager;
import md.speedy.developer.helpers.StringEncoder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 16.01.16 01 2016
 * project Speedy Backend
 */
public class Comments {

    private String comment;
    private String date;
    private String author;
    private String authorImage;
    private String authorGender;

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public String getAuthorGender() {
        return authorGender;
    }

    public static JSONObject writeJSON(ArrayList<Comments> comments) {
        JSONObject response = new JSONObject();
        JSONArray commentsArray = new JSONArray();
        try {
            for (Comments comment : comments) {
                JSONObject object = new JSONObject();
                object.put("comment", StringEncoder.decodeString(comment.getComment()));
                object.put("date", comment.getDate());
                object.put("author", StringEncoder.decodeString(comment.getAuthor()));
                object.put("authorImage", comment.getAuthorImage());
                object.put("authorGender", comment.getAuthorGender());
                commentsArray.put(object);
            }
            response.put("Status", true).put("ResponseData", commentsArray);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("Status", false).put("Error", e.getCause());
        }
        return response;
    }

    public JSONObject addComment(JSONObject income) {
        JSONObject response = new JSONObject();
        String comment = StringEncoder.decodeString(income.getString("comment"));
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
                    response.put("Status", false).put("Error", e.getCause());
                    e.printStackTrace();
                }
                counter++;
                String updateCounter = "update places set comments=" + counter + " where place_id=\"" + placeId + "\";";
                DBManager.getInstance().update(updateCounter);
                response.put("Status", true).put("ResponseData", "Comment added successfully");
            } else {
                response.put("Status", false).put("Error", "Can\'t add your comment, try again later");
            }
        } else {
            response.put("Status", false).put("Error", "Comment already exists");
        }
        String updateUnread = "update favorites set unread=true where place_id=\"" + placeId + "\";";
        DBManager.getInstance().update(updateUnread);
        String updateAuthorUnread = "update favorites set unread=false where place_id=\"" + placeId + "\" and user_id=\"" + userId + "\";";
        DBManager.getInstance().update(updateAuthorUnread);
        return response;
    }

    public static class Builder {
        private String comment;
        private String date;
        private String author;
        private String authorImage;
        private String authorGender;
        private ArrayList<Comments> comments;

        public Builder getCommentsFromDB(String placeId) {
            comments = new ArrayList<>();
            String query = "select c.comment, c.date, u.name, u.photo, u.gender from comments c, users u where c.place_id=\""
                    + placeId + "\" and c.user_id=u.user_id order by date desc;";
            ResultSet resultSet = DBManager.getInstance().query(query);
            try {
                while (resultSet.next()) {
                    this.comment = StringEncoder.decodeString(resultSet.getString("comment"));
                    this.date = resultSet.getString("date");
                    this.author = resultSet.getString("name");
                    this.authorImage = resultSet.getString("photo");
                    this.authorGender = resultSet.getString("gender");
                    comments.add(build(this));
                }
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public ArrayList<Comments> build() {
            return comments;
        }

        private Comments build(Builder builder) {
            Comments comments = new Comments();
            comments.comment = builder.comment;
            comments.date = builder.date;
            comments.author = builder.author;
            comments.authorImage = builder.authorImage;
            comments.authorGender = this.authorGender;
            return comments;
        }
    }
}
