/*
有了子父类之后构造函数的特点：
在对子类对象进行初始化的时候，
父类的构造方法也会运行，那是因为子类的构造函数默认
第一行
有一条隐式的语句 super();

子类每个构造方法第一行都有一个隐式的 super();
*/
class fu{
	String name;
	fu(){
		System.out.println("父类构造方法");
	}
	fu(String n){
		this.name=n;
		System.out.println("父类有参构造方法");
	}
}
class zi extends fu{
	zi(){
		System.out.println("子类构造方法");	
	}
	zi(String n){
		
		this.name="小李";s
		System.out.println("子类构造方法");	
	}
}
public class A6_49{
	public static void main(String[] args){
		zi z=new zi("小李");
	}
}