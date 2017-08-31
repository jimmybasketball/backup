/*
继承概述_2
子类拥有父类的成员,子类不能拥有父类中被private修饰后的成员

java多层继承 在多层继承中最下层的子类拥有整个继承体系的成员，
最上层的父类里面定义所有子类的共性描述

java中不支持多继承，只支持单继承。因为多继承存在安全隐患,
当多个父类定义了相同的成员，子类对象不确定运行哪一个。
java保留这种机制，并用了另一方式来体现 多实现
*/
class A{
	String name;
	void show(){
	
	}
}
class B{
	int age;
	void show(){
	
	}
}
class C extends A{

}
class A6_46{
	public static void main(String[] args){
		C c=new C();
		c.show();
	}
}