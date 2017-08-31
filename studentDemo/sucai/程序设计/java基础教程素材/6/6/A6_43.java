class Student{
	static{
		System.out.println("我是静态代码块");
	}
	{
		System.out.println("我是构造代码块");
	}
	void print(){
		System.out.println("我是普通方法");
	}
	private String name;
	private static String country;
}
public class A6_43{
	public static void main(String[] args){
		Student s=new Student();
	}
}
/*
1.因为new Student()用到了Student类，所以会把它从硬盘上加载进入内存
2.如果有static静态代码块就会随着类的加载而执行，
还有静态成员和普通方法也会随着类的加载而被加载
3.在堆中开辟空间，分配内存地址
4.在堆中建立对象特有属性，并同时对特有属性进行默认初始化
5.对属性进行显示初始化
6.执行构造代码块。对所有对象进行初始化
7.执行对应的构造函数，对对象进行初始化.
8.将内存地址给S(给栈中的变量)
*/









*/