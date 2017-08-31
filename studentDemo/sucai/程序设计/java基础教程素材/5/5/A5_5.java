import java.util.Scanner;
public class A5_5 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		int[] arr=new int[5];
		int sum=0;
		for(int i=0;i<5;i++){
			System.out.println("请输入第"+(i+1)+"个学生");
			arr[i]=in.nextInt();
			sum+=arr[i];
		}
		System.out.println("5个学生的平均分为"+sum/5);
	}
}
