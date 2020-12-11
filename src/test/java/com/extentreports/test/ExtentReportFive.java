package com.extentreports.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;

public class ExtentReportFive {
	
	//In this class, we read the configs from xml file and generate an extent report
	//Customization is also done for ViewName in the line 23
	
	@Test
	public void extentTest() throws IOException {
		ExtentReports extent = new ExtentReports();
		ExtentSparkReporter spark = new ExtentSparkReporter("index.html").viewConfigurer().viewOrder().as(new ViewName[] {ViewName.DASHBOARD,ViewName.TEST,ViewName.CATEGORY,ViewName.AUTHOR}).apply();//path where HTML file is generated
		//Below line generates a report with only failed Testcases
		ExtentSparkReporter failedspark = new ExtentSparkReporter("failed-tests-index.html").filter().statusFilter().as(new Status[] {Status.FAIL}).apply();
		extent.attachReporter(spark,failedspark);
		
		final File CONF = new File("extent-config.xml");
		spark.loadXMLConfig(CONF);

		
		/*spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("AutomationReport"); //Title of the report
		spark.config().setReportName("ExtentReportDemo");*/
		
		ExtentTest test = extent.createTest("Login Tests").assignAuthor("manasa").assignCategory("SmokeTest").assignCategory("Regression").assignDevice("Windows Chrome 84"); // To create a testnode in the report
		test.info("Login Test Started Successfully"); //these just simple assertions - Used to create a test step node
		test.info("URL loaded");
		test.pass("Login Test Completed Successfully");
		test.pass(MarkupHelper.createLabel("Login Test Completed Successfully", ExtentColor.GREEN));  //Used to highlight the color to the lines
		
		ExtentTest test1 = extent.createTest("HomePage Tests").assignAuthor("vishru").assignCategory("Regression").assignDevice("Linux Firefox 80"); // To create a testnode in the report
		test1.info("HomePage Test Started Successfully"); //these just simple assertions - Used to create a test step node
		test1.info("URL loaded");
		test1.fail("HomePage Test Failed miserably");
		test1.fail(MarkupHelper.createLabel("HomePage Test Failed miserably", ExtentColor.RED));
		
		extent.flush(); //Unless u call this method, your report will not be written with logs - we use it in @aftermethod or @aftertest
		Desktop.getDesktop().browse(new File("index.html").toURI()); // This line is used to open the html file automatically
		Desktop.getDesktop().browse(new File("failed-tests-index.html").toURI());
	}

}
