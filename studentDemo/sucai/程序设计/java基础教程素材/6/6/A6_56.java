/*
һ���������
Object:����java�����ж����ֱ�ӻ��߼�Ӹ��࣬�����ࣨ���ࣩ��
�����涨��Ĺ���ʱ���ж���Ӧ�þ߱���

��ס��������һ������ʱ��û��ָ��Ҫ�̳�ĳ�࣬��Ĭ�ϼ̳�Object��

==��Object�����equals��ʵ�Ƚϵľ���������ڴ��ַ�Ƿ�һ��

*/
class Cat{
	int num;
	public boolean equals(Object fff){
		if(fff instanceof Cat){
			Cat ee =(Cat) fff;
			return	this.num==ee.num;
		}else{
			return false;
		}
		
	}
}
class Dog{

}
public class A6_56{
	public static void main(String[] args){
		System.out.println("ff".equals("ff"));
	}
}


