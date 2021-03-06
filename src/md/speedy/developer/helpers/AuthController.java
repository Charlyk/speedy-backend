package md.speedy.developer.helpers;

import md.speedy.developer.model.User;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 * @author eduard.albu@gmail.com
 */
public class AuthController {

    private static JSONObject out = new JSONObject();

    public static boolean isUserRegistered(JSONObject income) {
        String id = income.getString("id");
        boolean registered = DBManager.getInstance().exists("users", "user_id", id);
        if (registered) {
            out.put("Status", true);
            out.put("NewUser", false);
        } else {
            out.put("NewUser", true);
        }
        out.put("id", id);
        return registered;
    }

    public static boolean exists(String userId) {
        String query = "select user_id from users where user_id=\"" + userId + "\";";
        ResultSet set = DBManager.getInstance().query(query);
        try {
            while (set.next()) {
                if (userId.equalsIgnoreCase(set.getString("user_id"))) {
                    return true;
                }
            }
            set.close();
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
        return false;
    }

    public static JSONObject getUserData(JSONObject income) {
        JSONObject outgoing = new JSONObject();
        String query = "select * from users u where user_id=\"" + income.getString("id") + "\";";
        ResultSet resultSet = DBManager.getInstance().query(query);
        outgoing.put("ResponseData", new User().getObject(resultSet));
        outgoing.put("Status", true);
        return outgoing;
    }

    public static void registerUser(JSONObject income) {
        User user = new User(income);
        String parsedName = "";
        try {
            byte[] nameBytes = user.getName().getBytes("ISO-8859-1");
            parsedName = new String(nameBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String query = "insert into users (name, photo, email, user_id, cover_id, gender, cover_url) values (\""
                + parsedName + "\", \""
                + user.getPhoto() + "\", \""
                + user.getEmail() + "\", \""
                + user.getId() + "\", \""
                + user.getCoverId() + "\", \""
                + user.getGender() + "\", \""
                + user.getCoverUrl() + "\");";
        DBManager.getInstance().update(query);
        out.put("Status", true);
    }

    public static JSONObject getUnregisteredErrorMessage() {
        return new JSONObject().put("Status", false).put("Error", "User is not registered");
    }

    public static JSONObject getAuthResponse() {
        return out;
    }
}
