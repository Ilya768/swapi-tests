package com.swapi.maven.testng;

import org.testng.annotations.Test;

public class StarWarsAPITests {
  private static final String URL = "https://swapi.co/api/planets/";
  private static final String firstPage = "1";
  private static final String nextFromFirstPage = "https://swapi.co/api/planets/?page=2";
  private static final String firstPlanetName = "Alderaan";
  
  private static final int allPlanets = 61;
  private static final int resultsPerPage = 10;
  
  private static final String lastPage = "7";
  private static final int resulsNumberAtLastPage = 1;
  private static final String lastPlanetName = "Jakku";
  private static final String previousFromLastPage = "https://swapi.co/api/planets/?page=6";
  
  
  @Test
  public void ResponseDataTest() {
    given().when().get(URL).
      assertThat().statusCode(200).and().
      contentType(ContentType.JSON);
  }
  
  @Test
  public void GetFirstPageResult() {
    given().pathParam("number", firstPage).when().
      get(URL + "?page={number}").
    then().
      assertThat().body("planets.'count'", equalTo(allPlanets)).and().
      assertThat().body("planets.'next'", equalTo(nextFromFirstPage)).and().
      assertThat().body("planets.'previous'", equalTo(null)).and().
      assertThat().body("planets.'results.length'", equalTo(resultsPerPage)).and().
      assertThat().body("planets.'results[0].'name''", equalTo(firstPlanetName));
  }
  
  @DataProvider(name="PlanetsListResult")
  public Object[][] createTestData {
    return new Object[][] {
      {"2", "https://swapi.co/api/planets/?page=1", "https://swapi.co/api/planets/?page=3"},
      {"3", "https://swapi.co/api/planets/?page=2", "https://swapi.co/api/planets/?page=4"},
      {"4", "https://swapi.co/api/planets/?page=3", "https://swapi.co/api/planets/?page=5"},
      {"5", "https://swapi.co/api/planets/?page=4", "https://swapi.co/api/planets/?page=6"},
      {"6", "https://swapi.co/api/planets/?page=5", "https://swapi.co/api/planets/?page=7"},
    };
  }
  
  @Test(dataProvider = "PlanetsListResults")
  public void GetPagesTwoToSix(String pageNumber, String previous, String next) {    
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
    given().pathParam("number", lastPage).when().
      get(URL + "?page={number}").
    then().
      assertThat().body("planets.'next'", equalTo(null)).and().
      assertThat().body("planets.'previous'", equalTo(previousFromLastPage)).and().
      assertThat().body("planets.'results.length'", equalTo(resulsNumberAtLastPage)).and().
      assertThat().body("planets.'results[0].'name''", equalTo(lastPlanetName));
  }
}
