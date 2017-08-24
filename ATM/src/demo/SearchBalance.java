package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchBalance {
	public void Search(String name) throws SQLException{
	
	
		Connection con=DBUtil.getConn();
		String sql=""+"select * from user  "+"where user_name=?";
		//预编译sql语句
		         PreparedStatement psmt = con.prepareStatement(sql);
		//先对应SQL语句，给SQL语句传递参数
		         psmt.setString(1, name);
		         /*psmt.execute();*///execute()方法是执行更改数据库操作（包括新增、修改、删除）;
		         //executeQuery()是执行查询操作
		         ResultSet rs = psmt.executeQuery();//返回一个结果集
		
		
		while(rs.next()){
			
			System.out.println(rs.getString("fund"));
		}
		
			
		
		
		
		
	
	
}
}
