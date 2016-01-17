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
public class Contacts {
    private String phone;
    private String email;
    private String site;

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getSite() {
        return site;
    }

    public static JSONObject writeJSON(Contacts contacts) {
        try {
            String string = new ObjectMapper().writeValueAsString(contacts);
            return new JSONObject(string);
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject().put("Status", false);
        }
    }

    public static class Builder {
        private String phone;
        private String email;
        private String site;

        public Builder getContactsFromDB(String placeId) {
            String query = "select phone, email, site from places where place_id=\"" + placeId + "\";";
            ResultSet resultSet = DBManager.getInstance().query(query);
            try {
                while (resultSet.next()) {
                    this.phone = resultSet.getString("phone");
                    this.email = resultSet.getString("email");
                    this.site = resultSet.getString("site");
                }
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public Contacts build() {
            Contacts contacts = new Contacts();
            contacts.phone = this.phone;
            contacts.email = this.email;
            contacts.site = this.site;
            return contacts;
        }
    }
}
