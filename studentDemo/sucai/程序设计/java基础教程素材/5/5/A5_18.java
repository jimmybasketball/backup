import java.util.*;
public class A5_18 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		System.out.println("请输入学生的人数：");
		int renshu=in.nextInt();
		System.out.println("请输入课程的数目");
		int courseNum=in.nextInt();
		String[] name=new String[renshu];
		String[] course=new String[courseNum];
		for(int i=0;i<course.length;i++)
		{
			System.out.println("请定义第"+(i+1)+"门课程的名字");
			course[i]=in.next();
		}
		
	}
}
