/*
�����ת�ͣ�
1.���������ת�� ����ת�ɸ��� Ĭ�Ͻ��� ��������ָ���������
2.���������ת�� ����ת������ ǿ�ƽ��� 

�ؼ��֣�instanceof ������ߵĶ����Ƿ����ұ����ʵ�� 
����Ƿ���true ���Ƿ���false
*/ 

class Animal{
	void sleep(){
		System.out.println("˯����");
	}	
}
class Cat extends Animal{
	void catchMouse(){
		System.out.println("����");
	}
}
class Dog extends Animal{
		
}
public class A6_50{
	public static void main(String[] args){
		Cat a=new Cat();//����ת��
		System.out.println(a instanceof Dog);
	}
}





