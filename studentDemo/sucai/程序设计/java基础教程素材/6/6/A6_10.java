class Cat{
	String name;
	int age;
	public String toString(){
		return "�ҵ�������"+name+"�ҵ����䣺"+age;
	}
}
public class A6_10{
	public static void main(String[] args){
		//String name=new String("���");
		Cat one=new Cat();
		one.name="С��";
		one.age=12;
		Cat two=new Cat();
		two.age=16;
		two.name="С��";
		System.out.println(one);
		System.out.println(two);
	}
}