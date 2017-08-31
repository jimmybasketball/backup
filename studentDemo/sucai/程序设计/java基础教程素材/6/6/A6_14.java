class Cat{
	private String name;
	private int age;
	Cat(String name,int age){
		this.name=name;
		this.age=age;
	}
	Cat(){
	
	}
	void get(){
		System.out.println(this.name);
	} 
}
public class A6_14{
	public static void main(String[] args){
		Cat one=new Cat();
		one.get();
		Cat two=new Cat("Ð¡»¨",2);
		two.get();
	}	
}