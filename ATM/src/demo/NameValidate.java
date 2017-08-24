package demo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NameValidate {
	public String NameValidate1(String name){
		
		List<String> list=new ArrayList();
try {
	        
	        //增加数据
	        
	        //2.创建statement类对象，用来执行SQL语句！！
	        Statement statement = DBUtil.getConn().createStatement();
	        //要执行的SQL语句
	        //String sql = "INSERT VALUES into user(name,pass,telephone,'')";
	        String sql="select *from user";
	        //3.ResultSet类，用来存放获取的结果集！！
	        ResultSet rs = statement.executeQuery(sql);

	      //存进数组
	        while(rs.next()){
	        	
	        	list.add(rs.getString("user_name"));
	        	
	        }
	        DBUtil.closeConn(rs, null, DBUtil.getConn() );
	        //转换成集合类
//	        List<String> list=Arrays.asList(aa);
	        if(list.contains(name)){
//	        	System.out.println("用户存在");
	        	return "用户存在";
	        }else{
//	        	System.out.println("用户不存在");
	        	return "用户不存在";
	        }
	        
	        
	    } catch(Exception e) {   
	        
	    }

return null;
	}
}
