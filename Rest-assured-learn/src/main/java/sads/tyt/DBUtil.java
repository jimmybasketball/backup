package sads.tyt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//һ�����ݿ�ͨ�õİ����࣬�����������ݿ���ر����ݿ�
public class DBUtil {
    
    //��һ��������������Ҫ��һЩȫ�ֱ���
    private final static String DRIVER_CLASS="com.mysql.jdbc.Driver";//�����������ַ���
    
    private final static String CONN_STR="jdbc:mysql://localhost:3306/school";//���ݿ������ַ���
    
    private final static String DB_USER="root";//�����û�
    
    private final static String DB_PWD="qazwsx@123456";//���ݿ��¼����
    
    //�ڶ������������ݿ�����(������sqlserver)
    static{
        
        try{
            
            Class.forName(DRIVER_CLASS);
            
        }catch(ClassNotFoundException e){
            
            e.printStackTrace();//�׳��쳣
        }
    }
    
    //����������ȡ���ݿ�����
    public static Connection getConn(){
        
        try {
            
            return DriverManager.getConnection(CONN_STR,DB_USER,DB_PWD);
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            
        }
        return null;
    }
    
    //���ر����ݿ�����
    public static void closeConn(ResultSet rs,PreparedStatement pstmt,Connection conn){
        
        try {
            if (rs!=null) {//������صĽ����������Ϊ��,�͹ر�����
                rs.close();
            }
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        try {
            if (pstmt!=null) {
                pstmt.close();//�ر�Ԥ�������
            }
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        try {
            
            if (conn!=null) {
                conn.close();//�رս��������
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
}