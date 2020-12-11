package com.extentreports.test;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AttachScreenshotToExtentReport {
	
	/* 1. Take a Screenshot as png, jpg and attach to the report - drawback - if u send to client they might not see the file
	 * 2. Take a Screenshot as png, jpg file, convert it in form of Base64 and attach to the report - unnecessary memory for saving screenshots
	 * 3. Take a Screenshot as Base64 and attach to the report- this is the right way to do */
	ExtentReports extent;
	WebDriver driver;
	
	@BeforeSuite
	public void setup() throws IOException {
	extent = new ExtentReports();
	ExtentSparkReporter spark = new ExtentSparkReporter("index-screenshot.html"); //path where HTML file is generated
	extent.attachReporter(spark);		
	final File CONF = new File("extent-config.xml");
	spark.loadXMLConfig(CONF);
	}
	
	@AfterSuite
	public void teardown() throws IOException {		
		extent.flush();
		driver.quit();
		Desktop.getDesktop().browse(new File("index-screenshot.html").toURI()); 		
	}
	
	
	@Test
	public void AttachScreenShot() throws IOException {		
		ExtentTest test = extent.createTest("Screenshots Test");
		WebDriverManager.chromedriver().setup();	
		driver = new ChromeDriver();
		test.pass("TestStarted");
		
		driver.get("https://google.com");
		driver.findElement(By.name("q")).sendKeys("Automation",Keys.ENTER);
		test.pass("Values Entered", MediaEntityBuilder.createScreenCaptureFromPath(getScreenshotPath()).build());
		
		test.pass("Test Completed");
		
	}
	
	@Test
	public void AttachBase64() throws IOException {		
		ExtentTest test = extent.createTest("Screenshots Test - Direct Base 64");
		WebDriverManager.chromedriver().setup();	
		driver = new ChromeDriver();
		test.pass("TestStarted");
		
		driver.get("https://google.com");
		driver.findElement(By.name("q")).sendKeys("Automation",Keys.ENTER);
		test.pass("Values Entered", MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64()).build());
		
		test.pass("Test Completed");
		
	}
	
	@Test
	public void AttachScreenShotBase64() throws IOException {		
		ExtentTest test = extent.createTest("Screenshots Test - Base 64");
		WebDriverManager.chromedriver().setup();	
		driver = new ChromeDriver();
		test.pass("TestStarted");
		
		driver.get("https://google.com");
		driver.findElement(By.name("q")).sendKeys("Automation",Keys.ENTER);
		test.pass("Values Entered", MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshotPathAsBase64()).build());
		
		test.pass("Test Completed");
		
	}
	
	public String getScreenshotPath() throws IOException {
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir")+"/Screenshots/image.png";
		FileUtils.copyFile(src, new File(path));
		return path;
	}
	
	public String getScreenshotPathAsBase64() throws IOException {
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir")+"/Screenshots/image.png";
		FileUtils.copyFile(src, new File(path));
		byte[] imagebytes = IOUtils.toByteArray(new FileInputStream(path));
		return Base64.getEncoder().encodeToString(imagebytes);
	}
	
	public String getBase64() {
		return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
	}
}
