public class A5_10 {
	public static void main(String[] args){
		int[] arrA={1,2,3,4,5,6,7,8};
		int[] arrB=new int[arrA.length];
		for(int i=0;i<arrA.length;i++){
			arrB[i]=arrA[i];
		}
		for(int i=0;i<arrA.length;i++){
			System.out.println(arrB[i]);
		}
	}
}
