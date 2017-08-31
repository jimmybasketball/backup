import java.util.Arrays;
class Student{
	public void max(int a,int b){
		System.out.println(a>b?a:b);	
	}
	public void max(double a,double b){
		System.out.println("第二个方法"+(a>b?a:b));
	}
	public void max(double a,double b,double c){
		double max=a>b?a:b;
		System.out.println("第三个方法"+(max>c?max:c));
	}
}
public class A6_13{
	public static void main(String[] args){
		double[] arr={43.5,5.46,61.5,7.6,7.8};
		Arrays.sort(arr);
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i]);
		}
			
	}
}