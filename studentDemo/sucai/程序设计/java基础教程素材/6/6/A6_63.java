/*
�ӿڣ���������ǹ��ܵ���չ�ԣ�
��Ϊ����������������֮��������(��ϵ���̶ܳ�)

�����ж�����Ƕ�����й��ԵĻ�������
�ӿ��ж��������չ����

�ӿںô���
���Զ�ʵ�֣�Ҳ����һ�������ʵ�ֶ���ӿ�
*/
class Person{
	String name;
	int age;
}
interface Smoket{
	public abstract void smoket();
}
class Student extends Person{

}
class Worker extends Person implements Smoket{
	public void smoket(){
		System.out.print("����");
	}	
}
public class A6_63{
	public static void main(String[] args){
		Worker w=new Worker();
		w.smoket();
	}
}