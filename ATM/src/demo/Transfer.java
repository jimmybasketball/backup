package demo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Transfer {
	public void transferMoney(String namePayfer,String namePayee,int money){
		/*扣除自己账户fund:update user set fund-=money where user_name=namePayfer
		 * 增加对方fund金额:update user set fund+=money where user_name=namePayee
		 * 边界值校验:money=<fund(namePayfer)
		 * 
		 * */
		List<String> list=new ArrayList();
		int[]aa=new int[9];
		int i=0;
		int[]bb=new int[9];
		int j=0;
		try {Connection con=DBUtil.getConn();
		String sql=""+"select * from user  "+"where user_name=?";
		String sql3=""+"select * from user  "+"where user_name=?";
		String sql1=""+"update user set fund=?-?   "+"where user_name=?";
		String sql2=""+"update user set fund=?+?   "+"where user_name=?";
		//预编译sql语句
		         PreparedStatement psmt = con.prepareStatement(sql);
		         PreparedStatement psmt1 = con.prepareStatement(sql1);
		         PreparedStatement psmt2 = con.prepareStatement(sql2);
		         PreparedStatement psmt3 = con.prepareStatement(sql3);
		//先对应SQL语句，给SQL语句传递参数
		         
					psmt.setString(1, namePayfer);
					psmt3.setString(1, namePayee);
					
		         /*psmt.execute();*///execute()方法是执行更改数据库操作（包括新增、修改、删除）;
		         //executeQuery()是执行查询操作
		         ResultSet rs = psmt.executeQuery();//返回一个结果集
		         ResultSet rs1 = psmt3.executeQuery();
		
		//付款方最初的金额
		while(rs.next()){
			
			//list.add(rs.getLong("fund"));
			aa[i]=rs.getInt("fund");
			
			i++;
//			rs.getBigDecimal("fund");
//			System.out.println(rs.getBigDecimal("fund"));
		}
		//金额校验
		if(aa[0]-money<0){
			System.out.println("余额不足");
			System.exit(0);
		}
		//收款方最初的金额
				while(rs1.next()){
					
					//list.add(rs.getLong("fund"));
					bb[j]=rs1.getInt("fund");
					
					j++;

				}
		
		psmt1.setInt(1, aa[0]);
		psmt1.setInt(2, money);
		psmt1.setString(3, namePayfer);
		psmt1.execute();
		//
		psmt2.setInt(1, bb[0]);
		psmt2.setInt(2, money);
		psmt2.setString(3, namePayee);
		psmt2.execute();
		System.out.println("转账成功");
		DBUtil.closeConn(rs1, psmt3, con);
		
		
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
