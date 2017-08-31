public class A6_33{
	public static void main(String[] args){
		Student B=new Student("fd");
		
	}	
}
/*
this:看上去，用来区分局部变量和成员变量同名的情况
this:就是代表本类对象，this代表它所在函数(方法)所属对象的引用

构造函数之间的调用只能通过this语句来完成

构造函数之间进行调用时this语句只能出现在第一行,初始化要先执行，如果初始化当中还有初始化，那就去执行更细节的初始化
*/
class Student{
	String name;
	int age;
	Student(){
		System.out.println("无参构造方法");
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