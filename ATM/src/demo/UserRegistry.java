package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.testng.annotations.Test;

public class UserRegistry {
	@Test
	public void regi1stry(){
		
		//DBUtil.getConn();
	    //遍历查询结果集
		List<String> list=new ArrayList();
	    Scanner in =new Scanner(System.in);
	    System.out.println("======欢迎来到注册模块======");
		System.out.println("请输入用户名：");
		String name= in.nextLine();
		//连接数据库查看用户名是否重复
		String backName= new NameValidate().NameValidate1(name);
		while(backName.equals("用户存在")){
			System.out.println("用户已存在，请重新输入");
        	name=in.nextLine();
        	 backName= new NameValidate().NameValidate1(name);
		}

		
		System.out.println("请输入密码： ");
		String pass=in.nextLine();
		System.out.println("请输入电话号码：");
		String telephone=in.nextLine();
	    try {
	        
	        //增加数据
	        java.sql.PreparedStatement psql;
	        
	        //预处理添加数据，其中有两个参数--“？”
	        psql = DBUtil.getConn().prepareStatement("insert into user (user_name,password,telephone,fund) "
	                + "values(?,?,?,?)");
	        psql.setString(1, name);              //设置参数1，创建id为3212的数据
	        psql.setString(2, pass);      //设置参数2，name 为王刚
	        psql.setString(3, telephone);
	        psql.setString(4, "5000"); 
	       
	        psql.executeUpdate();           //执行更新
	       

	      DBUtil.closeConn(null, psql, DBUtil.getConn());  
	    } catch(Exception e) {   
	        
	    }
		

	}
	
	
}


