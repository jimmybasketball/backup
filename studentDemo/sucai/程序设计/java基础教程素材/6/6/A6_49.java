/*
�����Ӹ���֮���캯�����ص㣺
�ڶ����������г�ʼ����ʱ��
����Ĺ��췽��Ҳ�����У�������Ϊ����Ĺ��캯��Ĭ��
��һ��
��һ����ʽ����� super();

����ÿ�����췽����һ�ж���һ����ʽ�� super();
*/
class fu{
	String name;
	fu(){
		System.out.println("���๹�췽��");
	}
	fu(String n){
		this.name=n;
		System.out.println("�����вι��췽��");
	}
}
class zi extends fu{
	zi(){
		System.out.println("���๹�췽��");	
	}
	zi(String n){
		
		this.name="С��";s
		System.out.println("���๹�췽��");	
	}
}
public class A6_49{
	public static void main(String[] args){
		zi z=new zi("С��");
	}
}