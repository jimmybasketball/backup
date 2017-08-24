package demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Deposito {
	public void Deposito1(String name,int money){
		int[]aa=new int[9];
		int i=0;
		try{
			Connection conn=DBUtil.getConn();
			String sql=""+"select * from user  "+"where user_name=?";
			String sql1=""+"update user set fund=?+?   "+"where user_name=?";
			
			
			PreparedStatement psmt = conn.prepareStatement(sql);
			PreparedStatement psmt1 = conn.prepareStatement(sql1);
			
			psmt.setString(1, name);
			ResultSet rs = psmt.executeQuery();
			
			//付款方最初的金额
			while(rs.next()){
				
				//list.add(rs.getLong("fund"));
				aa[i]=rs.getInt("fund");
				
				i++;

			}
			psmt1.setInt(1, aa[0]);
			psmt1.setInt(2, money);
			psmt1.setString(3, name);
			
			
			
			
			psmt1.execute();
			int s=aa[0]+money;
			System.out.println("存款成功"+s);
			DBUtil.closeConn(rs, psmt, conn);
		}
		catch(Exception e){
			System.out.println("有出异常了？");
		}
	}
	}

