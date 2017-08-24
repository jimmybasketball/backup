package demo;

import java.util.Scanner;
import org.testng.annotations.Test;

public class Login {
	
	@Test
	public void login(){
		Scanner in =new Scanner(System.in);
		Scanner in1 =new Scanner(System.in);
		int functionLogin=0;
		System.out.println("======欢迎来到登录模块======");
		System.out.println("请输入用户名：");
		String nameLogin=in.nextLine();
		//校验用户名是否存在
		String backName= new NameValidate().NameValidate1(nameLogin);
		try {

	        	if(backName.equals("用户存在"))//检查数组中是否包含nameLogin
	    	    {

	        		System.out.println("请输入密码：");
	        		String passLogin=in.nextLine();
	        		int i=0;
	        		int a=new PasswordValidate().passwordValidate(nameLogin, passLogin);
	        		
	        		while(a!=1){
	        			i++;
	        			System.out.println("密码或账号错误，请重新输入密码：");
	        			String passLogin1=in.nextLine();
	        			a=new PasswordValidate().passwordValidate(nameLogin, passLogin1);
	        				if(i>=3){
	        					System.out.println("错误次数超限，退出系统");
	        					System.exit(0);
	        						}
	        					}
	        				if(a==1){
	        					while(functionLogin!=5&&functionLogin!=6){
	        					System.out.println("========================="+"\n"+"请输入选项，1查询余额，2，转账，"
		        					+ "3，取款，4，存款，5，返回主菜单，6，退出");
	        	        			 functionLogin=in.nextInt();
	        	        			
	        	        			switch(functionLogin){
	        	        			case 1:System.out.println("====显示余额====");
	        	        			SearchBalance aa=new SearchBalance();
	        	        			aa.Search(nameLogin);
	        	        				   break;
	        	        			case 2:System.out.println("=====转账====");
	        	        			System.out.println("请输入转账姓名:");
	        	        			String TransferName =in1.nextLine();	        	   	
	        	        			new NameValidate().NameValidate1(TransferName);
	        	        			
	        	        			System.out.println("请输入转账金额：");
	        	        			int TransferMoney=in.nextInt();
	        	        			String ss=new MoneyValidate().moneyValidate(TransferMoney);
	        	        				while(ss.equals("验证失败!")){
	        	        					System.out.println("请重新输入转账金额：");
	        	        					TransferMoney=in.nextInt();
	        	        					ss=new MoneyValidate().moneyValidate(TransferMoney);
	        	        				}
	        	        			if(ss.equals("验证成功!")){
	        	        				Transfer bb=new Transfer();
		        	        			bb.transferMoney(nameLogin, TransferName, TransferMoney);
	        	        			}
	        	        			
	             				           break;
	        	        			case 3:System.out.println("====取款====");
	        	        			System.out.println("请输入取款金额：");
	        	        			int WithdrawMoney=in.nextInt();
	        	        			new Withdraw().Withdraw1(nameLogin, WithdrawMoney);
	        				           	   break;
	        	        			case 4:System.out.println("====存款====");
	        	        			System.out.println("请输入存款金额：");
	        	        			int DepositoMoney=in.nextInt();
	        	        			new Deposito().Deposito1(nameLogin, DepositoMoney);
	        				               break;
	        	        			case 5:System.out.println("返回主菜单");
	        	        			new Atm().Atm1();
	        				               break;
	        	        			case 6:System.out.println("退出");
	        				               break;}
	        					}
	        		}
	    	    }

	    	    	
	        	else//用户不存在
	    	    {
	    			System.out.println("用户名不存在，请先去注册：");
	    				
	    	    }
	        
	        
	    DBUtil.closeConn(null, null, DBUtil.getConn());  //close the connect  
	    	    
	    } catch(Exception e) {   
	        System.out.println("直接跳出异常");
	    }

	}
	public void functionMenu(){
		
	}
}
