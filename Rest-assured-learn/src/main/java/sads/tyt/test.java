package sads.tyt;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import java.util.Iterator;
import java.util.List;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class test { 

    public static void main(String[] args){ 

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

}