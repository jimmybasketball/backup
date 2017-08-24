package demo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//一个数据库通用的帮助类，用于连接数据库与关闭数据库
public class DBUtil {
    
    //第一步：声明你所需要的一些全局变量
    private final static String DRIVER_CLASS="com.mysql.jdbc.Driver";//加载驱动的字符串
    
    private final static String CONN_STR="jdbc:mysql://localhost:3306/school";//数据库连接字符串
    
    private final static String DB_USER="root";//数据用户
    
    private final static String DB_PWD="qazwsx@123456";//数据库登录密码
    
    //第二步：加载数据库驱动(这里是sqlserver)
    static{
        
        try{
            
            Class.forName(DRIVER_CLASS);
            
        }catch(ClassNotFoundException e){
            
            e.printStackTrace();//抛出异常
        }
    }
    
    //第三步：获取数据库连接
    public static Connection getConn(){
        
        try {
            
            return DriverManager.getConnection(CONN_STR,DB_USER,DB_PWD);
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            
        }
        return null;
    }
    
    //最后关闭数据库连接
    public static void closeConn(ResultSet rs,PreparedStatement pstmt,Connection conn){
        
        try {
            if (rs!=null) {//如果返回的结果集对象不能为空,就关闭连接
                rs.close();
            }
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        try {
            if (pstmt!=null) {
                pstmt.close();//关闭预编译对象
            }
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        try {
            
            if (conn!=null) {
                conn.close();//关闭结果集对象
            }
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
}