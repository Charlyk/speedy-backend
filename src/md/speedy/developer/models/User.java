package md.speedy.developer.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.speedy.developer.helpers.DBManager;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;

/**
 * Created by Eduard Albu on 16.01.16 01 2016
 * project Speedy Backend
 */
public class User {

    private String name;
    private String email;
    private String photo;
    private String id;
    private String gender;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public static int getUserRate(String placeId, String userId) {
        String query = "select rate from rates where place_id=\"" + placeId + "\" and user_id=\"" + userId + "\";";
        ResultSet resultSet = DBManager.getInstance().query(query);
        try {
            resultSet.next();
            return resultSet.getInt("rate");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static JSONObject writeJSON(User user) {
        JSONObject object;
        try {
            String userJSONString = new ObjectMapper().writeValueAsString(user);
            object = new JSONObject(userJSONString);
        } catch (IOException e) {
            e.printStackTrace();
            object = new JSONObject().put("Status", false);
        }
        return object;
    }

    public static class Builder {
        private String name;
        private String email;
        private String photo;
        private String id;
        private String gender;

        public Builder getUserFromDB(String userId) {
            String query = "select * from users where user_id=\"" + userId + "\";";
            ResultSet resultSet = DBManager.getInstance().query(query);
            try {
                while (resultSet.next()) {
                    this.name = resultSet.getString("name");
                    this.email = resultSet.getString("email");
                    this.photo = resultSet.getString("photo");
                    this.id = resultSet.getString("user_id");
                    this.gender = resultSet.getString("gender");
                }
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public User build() {
            User user = new User();
            user.name = this.name;
            user.email = this.email;
            user.photo = this.photo;
            user.id = this.id;
            user.gender = this.gender;
            return user;
        }
    }
}
