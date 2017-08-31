/*
java不支持多继承,因为存在安全隐患,当父类中定义了相同功能，
功能内容不同时,子类不确定要运行哪一个
但是java保留了这种机制，并用另一种体现形式来完成表示>>多实现

关系：
类与类之间：继承关系
接口与类之间：实现关系
接口与接口之间：继承关系
*/
interface Smoket{
	public abstract void smoket();
}
interface Game extends Smoket{
	public abstract void computerGame();
}
class Student{
	public void smoket(){
		System.out.println("吸烟");
	}
	public void computerGame(){
		
	}
} 
public class A6_62{
	public static void main(String[] args){
		
	}
}



