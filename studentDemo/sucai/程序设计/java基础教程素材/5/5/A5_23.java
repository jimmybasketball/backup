import java.util.Scanner;
public class A5_23 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		System.out.println("������ѧ����������");
		int renshu=in.nextInt();//�洢ѧ��������
		System.out.println("������γ̵���Ŀ");
		int courseNum=in.nextInt();//�洢�γ̵���Ŀ
		String[] name=new String[renshu];//����һ��String���������洢ѧ��������
		String[] course=new String[courseNum];//����һ��String���������洢�γ̵�����
		int[][] number=new int[renshu][courseNum];
		int[] sum=new int[renshu];
		int[] avg=new int[renshu];
		String[] str=new String[renshu];
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
			int S=0;//����һ��S����ʼֵΪ0
			System.out.println("������ѧ����������");
			name[i]=in.next();//�����洢ѧ��������
			String str11="";
			for(int j=0;j<courseNum;j++){
				System.out.println("������ѧ��"+name[i]+course[j]+"�ĳɼ�");
				number[i][j]=in.nextInt();//�����洢�ض�ѧ�����ض��ɼ�
				S+=number[i][j];
				str11+=number[i][j]+"\t";
			}
			sum[i]=S;//�����ͬѧ���ִܷ����ܷ�����
			avg[i]=S/courseNum;//�����ͬѧ��ƽ���ִ���ƽ��������
			str[i]=name[i]+"\t"+str11+sum[i]+"\t"+avg[i];
		}
		for(int i=0;i<sum.length-1;i++)
		{
			for(int j=0;j<sum.length-1;j++){
				if(sum[j]<sum[j+1]){
					int t1=sum[j];    String t2=str[j];
					sum[j]=sum[j+1];  str[j]=str[j+1];
					sum[j+1]=t1;      str[j+1]=t2;
				}
			}
		}
		/*
		 * Ч�����
		 * */
		System.out.print("ѧ��");
		for(int i=0;i<course.length;i++)
		{
			System.out.print("\t"+course[i]);//ѭ�����ÿ����Ŀ������
		}
		System.out.print("\t�ܷ�\tƽ����\t���а�");
		System.out.println();
		for(int i=0;i<renshu;i++){
			System.out.println(str[i]+"\t��"+(i+1)+"��");//���е�����
		}
	}
}
