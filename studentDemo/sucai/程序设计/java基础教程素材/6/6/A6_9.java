class Dog{
	String name;
	public String print(String n,int a,char b){
		System.out.println("n="+n);
		System.out.println("a="+a);
		System.out.println("b"+b);
		return n;
	}
	public void str(int a){
		if(a==0){
			System.out.println("���");
		}else if(a==1){
			return;
		}

		System.out.println("�Һܺ�");
	}
}
public class A6_9{
	public static void main(String[] args){
		Dog A=new Dog();
		A.print("С��",12,'��');
	}
}