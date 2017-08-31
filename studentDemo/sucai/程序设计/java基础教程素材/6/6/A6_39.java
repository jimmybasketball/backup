/*
	静态应用工具类
每一个应用程序都有共性的功能，
可以将这些功能进行抽取，独立封装。以使复用
对象的作用：对象是用来封装数据的,
只要这个类里面有非静态的成员时，
那对象就必须存在。
*/
public class A6_39{
	public static void main(String[] args){
		int[] arr={232,43,535,343,23,43,3,4};
		int[] arr1={34,43,232,34344};
		System.out.println(ArrayOperate.max(arr));
	}
}





