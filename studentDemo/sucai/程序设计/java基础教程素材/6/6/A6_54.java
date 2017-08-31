/*
匿名对象:没有名字的对象
匿名对象的使用方式之一：当对对象的方法只调用一次时，我们可以用匿名对象来完成
，比较简化。
匿名对象的使用方式之二：匿名对象可以被当做实参传递
*/
class Car{
	String color;
	void start(){
		System.out.println("汽车被开动");
	}
	
}
public class A6_54{
	public static void main(String[] args){
		print(new Car());
	}
	static void print(Car c){// Car c=new Car(); 
		c.start();
	}
}



