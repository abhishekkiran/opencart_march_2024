package testBase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.apache.logging.log4j.LogManager; //Log4j
import org.apache.logging.log4j.Logger; //Log4j

public class BaseClass {
	public static WebDriver driver;
	public Logger logger;
	public ResourceBundle rb; //Properties file
	@BeforeClass
	@Parameters("browser")
	public void setup(String br)
	{
		rb=ResourceBundle.getBundle("config"); //reading data from properties file
		logger=LogManager.getLogger(this.getClass());//log4j
		if(br.equalsIgnoreCase("chrome"))
		{
		logger.info("Launching chrome browser");
		
		ChromeOptions options=new ChromeOptions();
		options.setExperimentalOption("excludeSwitches",new String[] {"enable-automation"});//for removing msg " Your chrome is controlled by automated software"
		driver=new ChromeDriver(options);
		}
		else if(br.equalsIgnoreCase("edge"))
		{
			logger.info("Launching Edge browser...");
			EdgeOptions options=new EdgeOptions();
			options.setExperimentalOption("excludeSwitches",new String[] {"enable-automation"});
			driver=new EdgeDriver();
		}
		else if(br.equalsIgnoreCase("firefox"))
		{
			logger.info("Launching Firefox browser...");
			driver=new FirefoxDriver();
		}
		else
		{
			logger.info("Launching Safari browser");
			driver=new SafariDriver();
		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(rb.getString("appURL")); //local app URL
		//driver.get("http://demo.opencart.com/index.php"); //remote app URL
		logger.info("Launching browser...");
		driver.manage().window().maximize();
	}
	
	@AfterClass
	public void tearDown()
	{
		driver.quit(); //SocketException is coming because of close command. So changing it to quit..
	}
	public String randomString()
	{
		String generatedString=RandomStringUtils.randomAlphabetic(5);
		return generatedString;
	}
	public String randomNumber()
	{
		String generatedNumber=RandomStringUtils.randomNumeric(5);
		return generatedNumber;
	}
	public String randomAlphaNumeric()
	{
		String generatedString=RandomStringUtils.randomAlphabetic(3);
		String generatedNumber=RandomStringUtils.randomNumeric(5);
		return(generatedString+generatedNumber);
	}
	// "NoSuchMethodError" is the error after execution... Hence, adding testNG libraries to the project(in addition to what we added in dependencies)..

	public String captureScreen(String tname) throws IOException {
		
		/*Date dt=new Date();
		 SimpleDateFormat sp=new SimpleDateFormat("yyyyMMddhhmmss");
		 String 	timestamp=sp.format(dt);
		 */
		String timeStamp=new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		TakesScreenshot takesScreenshot=(TakesScreenshot) driver;
		File source=takesScreenshot.getScreenshotAs(OutputType.FILE);
		String destination=System.getProperty("user.dir")+"\\screenshots\\" +  tname + "_" + timeStamp +".png";
	    try {
	    	FileUtils.copyFile(source, new File(destination));
	    } catch(Exception e)  {
	    	e.getMessage();
	    }
	    return destination;
	
	}
}
