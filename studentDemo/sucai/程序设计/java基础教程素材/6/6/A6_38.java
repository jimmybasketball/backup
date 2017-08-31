/*
		      主函数(主方法)
1.public （访问修饰符，公共的）代表该类或者该函数访问权限是最大的
2.static  代表主函数随着类的加载就已经存在了
3.void    代表主函数没有具体的返回值
4.main    main不是java中的关键字，它是一个特殊的单词，
它能够被JVM（java虚拟机）所识别
5.String[] args 函数的参数，参数类型是一个字符串数组
，该数组的元素是字符串

*/
public class A6_38{
	public static void main(String[] a){
		for(int i=0;i<a.length;i++){
			System.out.print(a[i]);	
		}
	}
}