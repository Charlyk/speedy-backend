package md.speedy.developer.helpers;

import md.speedy.developer.models.Place;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 16.01.16 01 2016
 * project Speedy Backend
 */
public class SearchController {

    private static final String BASE = "select place_id from tags where tag like ";
    private static final String OR_LIKE = " or tag like ";

    private static JSONObject responseObject = new JSONObject();

    public static JSONObject search(String searchFilter) {
        // create a search query to search with full phrase
        String dbQuery = "select place_id from tags where tag like '%" + searchFilter + "%';";
        ArrayList<String> placesResult = new ArrayList<>();
        if (searchFilter.contains(" ")) {
            getPlacesIds(DBManager.getInstance().query(buildMultiWordQuery(searchFilter)), placesResult);
        }
        getPlacesIds(DBManager.getInstance().query(dbQuery), placesResult);
        JSONArray results = new JSONArray();
        for (String id : placesResult) {
            if (!id.equals(",") || !id.equals(" ")) {
                Place place = new Place.Builder().getPlaceDataFromDB(id, "0").buildSearchResult();
                results.put(Place.writeSearchResult(place));
            }
        }

        if (results.length() > 0) {
            responseObject.put("Status", true).put("ResponseData", results);
        } else {
            responseObject.put("Status", true).put("ResponseData", "No matches found");
        }
        return responseObject;
    }


    private static void getPlacesIds(ResultSet resultSet, ArrayList<String> destination) {
        try {
            StringBuilder idBuilder = new StringBuilder();
            while (resultSet.next()) {
                idBuilder.append(resultSet.getString("place_id")).append(",");
            }
            resultSet.close();
            String[] idArray = idBuilder.toString().split(",");
            for (String id : idArray) {
                if (!destination.contains(id)) {
                    destination.add(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildMultiWordQuery(String searchFilter) {
        String[] words = searchFilter.split(" ");
        StringBuilder builder = new StringBuilder();
        builder.append(BASE);
        for (int i = 0; i < words.length; i++) {
            if (i != words.length - 1) {
                builder.append("'%").append(words[i]).append("%'").append(OR_LIKE);
            } else {
                builder.append("'%").append(words[i]).append("%'");
            }
        }
        return builder.toString();
    }
}
