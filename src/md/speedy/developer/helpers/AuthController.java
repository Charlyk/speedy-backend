package md.speedy.developer.helpers;

import md.speedy.developer.model.User;
import org.json.JSONObject;

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
            out.put("NewUser", false);
        } else {
            out.put("NewUser", true);
        }
        out.put("id", id);
        return registered;
    }

    public static JSONObject getUserData(JSONObject income) {
        JSONObject outgoing = new JSONObject();
        String query = "select * from users where user_id=\"" + income.getString("id") + "\";";
        ResultSet resultSet = DBManager.getInstance().query(query);
        outgoing.put("ResponseData", new User().getObject(resultSet));
        outgoing.put("Status", true);
        return outgoing;
    }

    public static void registerUser(JSONObject income) {
        User user = new User(income);
        String query = "insert into users (name, photo, email, user_id, cover_id, cover_url) values (\""
                + user.getName() + "\", \""
                + user.getPhoto() + "\", \""
                + user.getEmail() + "\", \""
                + user.getId() + "\", \""
                + user.getCoverId() + "\", \""
                + user.getCoverUrl() + "\");";
        DBManager.getInstance().update(query);
        out.put("Status", true);
    }

    public static JSONObject getAuthResponse() {
        return out;
    }
}
