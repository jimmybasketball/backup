public class A6_32{
	public static void main(String[] args){
		Person A=new Person();
		Person B=new Person("С��");
		System.out.println(A.country);
		System.out.println(B.country);
		
	}	
}
/*�ص㣺
����һ�����������ˣ����������ڹ��캯��ִ��
���ã���������г�ʼ����
��������͹��췽��������
���췽���Ƕ�Ӧ�Ķ�����г�ʼ����
���������Ǹ����еĶ������ͳһ�ĳ�ʼ��

���������ж����ǲ�ͬ�����Եĳ�ʼ������
*/
class Person{
	String name;
	String country;
	Person(){
		System.out.println("�����޲ι��췽��");
		
	}
	Person(String name){
		this.name=name;
		
		System.out.println("�����вι��췽��");
	}
	{
		country="�й�";
	}
	
} 