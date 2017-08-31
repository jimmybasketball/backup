/*
abstract:是一个修饰符
可以修饰方法，类
1.当我们多个类存在相同的功能，但是功能的主体不同，这时我进行向上抽取
只抽取功能的定义不抽取功能的主体

抽象类的特点：
1.抽象方法一定要在抽象类中
2.抽象类和抽象方法必须要被abstract关键字修饰
  abstract只能修饰类和函数
3.抽象的类是不能被创建对象，因为调用抽象的方法没意义
4.抽象类的中方法要被使用，必须由子类重写抽象类中的方法
，然后创建子类对象来调用
5.抽象类中可以定义非抽象的方法,有时我们需要此类不能被new关键字创建对象时，
可以用abstract将此类变成抽象类。
6.子类如果只重写一部分的抽象方法，那么该子类还是一个抽象类，如果抽象类的方法
要被使用，子类必须重写抽象类中的所有方法。

值得注意：抽象类和普通的类没有太大的不同。
1.抽象类无法同过new关键字创建对象
2.抽象类里面可有抽象的方法


*/
abstract class Animal{
	abstract void sing();
	abstract void show();
}
class Cat extends Animal{
	void sing(){
		System.out.println("喵喵唱歌");
	}
	void show(){
	
	}
}
class Dog extends Animal{
	void sing(){
		System.out.println("汪汪唱歌");
	}
	void show(){
	
	}
}
public class A6_58{
	public static void main(String[] args){
		Animal t=new Cat();
		t.sing();
	}
}