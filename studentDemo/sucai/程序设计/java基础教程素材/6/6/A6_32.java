public class A6_32{
	public static void main(String[] args){
		Person A=new Person();
		Person B=new Person("小李");
		System.out.println(A.country);
		System.out.println(B.country);
		
	}	
}
/*特点：
对象一建立就运行了，而且优先于构造函数执行
作用：给对象进行初始化的
构造代码块和构造方法的区别：
构造方法是对应的对象进行初始化，
构造代码块是给所有的对象进行统一的初始化

构造代码块中定义是不同对象共性的初始化内容
*/
class Person{
	String name;
	String country;
	Person(){
		System.out.println("我是无参构造方法");
		
	}
	Person(String name){
		this.name=name;
		
		System.out.println("我是有参构造方法");
	}
	{
		country="中国";
	}
	
} 