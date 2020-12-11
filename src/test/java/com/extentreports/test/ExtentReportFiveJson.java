package com.extentreports.test;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.CodeLanguage;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportFiveJson {
	
	//In this class, we read the configs from json file and generate an extent report
	
	@Test
	public void extentTest1() throws IOException {
		ExtentReports extent1 = new ExtentReports();
		ExtentSparkReporter spark1 = new ExtentSparkReporter("index-json.html"); //path where HTML file is generated
		extent1.attachReporter(spark1);
		
		final File CONF = new File("extent-config.json");
		spark1.loadJSONConfig(CONF);

		
		/*spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("AutomationReport"); //Title of the report
		spark.config().setReportName("ExtentReportDemo");*/
		
		ExtentTest test2 = extent1.createTest("Login Tests").assignAuthor("manasa").assignCategory("SmokeTest").assignCategory("Regression").assignDevice("Windows Chrome 84"); // To create a testnode in the report
		test2.info("Login Test Started Successfully"); //these just simple assertions - Used to create a test step node
		test2.info("URL loaded");
		test2.pass("Login Test Completed Successfully");
		
		//Add Json Data to your report
		String jsondata = "{name: \"John\", age: 31, city: \"New York}";
		test2.info(jsondata);
		test2.info(MarkupHelper.createCodeBlock(jsondata, CodeLanguage.JSON));
		
		ExtentTest test3 = extent1.createTest("HomePage Tests").assignAuthor("vishru").assignCategory("Regression").assignDevice("Linux Firefox 80"); // To create a testnode in the report
		test3.info("HomePage Test Started Successfully"); //these just simple assertions - Used to create a test step node
		test3.info("URL loaded");
		test3.fail("HomePage Test Failed miserably");
		
		extent1.flush(); //Unless u call this method, your report will not be written with logs - we use it in @aftermethod or @aftertest
		
		Desktop.getDesktop().browse(new File("index-json.html").toURI()); //this will help you to open the report automatically after the execcution
	}

}