
public class A5_13 {
	public static  void main(String[] args){
		int[] arr={23,12,45,24,87,65,12,14,43,434,65,76};
		for(int i=0;i<arr.length-1;i++){
			for(int j=0;j<arr.length-1;j++){  //j=6
				if(arr[j]<arr[j+1]){
					int t=arr[j];
					arr[j]=arr[j+1];
					arr[j+1]=t;
				}
			}
		}
		for(int i=0;i<arr.length;i++){
			System.out.println(arr[i]);
		}
	}
}
