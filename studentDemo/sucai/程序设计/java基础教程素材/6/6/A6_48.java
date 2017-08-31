/*
动物
猫，狗

当子类中出现和父类中一样的函数时，当子类对象调用该函数，
运行的是子类中的函数,如同父类中的函数被覆盖了一样，
这种情况就是函数的另一种特性：重写（覆盖）

注意：
1.子类覆盖父类时，必须要保证子类权限大于等于父类，才可以覆盖，否则编译出错

访问权限修饰符：public> default>private   成员前面没加任何访问权限
修饰符默认权限就是default

2.静态的函数只能覆盖静态的

记住：
重载：只看同名的方法的参数列表
重写：子父类方法要一模一样

*/

class Animal{
	String Type;
	public void run(){
		System.out.println("跑步中");
	}	
}
class Cat extends Animal{
	
} 
class Dog extends Animal{ 
	public void run(String a){
		Type="大黄狗";
		System.out.println(Type+"哼着歌跑步中");
		
	}
	
}
public class A6_48{
	public static void main(String[] args){
		Cat C=new Cat();
		C.run();
		Dog d=new Dog();
		d.run();
		d.run("dd");
	}
}