import java.util.Scanner;
public class A5_11 {
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		System.out.println("请输入你要找的学生的姓名：");
		String name=in.next();
		String[] arr={"张三","李四","王二","麻子","壁虎"};
		boolean flag=false;
		for(int i=0;i<arr.length;i++){
			if(name.equals(arr[i])){
				flag=true;
				break;
			}
		}
		if(flag==true){
			System.out.println("找到了");
		}else{
			System.out.println("没有这个学员存在");
		}
	}
}
