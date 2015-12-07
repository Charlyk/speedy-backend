package md.speedy.developer.helpers;

import md.speedy.developer.DBManager;
import org.json.JSONObject;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 * @author eduard.albu@gmail.com
 */
public class AuthController {

    public static boolean isUserRegistered(JSONObject income) {
        String id = income.getString("id");
        return DBManager.getInstance().exists("users", "user_id", id);
    }
}
