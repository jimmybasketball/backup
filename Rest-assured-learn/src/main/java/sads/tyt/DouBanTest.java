package sads.tyt;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
 
public class DouBanTest
{
 @Before
public void setUP(){
 //指定 URL 和端口号
 RestAssured.baseURI = "https://api.douban.com/v2/book";
 RestAssured.port = 80;
 }
 
 @Test
 public void testGETBook()
 {
 get("/1220562").then().body("publisher", equalTo("青岛出版社"));
 }
 @Test
 public void testSearchBook(){
 given().param("q", "java8").when().get("/search").then().body("count", equalTo(2));
}
}