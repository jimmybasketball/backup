/*
����
è����

�������г��ֺ͸�����һ���ĺ���ʱ�������������øú�����
���е��������еĺ���,��ͬ�����еĺ�����������һ����
����������Ǻ�������һ�����ԣ���д�����ǣ�

ע�⣺
1.���า�Ǹ���ʱ������Ҫ��֤����Ȩ�޴��ڵ��ڸ��࣬�ſ��Ը��ǣ�����������

����Ȩ�����η���public> default>private   ��Աǰ��û���κη���Ȩ��
���η�Ĭ��Ȩ�޾���default

2.��̬�ĺ���ֻ�ܸ��Ǿ�̬��

��ס��
���أ�ֻ��ͬ���ķ����Ĳ����б�
��д���Ӹ��෽��Ҫһģһ��

*/

class Animal{
	String Type;
	public void run(){
		System.out.println("�ܲ���");
	}	
}
class Cat extends Animal{
	
} 
class Dog extends Animal{ 
	public void run(String a){
		Type="��ƹ�";
		System.out.println(Type+"���Ÿ��ܲ���");
		
	}
	
}
public class A6_48{
	public static void main(String[] args){
		Cat C=new Cat();
		C.run();
		Dog d=new Dog();
		d.run();
		d.run("dd");
	}
}