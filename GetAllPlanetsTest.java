package com.swapi.maven.testng;

import org.testng.annotations.Test;
import TestDataConstructor.TestDataManager.JsonResponseProcessor;
import TestDataConstructor.TestDataManager.MutualDataSet;
import TestDataConstructor.TestDataManager.FirstPageDataSet;
import TestDataConstructor.TestDataManager.LastPageDataSet;

public class StarWarsAPITests {
  
  MutualDataSet mutualData = new MutualDataSet();
  int allPlanets = mutualData.getAllResultsValue();
  int resultsPerPage = mutualData.getResultsPerPageValue();
  String URL = mutualData.getBaseUrl();
  
  /*Data set below represents the basic information about page results structure and returns a two demantional array
  type of Object with 3 values per row in following pattern: "Page Number", "Previous Page URL", "Next Page URL". 
  Data is hardcoded due to low number of pages. An alternative way to create such a data set is shown 
  at "AltPlanetList" section.*/
  @DataProvider(name="PlanetsListResult")
  public Object[][] createTestData() {
    return new Object[][] {
      {"2", "https://swapi.co/api/planets/?page=1", "https://swapi.co/api/planets/?page=3"},
      {"3", "https://swapi.co/api/planets/?page=2", "https://swapi.co/api/planets/?page=4"},
      {"4", "https://swapi.co/api/planets/?page=3", "https://swapi.co/api/planets/?page=5"},
      {"5", "https://swapi.co/api/planets/?page=4", "https://swapi.co/api/planets/?page=6"},
      {"6", "https://swapi.co/api/planets/?page=5", "https://swapi.co/api/planets/?page=7"},
    };
  }
  
  @DataProvider(name="AltPlanetsList")
  public Object[][] createTestObject() {
    int last = Integer.parseInt(lastPage);
    int dataSize = 3;
    Object[][] dataSet = new Object[last-2][dataSize];
    
    for(int i = 2; i < last; i++) {
      for(int j = 0; j < dataSize; j++) {
        if(j == 0) dataSet[i][j] = "" + i;
        if(j == 1) dataSet[i][j] = URL + "?page=" + (i-1);
        if(j == 2) dataSet[i][j] = URL + "?page=" + (i+1);
      }
    }
    
    return dataSet;
  }
  
  /*----------------Test Data for Planet Tatooine, id = 1-------------*/
  @DataProvider(name="Tatooine")
  public HashMap<String, String> createPlanetData() {    
    HashMap<String, String> planet = new HashMap();
    String residents = "https://swapi.co/api/people/1/," +
                       "https://swapi.co/api/people/2/," +
                       "https://swapi.co/api/people/4/," +
                       "https://swapi.co/api/people/6/," +
                       "https://swapi.co/api/people/7/," +
                       "https://swapi.co/api/people/8/," +
                       "https://swapi.co/api/people/9/," +
                       "https://swapi.co/api/people/11/," +
                       "https://swapi.co/api/people/43/," +
                       "https://swapi.co/api/people/62/";
    
    String films = "https://swapi.co/api/films/5/," +
                   "https://swapi.co/api/films/4/," +
                   "https://swapi.co/api/films/6/," +
                   "https://swapi.co/api/films/3/," +
                   "https://swapi.co/api/films/1/";
    
    map.put("name", "Tatooine");
    map.put("rotation_period", "23");
    map.put("orbital_period", "304");
    map.put("diameter", "10465");
    map.put("climate", "arid");
    map.put("gravity", "1 standard");
    map.put("terrain", "desert");
    map.put("surface_water", "1");
    map.put("population", "200000");
    map.put("residents", residents);
    map.put("films", films);
    map.put("created", "2014-12-09T13:50:49.641000Z");
    map.put("edited", "2014-12-21T20:48:04.175778Z");
    map.put("url", "https://swapi.co/api/planets/1/");
  }
  
  /*----------Tests for https://swapi.co/api/planets/ GET ALL-------------*/
  @Test
  public void ResponseDataTest() {
    given().when().get(URL).
      assertThat().statusCode(200).and().
      contentType(ContentType.JSON);
  }
  
