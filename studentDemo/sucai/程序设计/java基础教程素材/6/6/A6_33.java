public class A6_33{
	public static void main(String[] args){
		Student B=new Student("fd");
		
	}	
}
/*
this:����ȥ���������־ֲ������ͳ�Ա����ͬ�������
this:���Ǵ��������this���������ں���(����)�������������

���캯��֮��ĵ���ֻ��ͨ��this��������

���캯��֮����е���ʱthis���ֻ�ܳ����ڵ�һ��,��ʼ��Ҫ��ִ�У������ʼ�����л��г�ʼ�����Ǿ�ȥִ�и�ϸ�ڵĳ�ʼ��
*/
class Student{
	String name;
	int age;
	Student(){
		System.out.println("�޲ι��췽��");
	}
	Student(String name){
		this();
		this.name=name;
		System.out.println("fdfd");
		
	}
	Student(String name,int age){
		this(name);
		this.age=age;
	}		
}