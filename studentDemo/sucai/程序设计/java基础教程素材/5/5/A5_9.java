import java.util.*;
public class A5_9 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		String[] arr=new String[7];
		for(int i=0;i<arr.length;i++){
			System.out.println("ÇëÊäÈëµÚ"+(i+1)+"¸ö×Ö·û");
			arr[i]=in.next();
		}
		Arrays.sort(arr);
		for(int i=arr.length-1;i>=0;i--){
			System.out.println(arr[i]);
		}
	}
}
