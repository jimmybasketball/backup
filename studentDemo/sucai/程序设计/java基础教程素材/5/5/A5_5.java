import java.util.Scanner;
public class A5_5 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		int[] arr=new int[5];
		int sum=0;
		for(int i=0;i<5;i++){
			System.out.println("�������"+(i+1)+"��ѧ��");
			arr[i]=in.nextInt();
			sum+=arr[i];
		}
		System.out.println("5��ѧ����ƽ����Ϊ"+sum/5);
	}
}
