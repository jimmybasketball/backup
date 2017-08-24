package demo;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Test {
	@org.testng.annotations.Test
	public void test1() throws SQLException{
//SearchBalance aa=new SearchBalance();
//aa.Search("111");
//		Transfer bb=new Transfer();
//		BigDecimal b1 = new BigDecimal(300);
//		bb.transferMoney("111", "222", 30000);

//		new Withdraw().Withdraw1("111", 1000);
//		new Deposito().Deposito1("111", 1000);
		new MoneyValidate().moneyValidate(-99);
		
	}
}
