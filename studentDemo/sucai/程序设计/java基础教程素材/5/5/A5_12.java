import java.util.Scanner;
public class A5_12 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		int[] arr={23,32,57,78,98,43};
		System.out.println("请输入你要插入的数值：");
		int num=in.nextInt();
		arr[arr.length-1]=num;
		for(int i=arr.length-1;i>0;i--){
			if(arr[i]<arr[i-1]){
				int t=arr[i];
				arr[i]=arr[i-1];
				arr[i-1]=t;
			}else{
				break;
			}
		}
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i]);
		}
	}
}
