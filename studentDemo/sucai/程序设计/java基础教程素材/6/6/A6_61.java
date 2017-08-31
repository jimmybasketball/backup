/*
接口：初步理解，接口是一个特殊的抽象类，当抽象类中全部是抽象方法时，
可以通过接口的形式来体现。
class 用于定义类
interface 用于定义接口

接口中成员的格式:
1.public static final String NAME="小李";//全局常量
2.public abstract void show(); //抽象方法

注意：
1.接口中只能出现public 访问修饰符
2.接口不可以创建对象，因为里面有抽象方法，需要被子类实现，子类对接口中的所有抽象方法实现后，子类才能够实例化，否则子类就是一个抽象类
*/
interface smoket{
	public static final String NAME="小李";
	public abstract void show();
}
class Student implements smoket{
	public void show(){
		System.out.println("在吸烟");
	}
}
public class A6_61{
	public static void main(String[] args){
		Student s=new Student();
		s.show();	
	}
}



