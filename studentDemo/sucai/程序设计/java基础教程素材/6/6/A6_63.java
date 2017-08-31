/*
接口：提高了我们功能的扩展性，
因为它降低事物与事物之间的耦合性(关系紧密程度)

父类中定义的是多个类中共性的基本功能
接口中定义的是扩展功能

接口好处：
可以多实现，也就是一个类可以实现多个接口
*/
class Person{
	String name;
	int age;
}
interface Smoket{
	public abstract void smoket();
}
class Student extends Person{

}
class Worker extends Person implements Smoket{
	public void smoket(){
		System.out.print("抽烟");
	}	
}
public class A6_63{
	public static void main(String[] args){
		Worker w=new Worker();
		w.smoket();
	}
}