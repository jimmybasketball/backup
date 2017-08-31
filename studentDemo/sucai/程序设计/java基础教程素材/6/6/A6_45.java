/*
	继承
继承的概述：
1.提高了代码复用性，简化了代码
2.让类与类之间产生了继承关系，才有了后面的多态特性的存在
注意：千万不要为了获取其他类的功能简化代码，而建立继承关系，
必须要类与类之间存在继承关系， 继承关系：is a
*/
//父类，超类，基类

//子类,导出类
Person{
	String name;
	int age;
}
class Student extends Person{
	void study(){
		System.out.println("学习");
	}
}
class Teather extends Person{
	void teath(){
		System.out.println("教书");
	}
}