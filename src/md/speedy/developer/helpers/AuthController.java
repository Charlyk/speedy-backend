package md.speedy.developer.helpers;

import md.speedy.developer.DBManager;
import org.json.JSONObject;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 *
 * @author eduard.albu@gmail.com
 */
public class AuthController {

    public boolean isRegistered(JSONObject income) {
        String email = income.getString("email");
        boolean isEmailCorrect = DBManager.getInstance().exists("users", "email", email);
        String password = income.getString("password");
        boolean isPasswordCorrect = DBManager.getInstance().exists("users", "password", password);
        return isEmailCorrect && isPasswordCorrect;
    }
}
