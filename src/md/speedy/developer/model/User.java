package md.speedy.developer.model;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 12/7/15.
 * Speedy-API project
 */
public class User {

    private String name;
    private String photo;
    private String email;
    private String id;
    private String coverId;
    private String coverUrl;
    private String gender;

    public User(JSONObject income) {
        setName(income.getString("name"));
        setEmail(income.getString("email"));
        setPhoto(income.getString("photo"));
        setId(income.getString("id"));
        setCoverId(income.getString("coverId"));
        setCoverUrl(income.getString("coverUrl"));
        setGender(income.optString("gender", "unknown"));
    }

    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String avatar) {
        this.photo = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public JSONObject getObject(ResultSet resultSet) {
        JSONObject user = new JSONObject();
        try {
            while (resultSet.next()) {
                user.put("name", resultSet.getString("name"));
                user.put("photo", resultSet.getString("photo"));
                user.put("email", resultSet.getString("email"));
                user.put("id", resultSet.getString("user_id"));
                user.put("coverId", resultSet.getString("cover_id"));
                user.put("coverUrl", resultSet.getString("cover_url"));
                user.put("gender", resultSet.getString("gender"));
            }
            resultSet.close();
            Favorites favorites = new Favorites();
            user.put("unreadFavorites", favorites.getUnread(user.getString("id")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public ArrayList<JSONObject> getObjects(ResultSet resultSet) {
        ArrayList<JSONObject> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                JSONObject user = new JSONObject();
                user.put("id", resultSet.getLong("user_id"));
                user.put("photo", resultSet.getString("photo"));
                user.put("name", resultSet.getString("name"));
                user.put("online", resultSet.getBoolean("is_online"));
                users.add(user);
            }
            resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
