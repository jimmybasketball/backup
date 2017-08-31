import java.util.Scanner;
public class A5_21 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		System.out.println("请输入学生的人数：");
		int renshu=in.nextInt();//存储学生的人数
		System.out.println("请输入课程的数目");
		int courseNum=in.nextInt();//存储课程的数目
		String[] name=new String[renshu];//声明一个String数组用来存储学生的姓名
		String[] course=new String[courseNum];//声明一个String数组用来存储课程的名称
		int[][] number=new int[renshu][courseNum];
		
		/*
		 * 用来循环的存储课程的名称
		 * */
		for(int i=0;i<course.length;i++)
		{
			System.out.println("请定义第"+(i+1)+"门课程的名字");
			course[i]=in.next();
		}
		/*
		 * 用来录入学生的各科成绩
		 * */
		for(int i=0;i<renshu;i++)
		{
			System.out.println("请输入学生的姓名：");
			name[i]=in.next();//用来存储学生的姓名
			for(int j=0;j<courseNum;j++){
				System.out.println("请输入学生"+name[i]+course[j]+"的成绩");
				number[i][j]=in.nextInt();//用来存储特定学生的特定成绩
			}
		}
		System.out.print("学生");
		for(int i=0;i<course.length;i++)
		{
			System.out.print("\t"+course[i]);
		}
		System.out.println();
		for(int i=0;i<renshu;i++){
			System.out.print(name[i]);
			for(int j=0;j<courseNum;j++){
				System.out.print("\t"+number[i][j]);
			}
			System.out.println();
		}
	}
}
