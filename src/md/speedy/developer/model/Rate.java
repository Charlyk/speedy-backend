package md.speedy.developer.model;

import md.speedy.developer.helpers.DBManager;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 12.12.15 12 2015
 * project Speedy Backend
 */
public class Rate {

    private JSONObject mResponseObject;

    public Rate() {
        mResponseObject = new JSONObject();
    }

    public JSONObject addRate(JSONObject income) {
        int rate = income.getInt("rate");
        String placeId = income.getString("placeId");
        String userId = income.getString("userId");
        String addRateQuery = "insert into rates (place_id, user_id, rate) values (\"" + placeId + "\", \"" + userId
                + "\", \"" + rate + "\");";
        int row;
        String checkQuery = "select id from rates where place_id=\"" + placeId + "\" and user_id=\"" + userId + "\";";
        if (!DBManager.getInstance().exists(checkQuery)) {
            row = DBManager.getInstance().update(addRateQuery);
        } else {
            String query = "update rates set rate=" + rate + "where user_id=\"" + userId + "\" and place_id=\"" + placeId + "\";";
            row = DBManager.getInstance().update(query);
        }
        if (row > 0) {
            mResponseObject.put("Status", true).put("ResponseData", "Your rate added successfully");
            String queryRate = "select rate from rates where place_id=\"" + placeId + "\";";
            ResultSet set = DBManager.getInstance().query(queryRate);
            ArrayList<Integer> rates = new ArrayList<>();
            try {
                while (set.next()) {
                    rates.add(set.getInt("rate"));
                }
                int sum = 0;
                for (int r : rates) {
                    sum += r;
                }
                double placeRate = sum / rates.size();
                String updatePlaceRateQuery = "update places set rate=" + placeRate + " where place_id=\"" + placeId + "\";";
                DBManager.getInstance().update(updatePlaceRateQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            mResponseObject.put("Status", false).put("ResponseData", "Something went wrong, please try again");
        }
        return mResponseObject;
    }

    public int getCuerrentUserRate(String userId, String placeId) {
        String query = "select rate from rates where user_id=\"" + userId + "\" and place_id=\"" + placeId + "\";";
        ResultSet set = DBManager.getInstance().query(query);
        int rate = 0;
        try {
            while (set.next()) {
                rate = set.getInt("rate");
            }
        } catch (Exception e) {
            rate = 0;
        }
        return rate;
    }
}
