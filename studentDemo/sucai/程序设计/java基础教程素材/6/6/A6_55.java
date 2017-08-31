/*
一个特殊的类
Object:它是java中所有对象的直接或者间接父类，根父类（基类），
它里面定义的功能时所有对象都应该具备的

记住：当定义一个新类时，没有指明要继承某类，它默认继承Object类

*/

class Cat{
	public String toString(){
		return "你好";
	}
}

class A6_55{
	public static void main(String[] args){
		System.out.println(new Cat().toString());
	}
}



