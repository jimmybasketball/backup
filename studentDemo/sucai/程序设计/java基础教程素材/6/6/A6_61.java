/*
�ӿڣ�������⣬�ӿ���һ������ĳ����࣬����������ȫ���ǳ��󷽷�ʱ��
����ͨ���ӿڵ���ʽ�����֡�
class ���ڶ�����
interface ���ڶ���ӿ�

�ӿ��г�Ա�ĸ�ʽ:
1.public static final String NAME="С��";//ȫ�ֳ���
2.public abstract void show(); //���󷽷�

ע�⣺
1.�ӿ���ֻ�ܳ���public �������η�
2.�ӿڲ����Դ���������Ϊ�����г��󷽷�����Ҫ������ʵ�֣�����Խӿ��е����г��󷽷�ʵ�ֺ�������ܹ�ʵ�����������������һ��������
*/
interface smoket{
	public static final String NAME="С��";
	public abstract void show();
}
class Student implements smoket{
	public void show(){
		System.out.println("������");
	}
}
public class A6_61{
	public static void main(String[] args){
		Student s=new Student();
		s.show();	
	}
}



