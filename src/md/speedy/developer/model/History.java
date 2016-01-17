package md.speedy.developer.model;

import md.speedy.developer.helpers.DBManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Eduard Albu on 12.12.15 12 2015
 * project Speedy Backend
 */
public class History {

    private JSONObject mResponseObject;

    public History() {
        mResponseObject = new JSONObject();
    }


    /**
     * This method will add places to histories table
     * @param placeId visited placeId
     * @param userId wh visited that place
     */
    public static void addToHistory(String placeId, String userId) {
        // first we need to obtain the current date
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());

        // than we need to format the date for the database
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss", Locale.getDefault());
        String today = format.format(date);

        // after formating create a query to insert insert the place to user history
        String query = "insert ignore into histories (user_id, place_id) values (\"" + userId + "\", \"" + placeId + "\");";
        DBManager.getInstance().update(query);

        // adn now we need to update the date
        // I decided to update the date separately because a user can visit a place more than once
        // and in that case the above query will do nothing, so we need to update the date to when user visited the place
        // for the last time
        String updateQuery = "update histories set date=\"" + today + "\" where place_id=\"" + placeId + "\" and user_id=\"" + userId + "\";";
        DBManager.getInstance().update(updateQuery);
    }


    /**
     * Get the history from the database
     * @param userId for whom to get the history
     * @param offset the position from where to start getting histories
     * @param limit how many histories to get
     * @return a JSONObject with obtained histories
     */
    public JSONObject getHistory(String userId, int offset, int limit) {
        // create a query to obtain the data
        String query = "select p.name, p.description, p.rate, p.logo from places p, histories h " +
                "where p.place_id=h.place_id and h.user_id=\"" + userId + "\" limit " + offset + "," + limit + ";";

        // execute the query
        ResultSet set = DBManager.getInstance().query(query);
        try {
            // write all results into a JSONArray
            JSONArray histories = new JSONArray();
            while (set.next()) {
                JSONObject object = new JSONObject();
                object.put("name", set.getString("name"));
                object.put("description", set.getString("description"));
                object.put("rate", set.getDouble("rate"));
                object.put("photo", set.getString("logo"));
                histories.put(object);
            }

            // put all the results into response object
            mResponseObject.put("Status", true).put("ResponseData", histories);
        } catch (Exception e) {
            // or put an error if it's the case
            mResponseObject.put("Status", false).put("Error", e.getCause());
        }
        return mResponseObject;
    }
}
