import java.util.*;
import java.io.*;
public class L4_3
{
	public static void main(String[] args) 
	{
        LinkedList bb= new LinkedList();
        Sp sp1=new Sp("001","��÷",5f);
        Sp sp2=new Sp("002","��Ƭ",8f);
        
       // bb.addFirst(sp1); //����ȳ����ȷŽ�ȥ���������棬��Ž�ȥ�ģ���������
        //bb.addFirst(sp2);
        
       //for(int i=0;i<bb.size();i++)
       //{
        	//System.out.println(((Sp)bb.get(i)).getMingcheng());
        	//System.out.println(((Sp)bb.getFirst()).getMingcheng());
            //System.out.println(((Sp)bb.getLast()).getMingcheng());
         //}      
        
        bb.addLast(sp1);
        bb.addLast(sp2);
        
        for(int i=0;i<bb.size();i++)
        {
        	//System.out.println(((Sp)bb.get(i)).getMingcheng());
        	//System.out.println(((Sp)bb.getFirst()).getMingcheng());
            System.out.println(((Sp)bb.getLast()).getMingcheng());
        } 
        
	}
}
