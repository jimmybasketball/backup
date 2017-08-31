/*
需求：求一段代码运行所花费时间
原理：结束时间-开始时间 System.currentTimeMillis()
什么是模板方法设计模式？
定义功能时，功能的一部分是确定，而确定的部分在使用不确定，
那么这时就得将不确定的部分暴露出去，由该子类去完成。
*/
abstract class Demo{
	public final void getTime(){
		long start =System.currentTimeMillis();//1秒=1000毫秒
		function();	
		long stop =System.currentTimeMillis();
		System.out.println("共花费了"+(stop-start));
	} 
	public abstract void function();
}
class Test extends Demo{
	public void function(){
		for(int i=0;i<1000;i++){
			System.out.print("f");
		}
	}
}
public class A6_60{
	public static void main(String[] args){
		Test t=new Test();
		t.getTime();
	}
}


