class Student{
	private String name;
	private int age;
	private char gender;
	public void setGender(char gender){
		if(gender=='��'||gender=='Ů'){
			this.gender=gender;
		}else{
			System.out.println("��������ȷ���Ա�");
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