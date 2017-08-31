
	package sfhtpc;
		import org.openqa.selenium.By;
		import org.openqa.selenium.WebDriver;
		import org.openqa.selenium.firefox.FirefoxDriver;

		public class baidu {
		    public static void main(String[] args) {
		    	System.setProperty("webdriver.firefox.marionette","E:\\jimmybackup\\Software\\selenium\\learning simple\\geckodriver-v0.9.0-win64\\geckodriver.exe");
		        WebDriver driver = new FirefoxDriver();
		        System.out.println("open the firefox");
		        driver.get("https://www.baidu.com/");
		        System.out.println("open the baidu");
		        driver.findElement(By.id("kw")).sendKeys("selenium java");
		        driver.findElement(By.id("su")).click();
		        driver.findElement(By.xpath(".//*[@id='u']/a[1]")).click();
		        System.out.println("OK");
		        driver.quit();
		    }

		} 


