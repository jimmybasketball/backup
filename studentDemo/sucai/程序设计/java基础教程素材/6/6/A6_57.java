/*
final(最终)是一个修饰符
1.final可以修饰类,函数,变量(成员变量,局部变量)
2.被final修饰后的类不可以被其他类继承
3.被final修饰后的函数不可以被重写
4.被final修饰后的变量不允许被再次赋值,final在对变量进行修饰时一定赋值，
被final修饰后的变量我们称它常量

注意：常量的命名规范:字母全部要大写,
如果这个名字是由多个单词组成，在单词之间用_隔开
*/
class Student{
	final static public String STUDENT_NAME="小李";//全局常量
}
public class A6_57{
	public static void main(String[] args){
		Student s=new Student();
		Student s1=new Student();
	}
}