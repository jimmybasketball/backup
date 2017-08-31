import java.util.Scanner;
public class A5_6 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		System.out.println("请定义学生的人数");
		int num=in.nextInt();
		int[] arr=new int[num];
		int sum=0;
		for(int i=0;i<arr.length;i++){
			System.out.println("请输入第"+(i+1)+"个学生");
			arr[i]=in.nextInt();
			sum+=arr[i];
		}
		System.out.println(arr.length+"个学生的平均分为"+sum/arr.length);
	}
}