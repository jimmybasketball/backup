/*
静态代码块
格式：
static{
	静态代码块中执行语句	
}
特点：随着类的加载而执行，并且只会执行一次，并且还优先于主函数。
作用：用于给类进行初始化
*/
class Person{
	static{
		System.out.println("我是静态代码块");
	}
	{
		System.out.println("我是构造代码块");
	}
}
class A6_42{
	public static void main(String[] args){
		Person n= new Person();
		Person m= new Person();
	}
}





