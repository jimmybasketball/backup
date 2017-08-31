/*子父类成员特点
成员：
1.成员变量
2.函数
3.构造函数

变量：
this 代表当前对象的引用 this.变量  首先在本类中找所需要的这个变量，
如果没有找到再父类中找。
super 用于访问当前对象的父类成员，super.变量 直接父类中找所需变量
*/

class Person{
	String name="张三";
}
class Student extends Person{
	String name="李四";
	void show(){
		System.out.println(super.name);//超
	}
}
public class A6_47{
	public static void main(String[] args){
		Student stu=new Student();
		stu.show();
	}
}