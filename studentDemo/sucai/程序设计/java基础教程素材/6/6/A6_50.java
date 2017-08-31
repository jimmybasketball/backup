/*
对象的转型：
1.对象的向上转型 子类转成父类 默认进行 父类引用指向子类对象
2.对象的向下转型 父类转成子类 强制进行 

关键字：instanceof 测试左边的对象是否是右边类的实例 
如果是返回true 不是返回false
*/ 

class Animal{
	void sleep(){
		System.out.println("睡觉中");
	}	
}
class Cat extends Animal{
	void catchMouse(){
		System.out.println("捕鼠");
	}
}
class Dog extends Animal{
		
}
public class A6_50{
	public static void main(String[] args){
		Cat a=new Cat();//向上转型
		System.out.println(a instanceof Dog);
	}
}





