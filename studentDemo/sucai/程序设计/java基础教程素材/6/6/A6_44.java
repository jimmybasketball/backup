/*
���ģʽ����Դ�����������Ч�Ľ������
java23�����ģʽ
�������ģʽ�����һ����ֻ���ڴ��д���һ������

�����һ�������ڴ���ֻ����һ������
1.��ֹ������Ӧ�ó���ͨ����������������
2.��Ȼ�ⲿ����ͨ�����ഴ�������ˣ�����Ҫ�ö���
�Ϳ����ٱ������洴���������
3.Ϊ������Ӧ�ó����ܹ����ʵ��������洴���Ķ���
����Ҫ������ṩһ�ַ��ʷ�ʽ

����ô������ֳ�����
1.˽�л����췽��
2.�ڱ����д����������
3.������ṩ���ʵķ���
*/
class Person{
	String name;
	private Person(){}
	static Person p=new Person();
	public static Person getInstance(){
		return p;
	}
}
public class A6_44{
	public static void main(String[] args){
		Person A=Person.getInstance();
		A.name="С��";
		Person B=Person.getInstance();
		System.out.println(B.name);
	}
}










