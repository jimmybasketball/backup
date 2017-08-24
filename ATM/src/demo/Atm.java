package demo;

import java.util.Scanner;

public class Atm {
	public static void main(String[] agrs){
		 Atm ss=new Atm();
		 ss.Atm1();
		 System.exit(0);
	}
	public void Atm1(){

		System.out.println("»¶Ó­Ê¹ÓÃATM"+"\n"+"1:µÇÂ¼ 2:×¢²á 3:ÍË³ö");
		Scanner in =new Scanner(System.in);
		int shuru= in.nextInt();
		switch(shuru){
		case 1:
			System.out.println("µÇÂ¼");
			new Login().login();
			break;
		case 2:
			System.out.println("×¢²á");
			new UserRegistry().regi1stry();
			new Login().login();
			break;
		case 3:
			System.out.println("ÍË³ö");
			
			break;
			
		}
		System.exit(0);
	}
}
