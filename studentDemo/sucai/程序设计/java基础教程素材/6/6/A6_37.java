/*
		          ʲôʱ��ʹ�þ�̬
static�����ε������ǳ�Ա(��Ա����������)
�����������֣�
ʲôʱ��ʹ�þ�̬�ĳ�Ա������
������ͬһ��������ж�����ֹ�������ʱ��
��Ҫ���洢����������ݵĳ�Ա������static����

ʲôʱ��ʹ�þ�̬����
�������ڲ�û�з��ʵ��Ǿ�̬�ĳ�Աʱ���������е����ݣ�
��ô�ù��ܿ��Զ���ɾ�̬��
*/
public class A6_37{
	public static void main(String[] args){
		Person A=new Person();
		A.name="Ҧ��";
		Person B=new Person();
		B.name="С��"; 
		A.print();
		B.print();
	}
}
class Person{
	String name;
	static String country;
	void print(){
		System.out.println(name+"����");
	}
}




