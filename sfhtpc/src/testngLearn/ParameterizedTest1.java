package testngLearn;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ParameterizedTest1 {
	
    @Test()
    @Parameters({"myName","boyName"})
      public void parameterTest(String myName,String boyName) {
        System.out.println("Parameterized value is : " + myName+"+"+boyName);
        
    }
   
}