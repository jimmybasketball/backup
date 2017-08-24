package demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PasswordValidate {
	public int passwordValidate(String name,String password){
		List<String> listPassword=new ArrayList<String>();
		Scanner in =new Scanner(System.in);
		try{String sql1=""+"select * from user "+"where user_name=?";
		
		//预编译sql语句
		         PreparedStatement psmt = DBUtil.getConn().prepareStatement(sql1);
		//先对应SQL语句，给SQL语句传递参数
		         psmt.setString(1, name);
		         /*psmt.execute();*///execute()方法是执行更改数据库操作（包括新增、修改、删除）;
		         //executeQuery()是执行查询操作
		         ResultSet rs1 = psmt.executeQuery();//返回一个结果集
		
		
		while(rs1.next()){
			listPassword.add(rs1.getString("password"));
		}
		
		
		DBUtil.closeConn(rs1, psmt,null);
		if(listPassword.contains(password)){
			return 1;//true
		}else {
			return 2;//false
		}
		
		}
		catch(Exception e){}
		
		
		
		return 0;
		
		
	}
}
