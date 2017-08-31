package sads.tyt;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import java.util.Map;

import io.restassured.http.Header;
import  io.restassured.http.Header.*;
import io.restassured.response.Response;
import  io.restassured.response.Response.*;

/**
 * Hello world!
 *
 */
public class App 
{
	
    public static void main( String[] args )
    {
    	String url = "http://platform.app.autohome.com.cn/platform_v7.5.0/api/opt/propaganda";
        //设置入参
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("pm", "2");
        parameters.put("a", "2");
        parameters.put("v", "7.6.0");
        parameters.put("deviceid", "7b64fb2877cbb15900b670886ea3f71488899c90");
     parameters.put("cityid", "110100");         
    //设置url请求头
    Header first = new Header("username", "xushizhao");
        
		Response response = given().proxy("192.168.221.190",80)
                    .parameters(parameters)
                    .header(first)
                    .get(url);
    //查看请求正文
    System.out.println(response.asString());

    	System.out.println( "Hello World!" );
    }
}
