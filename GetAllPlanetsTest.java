package com.swapi.maven.testng;

import org.testng.annotations.Test;
import TestDataConstructor.TestDataManager.JsonSchemaFetcher;
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
}
