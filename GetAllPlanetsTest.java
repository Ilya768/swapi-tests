package com.swapi.maven.testng;

import org.testng.annotations.Test;

public class StarWarsAPITests {
  @Test
  public void ResponseDataTest() {
    given().when().get("https://swapi.co/api/planets/").
      assertThat().statusCode(200).and().
      contentType(ContentType.JSON);
  }
  
  @Test
  public void GetFirstPageResult() {
    String pageNumber = "1";
    String next = "https://swapi.co/api/planets/?page=2";
    String firstPlanetName = "Alderaan";
    
    int allPlanets = 61;
    int resultSize = 10;
    
    given().pathParam("number", pageNumber).when().
      get("https://swapi.co/api/planets/?page={number}").
    then().
      assertThat().body("planets.'count'", equalTo(allPlanets)).and().
      assertThat().body("planets.'next'", equalTo(next)).and().
      assertThat().body("planets.'previous'", equalTo(null)).and().
      assertThat().body("planets.'results.length'", equalTo(resultSize)).and().
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
    int resultSize = 10;
    
    given().
      pathParam("number", pageNumber).
      pathParam("previousPage", previous).
      pathParam("nextPage", next).
     when()get("https://swapi.co/api/planets/?page={pageNumber}").
     then().
      assertThat().body("planets.'next'", equalTo(next)).and().
      assertThat().body("planets.'previous'", equalTo(previous)).and().
      assertThat().body("planets.'results.length'", equalTo(resultSize));    
  }
  
  @Test
  public void GetLastPageResult() {
    String pageNumber = "7";
    String previous = "https://swapi.co/api/planets/?page=6";
    String firstPlanetName = "Jakku";
    
    int resultSize = 1;
    
    given().pathParam("number", pageNumber).when().
      get("https://swapi.co/api/planets/?page={number}").
    then().
      assertThat().body("planets.'next'", equalTo(null)).and().
      assertThat().body("planets.'previous'", equalTo(previous)).and().
      assertThat().body("planets.'results.length'", equalTo(resultSize)).and().
      assertThat().body("planets.'results[0].'name''", equalTo(firstPlanetName));
  }
}
