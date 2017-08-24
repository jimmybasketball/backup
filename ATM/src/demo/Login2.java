package demo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.testng.annotations.Test;

public class Login2 {
	@Test
	public void login(){
		Scanner in =new Scanner(System.in);
		
		System.out.println("请输入用户名：");
		String nameLogin=in.nextLine();
try {
	      
	        
	        //2.创建statement类对象，用来执行SQL语句！！
	        Statement statement = DBUtil.getConn().createStatement();
	        //要执行的SQL语句
	        
	        
	        String sql1="select * from user where user_name=nameLogin";
    		ResultSet rs1 = statement.executeQuery(sql1);
    		System.out.println(rs1.getString("password"));
	        String sql="select * from user ";
	        //3.ResultSet类，用来存放获取的结果集！！
	        ResultSet rs = statement.executeQuery(sql);
	        List<String> list=new ArrayList();
	        
	        //存进数组
	        while(rs.next()){
	        	
	        	list.add(rs.getString("user_name"));
	        	
	        }
	        //转换成集合类
//	        List<String> list=Arrays.asList(aa);
	        if(list.contains(nameLogin)){
	        	System.out.println("true");
	        }
	        else{
	        	System.out.println("false");
	        }
	        	}
catch(Exception e){
	
}
	}
}
