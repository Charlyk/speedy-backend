package md.speedy.developer.helpers;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 *
 * @author eduard.albu@gmail.com
 */
public class JSONParser<T> {

    private T mObject;

    ObjectMapper objectMapper = new ObjectMapper();

    public JSONObject parseRequest(HttpServletRequest request) {
        JSONObject object = new JSONObject();
        try (BufferedReader reader = request.getReader()) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            object = new JSONObject(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            object.put("error", e.getMessage());
        }
        return object;
    }

    public T buildObject(Class<T> object, String json) {
        try {
            mObject = objectMapper.readValue(json, object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mObject;
    }

    public String deserialize(T object) {
        try {
            //objectMapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
