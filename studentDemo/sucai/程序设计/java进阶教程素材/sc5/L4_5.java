import java.util.*;
public class L4_5 
{
    public static void main(String[] args) 
    {
	    HashMap ee=new HashMap();
	    Sp sp1=new Sp("001","�㳦",20);
	    Sp sp2=new Sp("002","����",10);
	    Sp sp3=new Sp("003","���",2);
	    ee.put("002",sp1);
	    ee.put("002",sp2);  // ��  ֵ
	    ee.put("002",sp3);
	    
	   /* if(ee.containsKey("002"))  // ����Ҫ����
	    {
	    	System.out.println("��ʳƷ��ϢΪ");
	    	Sp sp=(Sp)ee.get("002");
	    	System.out.println(sp.getMingcheng());
	    	System.out.println(sp.getJiage());
	   }	    
	    else
	    {
	    	System.out.println("�Բ���û�и�ʳƷ��");	    
	    }*/
	    Iterator it=ee.keySet().iterator();
	    while(it.hasNext())
	    {
	    	String key=it.next().toString();
	    	Sp sp=(Sp)ee.get(key);
	    	System.out.println("ʳƷ����:"+sp.getMingcheng());
	    	System.out.println("ʳƷ�۸�:"+sp.getJiage()+"Ԫ");
	    }
	}
}
