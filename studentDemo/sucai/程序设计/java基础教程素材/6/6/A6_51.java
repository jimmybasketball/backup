/*
OOP��������:��װ���̳У���̬
��̬���������Ϊ������ڵĶ���������̬
���è��������
�� ���� Ů��

1.��̬������
��������ָ���������
2.��̬��ǰ��
��1�������������֮����ڹ�ϵ�������Ǽ̳й�ϵ Ҳ������ʵ�ֹ�ϵ
��2�����������д 
3.��̬�ĺô�
�������˳���Ŀ���չ��
4.��̬�ı׶�
��ߵĳ������չ�ԣ�ֻ���ø�������ã����ʵ������г�Ա
��̬��Ա�������ص㣺
1.�����ڼ䣺���ĵ��������ͱ��������������Ƿ��е��õķ���
2.�����ڼ䣺���Ķ��������������Ƿ��е��õķ���
*/
class Animal{
	
}
class Dog extends Animal{
	void sing(){
		System.out.println("�����ĳ���");
	}
}
class Cat extends Animal{
	void sing(){
		System.out.println("�����ĳ���");
	}
	void catchMouse(){
		System.out.println("����");
	}
}
public class A6_51{
	public static void main(String[] args){
		Cat c1=new Cat();
		function(c1);
	}
	public static void function(Animal c){//Animal c=new Cat();
		Cat c1=(Cat)c;
		c1.catchMouse();
	}
}









