
public class A5_17 {
	public static void main(String[] args){
		int[][] arr=new int[3][];
		arr[0]=new int[3];
		arr[1]=new int[2];
		arr[2]=new int[5];
		for(int i=0;i<arr[0].length;i++){
			arr[0][i]=i+5;         // arr[0][0]=5  arr[0][1]=6   arr[0][2]=7
		}
		for(int i=0;i<arr[0].length;i++){
			System.out.println(arr[0][i]);
		}
	}
}
