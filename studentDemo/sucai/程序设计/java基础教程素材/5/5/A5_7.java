import java.util.Scanner;
public class A5_7 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		int[] arr=new int[5];
		for(int i=0;i<arr.length;i++){
			System.out.println("请输入第"+(i+1)+"个学生的成绩");
			arr[i]=in.nextInt();
		}
		int max=arr[0];
		for(int i=0;i<arr.length;i++){
			if(max<arr[i]){
				max=arr[i];
			}
		}
		System.out.println("最大值为："+max);
	}
}
