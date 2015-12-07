package md.speedy.developer;

import com.google.api.client.googleapis.apache.GoogleApacheHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import java.util.Arrays;

/**
 * Created by Eduard Albu on 06.12.15 12 2015
 * project Speedy Backend
 */
public class GoogleAuth {

    HttpTransport transport;
    GoogleIdTokenVerifier verifier;
    JsonFactory jsonFactory;
    String[] audience = {"22473876548-k6qin9a62286r35u92kn49t35f9oo9mt.apps.googleusercontent.com"};
    public GoogleAuth() {
        try {
            jsonFactory = new JacksonFactory();
            transport = GoogleApacheHttpTransport.newTrustedTransport();
            verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Arrays.asList(audience)).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GoogleIdToken.Payload verify(String idTokenString) {
        GoogleIdToken.Payload payload = null;
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                payload = idToken.getPayload();
                System.out.println("User ID: " + payload.getSubject());
            } else {
                System.out.println("Invalid ID token.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payload;
    }
}
