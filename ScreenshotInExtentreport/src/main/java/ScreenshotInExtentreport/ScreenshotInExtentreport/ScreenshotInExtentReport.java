package ScreenshotInExtentreport.ScreenshotInExtentreport;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class ScreenshotInExtentReport {
	public WebDriver driver;
	public ExtentReports extent;
	public ExtentTest extentTest;
		@BeforeTest
		public void setExtent()
		{
			extent= new ExtentReports(System.getProperty("user.dir")+"/test-output/ExtentReport.html",true);
			extent.addSystemInfo("Host Name","Windows");
			extent.addSystemInfo("User Name","TestNg");
			extent.addSystemInfo("Enviroment","QA");
			
			
		}
		@AfterTest
		public void endReport()
		{
			extent.flush();
			extent.close();
		}
		public static String getScreenshot(WebDriver driver,String screenshotName) throws IOException
		{
			
			String dateName=new SimpleDateFormat("yyyyMMddhhss").format(new Date());
			File scrnshot=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String destination=System.getProperty("user.dir")+"/failedtestscreenshots/"+screenshotName+dateName+".png";
			File finalDestination= new File(destination);
			FileUtils.copyFile(scrnshot,finalDestination);
			return destination;
		}
		
		
		
		
		@BeforeMethod
	public void chromeRun()
	{
		System.setProperty("webdriver.chrome.driver", "E:\\lib\\Chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get("https://www.google.com/");
		
	}
		
		@Test(priority=1)
		public void googleTitleTest()
		{
			extentTest= extent.startTest("googletitleTest");
			String title=driver.getTitle();
			System.out.println(title);
	        Assert.assertEquals(title, "Google123","title not matched");
			
		}
		@Test(priority=2)
		public void gmailButtonTest()
		{
			extentTest=extent.startTest("gmailButtonTest");
					driver.findElement(By.xpath("//a[contains(@class,'gb_d')]")).click();
					
		}
		@Test(priority=3)
		public void facebookUrl() throws InterruptedException
		{Thread.sleep(2000);
			extentTest=extent.startTest("Facebook URL test");
			driver.findElement(By.xpath("//input[contains(@class,'gLFyf gsfi')]")).sendKeys("facebook");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//input[@class='gNO89b']")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//h3[contains(text(),'Facebook - Log In or Sign Up')]")).click();
			String url=driver.getCurrentUrl();
			System.out.println(url);
			
			
		}
		@Test(priority=4)
		public void enterUsername()
		{
			extentTest=extent.startTest("Enter Email id test");
			driver.navigate().to("https://www.facebook.com/");
			driver.findElement(By.name("email")).sendKeys("jashashidhar@gmail.com");
			throw new SkipException("skipped");
		}
		@AfterMethod
		public void tearDown(ITestResult result) throws IOException
		{
			if(result.getStatus()==ITestResult.FAILURE) {
				extentTest.log(LogStatus.FAIL,"Testcase failed is "+ result.getName());
				extentTest.log(LogStatus.FAIL, "Testcase failed is "+ result.getThrowable());
				
				String screenshotPath=ScreenshotInExtentReport.getScreenshot(driver,result.getName());
				extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath));
				
			}
			else if(result.getStatus()==ITestResult.SKIP)
			{
				extentTest.log(LogStatus.SKIP, "Testcase skipped is "+ result.getName());
				
			}
			else if(result.getStatus()==ITestResult.SUCCESS)
			{
				extentTest.log(LogStatus.PASS, "Testcase passed is "+ result.getName());
			}
			extent.endTest(extentTest);
			driver.quit();
		}
	}


