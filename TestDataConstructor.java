import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.org.json.JSONObject;

public class TestDataManager {
  
  public String getJsonString(String schemaURL) {    
    String url = schemaURL;
    URL urlObject = new URL(url);
    HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
    InputStreamReader stream = new InputStreamReader(connection.getInputStream());
    BufferedReader in = new BufferedReader(stream);

    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    } 
    in .close();
    return response.toString();
   }
  
  public JSONObject getResourceSchema(String jsonString) {
    JSONObject result = new JSONObject(jsonString);
    return result;
  }
}
