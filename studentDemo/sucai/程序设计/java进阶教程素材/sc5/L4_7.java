import java.util.*;
import java.lang.reflect.*;
public class L4_7 
{
	public static void main(String[] args) 
	{
		//Bh<String> hw1=new Bh<String>("����1"); 
		Bh<Integer> hw2=new Bh<Integer>(123); 
		//Bh<Wj> hw3=new Bh<Wj>(new Wj());
		hw2.lxmc();
	}
}
class Wj
{
	public void wan()
	{
		System.out.println("���������Ϸ����");
	}
	public int jiafa(int a,int b)
	{
		return a+b;
	}
}


class Bh<L>
{
   private L l;
   
   Bh(L l)
   {
	   this.l=l;
   }
   
   public void lxmc()
   {
	   System.out.println("������"+l.getClass().getName());//ȡ��������
	   //Method []a=l.getClass().getDeclaredMethods();
	   /*for(int i=0;i<a.length;i++)
	   {
		   System.out.println("������Ϊ"+a[i].getName());
	   }*/
   }
}
