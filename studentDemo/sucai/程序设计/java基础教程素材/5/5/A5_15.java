public class A5_15{
	public static void main(String[] args){
		int[] arr={34,54,6,87,98,54,3432,1};
		for(int i=0;i<arr.length-1;i++){
			for(int j=i+1;j<arr.length;j++){
				if(arr[i]>arr[j]){
					int t=arr[i];
					arr[i]=arr[j];
					arr[j]=t;
				}
			}
		}
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i]);
		}
	}
}