import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.org.json.JSONObject;

public class TestDataManager {
  
  public class JsonResponseProcessor {
    public String getJsonString(String schemaURL) {    
      URL urlObject = new URL(schemaURL);
      HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
      InputStreamReader stream = new InputStreamReader(connection.getInputStream());
      BufferedReader in = new BufferedReader(stream);

      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      } 
      in.close();
      return response.toString();
     }

    public JSONObject getJsonObject(String jsonString) {
      JSONObject result = new JSONObject(jsonString);
      return result;
    }
  }
  
  public class MutualTestData {
    String baseURL;
    int allResults;
    int resultsPerPage;
    
    MutualTestData() {
      this.baseURL = "https://swapi.co/api/planets/";
      this.allResults = 61;
      this.resultsPerPage = 10;
    }
    
    public String getBaseUrl() {
      return baseUrl;
    }
    
    public int getAllResultsValue() {
      return allResults;
    }
    
    public int getResultsPerPageValue() {
      return resultsPerPage;
    }
  }
  
  public class FirstPageTestData {
    String firstPage;
    String nextFromFirstPage;
    String firstPlanetName;
    
    FirstPageTestData() {
      this.firstPage = "1";
      this.nextFromFirstPage = "https://swapi.co/api/planets/?page=2";
      this.firstPlanetName = "Alderaan";
    }
    
    public String getFirstPageValue() {
      return firstPage;
    }
    
    public String getNextPageUrl() {
      return nextFromFirstPage;
    }
    
    public String getPlanetName() {
      return firstPlanetName;
    }
  }
  
  public class LastPageTestData {
    String lastPage;
    String lastPlanetName;
    String previousFromLastPage;
    int resultsAtLastPage;
    
    public LastPageTestData() {
      this.lastPage = "7";
      this.lastPlanetName = "Jakku";
      this.previousFromLastPage = "https://swapi.co/api/planets/?page=6";
      this.resultsAtLastPage = 1;
    }
    
    public String getLastPageValue() {
      return lastPage;
    }
    
    public String getLastPlanetName() {
      return lastPlanetName;
    }
    
    public String getPreviousPageUrl() {
      return previousFromLastPage;
    }
    
    public int getLastPageResultsNumber() {
      return resultsAtLastPage;
    }
  }
}
