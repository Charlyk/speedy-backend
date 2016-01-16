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

    /**
     * Add or update a place rate
     * @param income the user request
     * @return JSONObject which contains the response for user
     */
    public JSONObject addRate(JSONObject income) {
        int rate = income.getInt("rate");
        // get placeId and userId from the request
        String placeId = income.getString("placeId");
        String userId = income.getString("userId");

        // create a query to check if the user already rated this place
        String checkQuery = "select * from rates where place_id=\"" + placeId + "\" and user_id=\"" + userId + "\";";
        // check if place is already rated by the user
        boolean exists = DBManager.getInstance().exists(checkQuery);
        int row = 0;

        // if it's already rated than we just update the rate
        if (exists) {
            String rateQuery = "update rates set rate=" + rate + " where place_id=\"" + placeId + "\" and user_id=\"" + userId + "\";";
            row = DBManager.getInstance().update(rateQuery);
        } else {
            // else we need to insert the rate to database
            // first we create a insert query
            String addRateQuery = "insert ignore into rates (place_id, user_id, rate) values (\"" + placeId +
                "\", \"" + userId + "\", " + rate + ");";
            // and insert the rate
            // row variable will show if there either a row is affected or not
            row = DBManager.getInstance().update(addRateQuery);
        }

        // if row variable is more the 0 this means that we inserted or updated the rate successfully
        if (row > 0) {
            // now we put a true response for the client
            mResponseObject.put("Status", true).put("ResponseData", "Your rate added successfully");
            // than we need to update the place total rate
            // first we create a query to get all rates for this place
            String queryRate = "select rate from rates where place_id=\"" + placeId + "\";";

            // than we execute the above query and add all the rates to an array list
            ResultSet set = DBManager.getInstance().query(queryRate);
            ArrayList<Integer> rates = new ArrayList<>();
            try {
                while (set.next()) {
                    rates.add(set.getInt("rate"));
                }
                // after this we need tu sum all rates
                int sum = 0;
                for (int r : rates) {
                    sum += r;
                }
                // and divide them by rates count to get the total value
                double placeRate = sum / rates.size();
                // and now we can create a query to update total rate for this place
                String updatePlaceRateQuery = "update places set rate=" + placeRate + " where place_id=\"" + placeId + "\";";
                // and finally we need to execute the above query
                DBManager.getInstance().update(updatePlaceRateQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // if row it's 0 that means we didn't update the rate and we need to tell about this to client
            mResponseObject.put("Status", false).put("Error", "Something went wrong, please try again");
        }
        return mResponseObject;
    }


    /**
     * Get the rate for the given user id and place id
     * @param userId for whom to get the rate
     * @param placeId for what place to get the rate
     * @return the rate for the given userId and placeId
     */
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
