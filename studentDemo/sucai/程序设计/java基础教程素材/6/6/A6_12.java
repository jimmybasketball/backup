class Student{
	private String name;
	private int age;
	private char gender;
	public void setGender(char gender){
		if(gender=='男'||gender=='女'){
			this.gender=gender;
		}else{
			System.out.println("请输入正确的性别");
		}
	}
	public char getGender(){
		return this.gender;
	}
}
public class A6_12{
	public static void main(String[] args){
		Student one=new Student();
		one.setGender('f');
		System.out.println(one.getGender());
	}
}