/*
������һ�δ�������������ʱ��
ԭ������ʱ��-��ʼʱ�� System.currentTimeMillis()
ʲô��ģ�巽�����ģʽ��
���幦��ʱ�����ܵ�һ������ȷ������ȷ���Ĳ�����ʹ�ò�ȷ����
��ô��ʱ�͵ý���ȷ���Ĳ��ֱ�¶��ȥ���ɸ�����ȥ��ɡ�
*/
abstract class Demo{
	public final void getTime(){
		long start =System.currentTimeMillis();//1��=1000����
		function();	
		long stop =System.currentTimeMillis();
		System.out.println("��������"+(stop-start));
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


