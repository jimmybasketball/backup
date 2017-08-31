package sads.tyt;
import static io.restassured.RestAssured.*;//必须使用静态，不然given方法认不到

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;

import org.junit.runner.Request;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.testng.annotations.Test;

import groovy.json.JsonException;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class DouBanBook1 {
	@Test
	public void testGetBook1(){
		String url="https://api.douban.com/v2/book/search";
				Map<String, String> paramter=new HashMap<String, String>();
				paramter.put("q", "满月之夜白鲸现");
//				param("q","满月之夜白鲸现");
	Response response=given().params(paramter).get(url);
	String responseTest=get(url).asString();
	
	// Get the response body as a String
//	String response11 = get(url).asString();
	// And get all books with price < 10 from the response. "from" is statically imported from the JsonPath class
//	List<String> bookTitles = from(response).getList("store.book.findAll { it.price < 10 }.title");
//	List s=get(url).path("books.author");
//	for(int i=0;i<(s.size();i++){
//		System.out.println(((RestAssured) s).get(i));
//	}
//	List<String> groceries = from(responseTest).getList("shopping.category.author");
	 System.out.println(response.asString());
	 System.out.println(response.getBody().prettyPrint());
}
	@Test
	public void testGetBook1Count(){
		String url="https://api.douban.com/v2/book/search";
				Map<String, String> paramter=new HashMap<String, String>();
				paramter.put("q", "满月之夜白鲸现");
	given().params(paramter).when().get(url).then().body("count",equalTo(6));
	 
}
	@Test
	public void testStatus(){
		Response response1=get("http://baidu.com");
		if(response1.getStatusCode()==200){
			System.out.println("ok");
		}
	}
	@Test
	public void testNodeVerfiry(){
		Response response2=get("https://api.douban.com/v2/book/1220562");
		System.out.println(response2.getBody().prettyPrint());
		response2.then().body("publisher", equalTo("青岛出版社"));
		response2.then().body("tags",hasSize(8));
		response2.then().body("tags[0].count",lessThan(150) );
		response2.then().body("tags[0].count", greaterThan(50));
		response2.then().body("tags[1].name", not(""));
		response2.then().body("tags.count", hasItems(143,69,66,11));
		response2.then().body("$", hasItems("price=15.00元"));//$表示跟属性，还不是很懂
	}
	@Test
		public void testRespondTime(){
		when().get("http://baidu.com").then().time(lessThan(5L),TimeUnit.SECONDS);
	}
	@Test
	public void testShecma(){
		get("https://api.douban.com/v2/book/1220562").then().assertThat()
		.body(matchesJsonSchemaInClasspath("products-schema.json"));
	}
	
	@Test
	public void testCook_Header_Status(){
		Response response3=get("https://api.douban.com/v2/book/1220562");
		Headers allHeaders= response3.getHeaders();
		response3.getCookies();
		response3.getStatusCode();
		response3.getStatusLine();
		System.out.println("Header"+"\n"+allHeaders);
		System.out.println("COOK"+response3.getCookies());
		System.out.println("status"+response3.getStatusCode()+response3.getStatusLine());
				
		
	}
	@Test
	public void testDemo(){
		//发起一个接口请求
        String url = "https://club.app.autohome.com.cn/club_v7.5.0/club/jinghuatopic-pm1-p1-s20-b0.json";
        Response response =get(url);

        //如果返回的结构体是json格式的话，用jsonpath可以很方便的获取json中的各种数据       
        System.out.println(response.getBody().jsonPath().getString("result.list[1].bbsname"));

        //直接获取json结构体中的数据
        System.out.println(response.getBody().jsonPath().getString("returncode"));

        //也可以从json结构体中指定的根路径开始获取（当前指定result）
        JsonPath jsonPath = new JsonPath(response.asString()).setRoot("result");            

        //获取result下list列表中的值
        System.out.println(jsonPath.get("list.bbsname"));
        System.out.println(jsonPath.get("list.userpic"));       

        //获取list下所有userpic的值并验证链接是否有效
        List<String> imgurls = jsonPath.getList("list.userpic");
        for (Iterator iterator = imgurls.iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            System.out.println(string );        
        }
        

		
	}
	@Test
	public void testBookDetail(){
		//发起一个接口请求
        String url = "https://api.douban.com/v2/book/search?q=满月之夜白鲸现";
        Response response =get(url);

        //如果返回的结构体是json格式的话，用jsonpath可以很方便的获取json中的各种数据       
        System.out.println(response.getBody().jsonPath().getString("books.author"));
        //也可以从json结构体中指定的根路径开始获取（当前指定result）
        JsonPath jsonPath = new JsonPath(response.asString()).setRoot("books");            

        List<String> authors = jsonPath.getList("author");
        List<String> titles = jsonPath.getList("title");
        List<String> publishers = jsonPath.getList("publisher");
        List<String> ids = jsonPath.getList("id");
        List<String> prices = jsonPath.getList("price");
        
        Iterator author = authors.iterator();
        Iterator titler = titles.iterator();
        Iterator publisher = publishers.iterator();
        Iterator id = ids.iterator();
        Iterator price = prices.iterator();
        while(author.hasNext()){
        	String string1 =   author.next().toString();
        	String string2 =   titler.next().toString();
        	String string3 =   publisher.next().toString();
        	String string4 =   id.next().toString();
        	String string5 =   price.next().toString();
          try{
          Connection conn=DBUtil.getConn();
          String sql="INSERT INTO bookDetail (`title`, `author`, `publisher`, `id`, `price`) "
          		+ "VALUES (?, ?, ?, ?, ?);";
          PreparedStatement psmt = conn.prepareStatement(sql);
          psmt.setString(1, string2);
          psmt.setString(2, string1);
          psmt.setString(3, string3);
          psmt.setString(4, string4);
          psmt.setString(5, string5);
          psmt.execute();
          DBUtil.closeConn(null, psmt, conn);
          }
          catch(Exception e){}
        }
        

        
        /*
         * 一些集合类提供了内容遍历的功能，通过java.util.Iterator接口。
         * 这些接口允许遍历对象的集合。依次操作每个元素对象。
         * 当使用 Iterators时，在获得Iterator的时候包含一个集合快照。
         * 通常在遍历一个Iterator的时候不建议修改集合本省。
         * 
         * */

        
    }
	@Test
	public void testLagou(){
		String url="https://www.lagou.com/jobs/positionAjax.json?px=default&city=%E9%87%8D%E5%BA%86&needAddtionalResult=false&isSchoolJob=0";
//		Response response3=get(url);
		String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER";
		String Origin="https://www.lagou.com";
		String ContentType="application/x-www-form-urlencoded; charset=UTF-8";
		String Referer="https://www.lagou.com/jobs/list_%E6%B5%8B%E8%AF%95%E5%B7%A5%E7%A8%8B%E5%B8%88?px=default&city=%E9%87%8D%E5%BA%86";
		String cookie1="user_trace_token=20170824152138-de277827-889c-11e7-a5a0-525400f775ce; LGUID=20170824152138-de277c4e-889c-11e7-a5a0-525400f775ce; JSESSIONID=ABAAABAACBHABBICB8A6E0E55289EB9460FE0E673E8B679; _gat=1; PRE_UTM=; PRE_HOST=www.baidu.com; PRE_SITE=https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3Ds0EIegffgNt1MIvXtYmfVJ61jxSIe6FS8z49yLmyO9m%26wd%3D%26eqid%3Db784e9b80002073b0000000559a7850f; PRE_LAND=https%3A%2F%2Fwww.lagou.com%2F; _putrc=3938DE1BBF308BF9; login=true; unick=%E6%98%8E%E6%9D%B0; showExpriedIndex=1; showExpriedCompanyHome=1; showExpriedMyPublish=1; hasDeliver=24; TG-TRACK-CODE=index_navigation; _gid=GA1.2.1174598928.1504076788; _ga=GA1.2.879088804.1503559300; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1504076788,1504150809,1504150816,1504150823; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1504150860; LGSID=20170831114018-1ba14329-8dfe-11e7-9024-5254005c3644; LGRID=20170831114054-317e1991-8dfe-11e7-a070-525400f775ce; SEARCH_ID=ca67382678b9420294e24eec779b4b31; index_location_city=%E5%85%A8%E5%9B%BD";
		Response response4=given().header("user-agent", userAgent,"Origin",Origin,"Referer",Referer,"cookie",cookie1)
				                  .param("first", "ture").param("kd", "测试工程师").param("pn", "1")
				                  .when().post(url);
//		Response response4=given().header("user-agent", userAgent).header("cookie",cookie1).when().post(url);
		String aa=response4.getBody().prettyPrint();
		System.out.println(aa);
//		System.out.println(response4.getHeaders());
		
	}
}
