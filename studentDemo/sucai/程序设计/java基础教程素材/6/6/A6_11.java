class Cat{
	String name;
	int age;
	String color;
	public void set(String name,int age,String color){
		this.name=name;
		this.age=age;
		this.color=color;
	}
	public String toString(){
		return "姓名:"+name+"\t年龄:"+age+"\t颜色:"+color;
	}
	public Cat abc(){
		return this;
	}
}
public class A6_11{
	public static void main(String[] args){
		Cat one=new Cat();
		Cat two=new Cat();
		one.set("第一只猫",5,"黑色");
		two.set("第二只猫",6,"紫色");
		Cat three=new Cat();
		three=two.abc();
		//three=two;
		System.out.println(one);
		System.out.println(two);
		System.out.println(three);
	}
}