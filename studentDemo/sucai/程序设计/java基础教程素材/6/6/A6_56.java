/*
一个特殊的类
Object:它是java中所有对象的直接或者间接父类，根父类（基类），
它里面定义的功能时所有对象都应该具备的

记住：当定义一个新类时，没有指明要继承某类，它默认继承Object类

==和Object里面的equals其实比较的就两对象的内存地址是否一致

*/
class Cat{
	int num;
	public boolean equals(Object fff){
		if(fff instanceof Cat){
			Cat ee =(Cat) fff;
			return	this.num==ee.num;
		}else{
			return false;
		}
		
	}
}
class Dog{

}
public class A6_56{
	public static void main(String[] args){
		System.out.println("ff".equals("ff"));
	}
}


