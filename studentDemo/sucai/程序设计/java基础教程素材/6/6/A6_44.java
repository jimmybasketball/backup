/*
设计模式：针对此类问题最有效的解决方法
java23种设计模式
单例设计模式：解决一个类只在内存中存在一个对象

如何让一个类在内存中只存在一个对象？
1.禁止其他的应用程序，通过此类来创建对象
2.既然外部不能通过此类创建对象了，我们要用对象，
就可以再本类里面创建本类对象。
3.为了其他应用程序能够访问到本类里面创建的对象，
我需要对外界提供一种访问方式

如何用代码体现出来：
1.私有化构造方法
2.在本类中创建本类对象
3.对外界提供访问的方法
*/
class Person{
	String name;
	private Person(){}
	static Person p=new Person();
	public static Person getInstance(){
		return p;
	}
}
public class A6_44{
	public static void main(String[] args){
		Person A=Person.getInstance();
		A.name="小明";
		Person B=Person.getInstance();
		System.out.println(B.name);
	}
}










