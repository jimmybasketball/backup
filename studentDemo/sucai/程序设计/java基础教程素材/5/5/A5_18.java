import java.util.*;
public class A5_18 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		System.out.println("������ѧ����������");
		int renshu=in.nextInt();
		System.out.println("������γ̵���Ŀ");
		int courseNum=in.nextInt();
		String[] name=new String[renshu];
		String[] course=new String[courseNum];
		for(int i=0;i<course.length;i++)
		{
			System.out.println("�붨���"+(i+1)+"�ſγ̵�����");
			course[i]=in.next();
		}
		
	}
}
