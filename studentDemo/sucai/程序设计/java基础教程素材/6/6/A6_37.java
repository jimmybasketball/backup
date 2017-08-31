/*
		          什么时候使用静态
static所修饰的内容是成员(成员变量，方法)
从两方面入手：
什么时候使用静态的成员变量：
当属于同一个类的所有对象出现共享数据时，
需要将存储这个共享数据的成员变量用static修饰

什么时候使用静态方法
当功能内部没有访问到非静态的成员时（对象特有的数据）
那么该功能可以定义成静态的
*/
public class A6_37{
	public static void main(String[] args){
		Person A=new Person();
		A.name="姚明";
		Person B=new Person();
		B.name="小明"; 
		A.print();
		B.print();
	}
}
class Person{
	String name;
	static String country;
	void print(){
		System.out.println(name+"您好");
	}
}




