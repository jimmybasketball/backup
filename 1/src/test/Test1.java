package test;
import java.sql.*;
public class Test1 {
	public static void main(String[] args) throws Exception{
	Class.forName("com.mysql.jdbc.Driver");//��������
    
    String jdbc="jdbc:mysql://testdb-cq.fq.com:3306/haitao_supplychain?characterEncoding=GBK";
    Connection conn=DriverManager.getConnection(jdbc, "sfaitao", "sfhaitaotest");//���ӵ����ݿ�
    
    Statement state=conn.createStatement();   //����
    String sql="INSERT INTO `haitao_supplychain`.`sc_warehouse` (`id`, `name`, `warehouse_nid`, `warehouse_code`, `logistics_provider_nid`, `logistics_provider_id`, `principal_email`, `region`, `warehouse_type`, `cooperation_state`, `warehouse_state`, `contract_period_start`, `contract_period_end`, `is_storage`, `is_return`, `is_transit`, `is_support_batch`, `country`, `province`, `city`, `district`, `address`, `zipcode`, `sender_name`, `sender_telephone`, `sender_country`, `sender_province`, `sender_city`, `sender_district`, `sender_address`, `sender_zipcode`, `contact_name`, `contact_email`, `contact_cellphone`, `contact_telephone`, `gmt_create`, `gmt_modified`, `updated_time_for_bi`, `is_delete`) VALUES ('63', '�������ڲ�1', 'TEST005', 'TEST005', 'SF_TMS', '3', 'JIMMY@ifunq.com', '1', 'DOMESTIC', 'IN_PROCESS', 'ENABLE', '2017-07-26 14:50:47', '2017-07-21 14:50:51', '1', '0', '0', '1', '�й�', '����', '������', '�ϰ���', '�������ϰ�����ƺ��·36��', '400000', '���Թ�Ӧ��', '400-160-3366', '�й�', '����', '������', '�ϰ���', '�������ϰ�����ƺ��·36��', '400000', 'matt', '1@1.com', '13802561147', '0561-001', '2017-07-11 08:01:28', '2017-08-10 14:51:22', '2017-08-14 14:22:15', '0');"
;   //SQL���
    state.executeUpdate(sql);         //��sql����ϴ������ݿ�ִ��
    
    conn.close();//�ر�ͨ��
}
}