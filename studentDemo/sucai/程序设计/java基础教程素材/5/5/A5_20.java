import java.util.Scanner;
public class A5_20 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		System.out.println("������ѧ����������");
		int renshu=in.nextInt();//�洢ѧ��������
		System.out.println("������γ̵���Ŀ");
		int courseNum=in.nextInt();//�洢�γ̵���Ŀ
		String[] name=new String[renshu];//����һ��String���������洢ѧ��������
		String[] course=new String[courseNum];//����һ��String���������洢�γ̵�����
		int[][] number=new int[renshu][courseNum];
		
		/*
		 * ����ѭ���Ĵ洢�γ̵�����
		 * */
		for(int i=0;i<course.length;i++)
		{
			System.out.println("�붨���"+(i+1)+"�ſγ̵�����");
			course[i]=in.next();
		}
		/*
		 * ����¼��ѧ���ĸ��Ƴɼ�
		 * */
		for(int i=0;i<renshu;i++)
		{
			System.out.println("������ѧ����������");
			name[i]=in.next();
			for(int j=0;j<courseNum;j++){
				System.out.println("������ѧ��"+name[i]+course[j]+"�ĳɼ�");
				number[i][j]=in.nextInt();
			}
		}
		
	}
}