  @Test
  public void GetFirstPageResult() {
    FirstPageDataSet firstPage = new FirstPageDataSet();
    String pageNumber = firstPage.getFirstPageValue();
    String nextFromFirstPage = firstPage.getNextPageUrl();
    String planetName = firstPage.getPlanetName();
    
    given().pathParam("number", firstPage).when().
      get(URL + "?page={number}").
    then().
      assertThat().body("planets.'count'", equalTo(allPlanets)).and().
      assertThat().body("planets.'next'", equalTo(nextFromFirstPage)).and().
      assertThat().body("planets.'previous'", equalTo(null)).and().
      assertThat().body("planets.'results.length'", equalTo(resultsPerPage)).and().
      assertThat().body("planets.'results[0].'name''", equalTo(firstPlanetName));
  }
  
  @Test(dataProvider = "PlanetsListResults")
  public void GetMiddlePages(String pageNumber, String previous, String next) {    
    given().
      pathParam("number", pageNumber).
      pathParam("previousPage", previous).
      pathParam("nextPage", next).
     when().get(URL + "?page={pageNumber}").
     then().
      assertThat().body("planets.'next'", equalTo(next)).and().
      assertThat().body("planets.'previous'", equalTo(previous)).and().
      assertThat().body("planets.'results.length'", equalTo(resultsPerPage));    
  }
  
  @Test
  public void GetLastPageResult() {
    LastPageDataSet lastPage = new LastPageDataSet();
    String pageNumber = lastPage.getLastPageValue();
    String previousFromLastPage = lastPage.getPreviousPageUrl();
    String planetName = lastPage.getLastPlanetName();
    int resultsNumberAtLastPage = lastPage.getLastPageResultsNumber();
    
    given().pathParam("number", lastPage).when().
      get(URL + "?page={number}").
    then().
      assertThat().body("planets.'next'", equalTo(null)).and().
      assertThat().body("planets.'previous'", equalTo(previousFromLastPage)).and().
      assertThat().body("planets.'results.length'", equalTo(resulsNumberAtLastPage)).and().
      assertThat().body("planets.'results[0].'name''", equalTo(lastPlanetName));
  }
  
  /*-------------End of Tests for https://swapi.co/api/planets/ GET ALL---------------*/
  
  /*-------------Tests for https://swapi.co/api/planets/{id} GET BY ID-----------------*/
  
  @Test
  public void RequiredAttributesPresent() {
    String schemaURL = "https://swapi.co/api/planets/schema";
    
    JsonResponseProcessor jsonRP = new JsonResponseProcessor();
    String jsonString = jsonRP.getJsonString(schemaURL);
    JsonObject response = jsonRP.getJsonObject(jsonString);
    
    String[] attributes = response.required;
    for(int i = 1; i <= allPlanets; i++) {
      JsonObject planetResponse = jsonRP.getJsonObject(jsonRP.getJsonString(URL + "i/"));
      ArrayList<String> planetAttributes = jsonRP.structureJsonResponse(planetResponse);
      assertEquals(attributes.length, planetAttributes.size());
      
      for(int j = 0; j < attributes.length; j++) {
        assertEquals(attributes[j], planetAttributes.get(j));
      }
    }
  }
  
  @Test(dataProvider = "Tatooine")
  public void GivenIdReturnsExpectedValue(HashMap<String, String> planet) {
    String planetURL = "https://swapi.co/api/planets/1/";
    
    JsonResponseProcessor jsonRP = new JsonResponseProcessor();
    String jsonString = jsonRP.getJsonString(planetURL);
    JsonObject response = jsonRP.getJsonObject(jsonString);
    
    for(String key : planet.keys()) {
      if(response.key instanceof Array) {
        String[] converted = planet.get(key).spli(",");
        assertArrayEquals(response.key, planet.get(key));
        continue;
      }
      
      assertEquals(response.key, planet.get(key));
    }
  }
}
