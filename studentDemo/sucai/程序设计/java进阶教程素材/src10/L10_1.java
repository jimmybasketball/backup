import java.sql.*;
public class L10_1 
{
	public static void main(String[] args)
	{
		Connection ct=null;
		Statement sm=null;
		try{
		  Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		  ct=DriverManager.getConnection("jdbc:odbc:sql server","sa","ydyd4488321");
		  sm=ct.createStatement();
//		  int i=sm.executeUpdate("insert into bumen values('12','aa','aaaa')");
//		  if(i==1)
//		  {
//			  System.out.println("��ӳɹ���");
//		  }else
//		  {
//			  System.out.println("���ʧ�ܣ�");
//		  }
//		  int i=sm.executeUpdate("delete from bumen where bianhao='12'");
//		  if(i==1)
//		  {
//			  System.out.println("ɾ���ɹ���");
//		  }else
//		  {
//			  System.out.println("ɾ��ʧ�ܣ�");
//		  }
//		  int i=sm.executeUpdate("update bumen set didian='������' where bianhao='1'");
//		  if(i==1)
//		  {
//			  System.out.println("�޸ĳɹ���");
//		  }else
//		  {
//			  System.out.println("�޸�ʧ�ܣ�");
//		  }
		  ResultSet rs=sm.executeQuery("select * from bumen where bianhao=1125 and didian='vxdvɽ' or 1='1'"); 
		  while(rs.next())
		  {
			  int bianhao=rs.getInt(1);
			  String mingcheng=rs.getString(2);
			  String didian=rs.getString(3);
			  System.out.println(bianhao+"    "+mingcheng+"     "+didian);
		  }		  
	    }catch(Exception e){}
	    finally
	    {
	    	try {
				if(sm!=null)
				{
					sm.close();
				}
				if(ct!=null)
				{
					ct.close();
				}
			} catch (Exception e2){}
	    }
	}
}