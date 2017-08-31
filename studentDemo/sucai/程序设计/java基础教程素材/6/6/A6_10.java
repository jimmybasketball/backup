class Cat{
	String name;
	int age;
	public String toString(){
		return "我的姓名："+name+"我的年龄："+age;
	}
}
public class A6_10{
	public static void main(String[] args){
		//String name=new String("你好");
		Cat one=new Cat();
		one.name="小明";
		one.age=12;
		Cat two=new Cat();
		two.age=16;
		two.name="小红";
		System.out.println(one);
		System.out.println(two);
	}
}