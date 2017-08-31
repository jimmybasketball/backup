public class A6_5{
	public static void main(String[] args){
		Person One=new Person();
		

		Person Two=new Person();
		Two.age=12;
		System.out.println(Two.age);
		System.out.println(One.age);
		One.eat();
		Two.eat();
		
	}
}