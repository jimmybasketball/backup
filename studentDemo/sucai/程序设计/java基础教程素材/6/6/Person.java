public class Person{
	String name;
	int age;
	char Gender;
	
	/*
	无参无返回  方法
	*/
	public void eat(){
		System.out.println("我在吃饭");	
	}
	
	/*
	有参无返回
	*/
	public void sleep(String A){
		System.out.println("我在"+A+"睡觉");
	}
	
	/*
	有参有返回
	*/
	public int getAge(int a){
		return a;
	}
}





