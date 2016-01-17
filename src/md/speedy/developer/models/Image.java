package md.speedy.developer.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.speedy.developer.helpers.DBManager;
import org.json.JSONArray;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Eduard Albu on 16.01.16 01 2016
 * project Speedy Backend
 */
public class Image {
    private String url;

    public String getUrl() {
        return url;
    }

    public static JSONArray writeJSON(ArrayList<Image> images) {
        JSONArray urls = new JSONArray();
        try {
            for (Image image : images) {
                urls.put(new ObjectMapper().writeValueAsString(image));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

    public static class Builder {
        private String url;
        private ArrayList<Image> urls = new ArrayList<>();

        public Builder getImageGalleryFromDB(String placeId) {
            String query = "select path_to_photo from place_images where place_id=\"" + placeId + "\";";
            ResultSet resultSet = DBManager.getInstance().query(query);
            try {
                while (resultSet.next()) {
                    this.url = resultSet.getString("path_to_photo");
                    urls.add(build(this));
                }
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        private Image build(Builder builder) {
            Image image = new Image();
            image.url = builder.url;
            return image;
        }

        public ArrayList<Image> build() {
            return urls;
        }
    }
}
