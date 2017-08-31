/*
	文档注释 文档标识符
*/
/**
此类是对数组进行取最值，排序等操作的
@author 张三
@version 1.0
*/
public class ArrayOperate{
	private ArrayOperate(){}
	/**
	取int数组里面的最大值
	@param arr 传入一个int数组
	@return 返回一个int数值
	*/
	public static int max(int[] arr){
		int max=arr[0];
		for(int i=0;i<arr.length;i++){
			if(arr[i]>max){
				max=arr[i];
			}
		}
		return max;
	}
	/**
	取int数组里面的最小值
	@param arr 传入一个int数组
	@return 返回一个int数值
	*/
	public static int min(int[] arr){
		int min=arr[0];
		for(int i=0;i<arr.length;i++){
			if(arr[i]<min){
				min=arr[i];
			}
		}
		return min;
	}
	/**
	对传入int数组里面进行冒泡排序
	@param arr 传入一个int数组
	*/
	public static void maoSort(int[] arr){//冒泡排序
		for(int i=0;i<arr.length-1;i++){
			for(int j=0;j<arr.length-1;j++){
				replace(arr,j,j+1);
			}
		}	
	}
	/**
	对传入int数组里面进行选择排序
	@param arr 传入一个int数组
	*/
	public static void xuanzeSort(int[] arr){//选择排序
		for(int i=0;i<arr.length-1;i++){
			for(int j=i+1;j<arr.length;j++){
				replace(arr,i,j);
			}
		}	
	}
	private static void replace(int[] arr,int a,int b){
		if(arr[a]>arr[b]){
			int t=arr[a];
			arr[a]=arr[b];
			arr[b]=t;
		}
	}
}













