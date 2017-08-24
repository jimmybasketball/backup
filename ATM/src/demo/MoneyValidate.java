package demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyValidate {
	public String moneyValidate(int money){
		String s = Integer.toString(money);
		 String mre = "^\\+?[1-9][0-9]*$";//Java里需要用双斜杠代替正则表达式的反斜杠
	        Pattern p = Pattern.compile(mre);
	        Matcher m = p.matcher(s);
	        if (m.find()) {
	        	return "验证成功!";
	        } else {
	        	return "验证失败!";
	        }
	}
}
