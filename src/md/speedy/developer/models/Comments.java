package md.speedy.developer.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.speedy.developer.helpers.DBManager;
import org.json.JSONArray;

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

    public static JSONArray writeJSON(ArrayList<Comments> comments) {
        JSONArray commentsArray = new JSONArray();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (Comments comment : comments) {
                commentsArray.put(mapper.writeValueAsString(comment));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commentsArray;
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
                    this.comment = resultSet.getString("comment");
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
