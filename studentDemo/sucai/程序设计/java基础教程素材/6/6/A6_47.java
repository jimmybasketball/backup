/*�Ӹ����Ա�ص�
��Ա��
1.��Ա����
2.����
3.���캯��

������
this ����ǰ��������� this.����  �����ڱ�����������Ҫ�����������
���û���ҵ��ٸ������ҡ�
super ���ڷ��ʵ�ǰ����ĸ����Ա��super.���� ֱ�Ӹ��������������
*/

class Person{
	String name="����";
}
class Student extends Person{
	String name="����";
	void show(){
		System.out.println(super.name);//��
	}
}
public class A6_47{
	public static void main(String[] args){
		Student stu=new Student();
		stu.show();
	}
}