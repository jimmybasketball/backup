import java.util.Scanner;
public class A5_6 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		System.out.println("�붨��ѧ��������");
		int num=in.nextInt();
		int[] arr=new int[num];
		int sum=0;
		for(int i=0;i<arr.length;i++){
			System.out.println("�������"+(i+1)+"��ѧ��");
			arr[i]=in.nextInt();
			sum+=arr[i];
		}
		System.out.println(arr.length+"��ѧ����ƽ����Ϊ"+sum/arr.length);
	}
}