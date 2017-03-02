package taf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Iterator;
/**
 * 
 */

/**
 * @author macdonc
 *
 */
public class InteractionLayer {
	
	public static String stepSuccess;
	public String executionDetail = "";
	public static String fieldDesc;
	public static String fieldInputVal;
	public static String fieldType;
	public static int globalWait;
	public static WebDriver driver;
	public static String osSystem = System.getProperty("os.name");
	//public static WebDriver;
	//static WebDriver driver;
//	public static void newDriverCreate(){
//	
	//public static WebDriver driver = new FirefoxDriver();
//	 //* @param args
//	} 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String navSuccess = "true";
		//selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://www.google.com/");
		//WebDriver driver = new FirefoxDriver();
		
	}

	//public void setUp() 
		
	//}
	
	public static String browserInvoke() throws Exception {
		
		if (driver != null){ // does an active driver instance already exist?
			//driver.close();
			driver.quit();
		}
		
		//Thread.sleep(1000);
		System.out.println(osSystem);
		switch (osSystem){
			case "Mac OS X":	
				System.setProperty("webdriver.gecko.driver", "//geckodriver//geckodriver"); // need to document this
				break;
			default:
				System.setProperty("webdriver.gecko.driver", "C:\\GeckoDriver\\geckodriver.exe"); // need to document this
				break;
		}
		
		System.out.println("launching firefox");
		
		driver = new FirefoxDriver();
		//driver.manage().window().setPosition('1');
		driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    //driver.manage().window().setSize(new Dimension(1920,1080));
		
		System.out.println(driver);
		
		//String driverString = 
		
		//ActorLibrary.scenarioInfoContainer.put("driver",driver);
		
		stepSuccess = "pass";
		
		//WebDriver driver =new FirefoxDriver();
		
		//String baseUrl = ActorLibrary.globalInfoContainer.get("BASE_URL");
		//driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		//driver.
		
		// try and catch? What if we can't launch for some reason?
		//driver.get(ActorLibrary.globalInfoContainer.get("target_url"));
		return stepSuccess;
	}
	
	public static void killBrowser(){
		
		if (driver != null){
			driver.quit();
		}
		
		//java.util.Set<String> windowIterator = driver.getWindowHandles();
		//Iterator<String> iter = windowIterator.iterator();
		//System.out.println(windowIterator);
		//System.out.println("number of windows to close = " + windowIterator.size());
		
		
		//driver.Dispose();
		//tearDown();
		//driver.quit(); // quit actually kills the session. need a better method!
	}
	
	public static void maximiseBrowser(){
		//var window = new webdriver.Webdriver.Window(driver);
		if (driver == null){
			return; //"fail";
		}
		
		driver.manage().window().maximize();
	}
	
	
	public static String objectClick(){
		
		// else {
		if (driver == null){
			return "fail";
		}
		
		String seekFlag = isElementPresent(OperationStepProcess.navTarget);
		
		if (seekFlag != "pass"){
			return seekFlag;
		}
		//}
		//findAllElementsOnPage("//*[@class='img' or @class='input']"); // we'll revisit this.
		//findAllElementsOnPage("//input");
		
		//findAllElementsOnPage("//img");
		
		//findAllElementsOnPage("img");
		
		//findAllElementsOnPage("alt=");
		
		// throws?
		WebDriverWait wait = new WebDriverWait(driver,10);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath (OperationStepProcess.navTarget)));
		
		driver.findElement(By.xpath (OperationStepProcess.navTarget)).click();
		
		return seekFlag;
		
	}
	
	public static String sendValueToField(String xpathDesc, String value){
		//int waitTimeGlobal = 30000;
		String inputSuccess = "pass";
		
		fieldDesc = xpathDesc;
		fieldInputVal = value;
		
		// chop up any meta info (radiobuttons, for example).
		cleanAndDetermine();
		
		String seekFlag = isElementPresent(fieldDesc);
		
		if (seekFlag != "pass"){
			return seekFlag;
		}
		
		sendValueToField();
		
		System.out.println("field populated:" + fieldDesc + ", value: " + fieldInputVal);
		
		return inputSuccess;
	}
	
	public static void cleanAndDetermine(){ // assert the type.
		
		// reset the type.
		fieldType = null; // fieldType is the field eventually used to determine behaviour.
		
		if (OperationStepProcess.declaredFieldType != null){ // has a field type already been declared? If so, use that.
			fieldType = OperationStepProcess.declaredFieldType;
		} else { // use the 'old' ;; meta functionality (i.e. field type included in field name string
			String[] fieldMeta = fieldDesc.split(";;", 2);
			// do we have field meta info?
			int metaIndicator = fieldMeta.length;
			
			if (metaIndicator > 1){
				System.out.println ("field meta detected:" + fieldMeta);
				fieldDesc = fieldMeta[0];
				fieldType = fieldMeta[1];
			}
			
		}
			
		
		switch (fieldType){
			case "radiobutton":
				System.out.println("radio button field input triggered.");
				radiobuttonHandling();
				return;
				//break;
			case "select":
				System.out.println("select field input triggered.");
				return;
			default:
				fieldType = "edit";
				return;
				//break;
			}
			
			
			
		 // else, it's an edit field.
		//fieldType = "edit";
		
		
	}
	
	public static String isElementPresent(String desc){ // pass in the xpath description
		// is field present
	
		if (driver == null){
			return "fail";
		}
		
		
	stepSuccess = "pass";
	//WebDriverWait wait = new WebDriverWait(driver, globalWait);
	//WebDriverWait wait = new WebDriverWait(driver, 1);
	
	WebElement element = null;
	int totalWait = 0;
	int globalWaitMilli = (globalWait*1000);
	
	long startStamp = System.currentTimeMillis();
	System.out.println("sync start: "+ startStamp);
	long timeoutMilliPoint = (startStamp+globalWaitMilli);
	
	
	while (startStamp <= timeoutMilliPoint){
	
	//try{
		while (element == null){
		int navOptSize = OperationStepProcess.navTargetOptions.size();
		int navOpt;
		//String elementScanResult;
		
		if (navOptSize > 0){
			//FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver) // we are checking for more than one wait element.
					//.withTimeout(0, TimeUnit.SECONDS) // one second between scans. 
					//.pollingEvery(0, TimeUnit.SECONDS)
					//.ignoring(NoSuchElementException.class);
			
			//WebDriverWait wait = new WebDriverWait(driver, 0);
			
			
			for (navOpt = 0; navOpt < navOptSize; navOpt++){
				desc = OperationStepProcess.navTargetOptions.get(navOpt);
				System.out.println("alternate gui desc " +  navOpt + " of " + navOptSize + " = " + desc);
				
				if (desc.length() < 3){
					stepSuccess = "fail";
					return "fail";
				}
				
				try{
					//element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(desc)));
					driver.manage().timeouts().implicitlyWait(100,TimeUnit.MILLISECONDS);
					List <WebElement> elementList = driver.findElements(By.xpath(desc));
					
					driver.manage().timeouts().implicitlyWait(globalWait,TimeUnit.MILLISECONDS);
					
					if (elementList.size() > 0){
						System.out.println("web element " + desc + "located.");
						OperationStepProcess.navTargetOptions.clear();
						OperationStepProcess.navTarget = desc;
						
						long currentStamp = System.currentTimeMillis();
						long elapsedTime = currentStamp-startStamp;
						double elapsedTimeInSeconds = elapsedTime/1000;
						System.out.println("sync complete, object located in "+ elapsedTimeInSeconds);
						
						return stepSuccess;
					}
					
					
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					stepSuccess = "false";
					e.printStackTrace();
					
					//System.out.println("web element " + desc + "not found within specified time limit.");
					//return navSuccess;
				}	
				
				
				try {
					Thread.sleep(10);
					//totalWait++;
					
					long currentStamp = System.currentTimeMillis();
					
					//if (totalWait == (globalWait*100)){ // no sign of the element we are searching for. quit.
					if (currentStamp > timeoutMilliPoint){	
						stepSuccess = "false";
						OperationStepProcess.navTargetOptions.clear();
						
						long elapsedTime = currentStamp-startStamp;
						double elapsedTimeInSeconds = elapsedTime/1000;
						System.out.println("sync failed: "+ elapsedTimeInSeconds);
						return "fail";
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				}	
			
			//OperationStepProcess.navTargetOptions = ;
		} else {
			
			
			//WebDriverWait wait = new WebDriverWait(driver, globalWait);
			
			//element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(desc)));
			try{
				//driver.manage().timeouts().implicitlyWait(globalWait,TimeUnit.MILLISECONDS);
				
				driver.manage().timeouts().implicitlyWait(globalWaitMilli,TimeUnit.MILLISECONDS);
				
				List <WebElement> elementList = driver.findElements(By.xpath(desc));
				
				if (elementList.size() > 0){
					stepSuccess = "pass";
					OperationStepProcess.navTargetOptions.clear();
					return "pass";
				}
				
				if (elementList.size() == 0){
					System.out.println("web element " + desc + "not found.");
					stepSuccess = "fail";
					//OperationStepProcess.navTargetOptions.clear();
					//OperationStepProcess.navTarget = desc;
					return "fail";
				}
				
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					stepSuccess = "fail";
					e.printStackTrace();
					OperationStepProcess.navTargetOptions.clear();
					return "fail";
					//System.out.println("web element " + desc + "not found within specified time limit.");
					//return navSuccess;
				}	
			
			
		}
		//driver.findElement(By.xpath (desc));
		System.out.println (element);
		};
		
		
		

		//catch}
		System.out.println("web element " + desc + "located.");
		return "pass";
		
	}
	return "pass";
	
	} // end of function
	
	public static void radiobuttonHandling(){
		
		String explicitOpt = specificItemCheck();
		
		String option = "";
		String[] fieldOptionSplit = fieldDesc.split(":", 2); // for re-assembly
		String selector = fieldOptionSplit[0];
		
		switch (fieldInputVal.toUpperCase()){
		case "0":
			option = ":0']";
			break;
		case "1":
			option = ":1']";
			break;
		case "TRUE":
			option = ":0']";
			break;
		case "FALSE":
			option = ":1']";
			break;
		case "Y":
			option = ":0']";
			break;
		case "N":
			option = ":1']";
			break;
		default:
			option = ":" + explicitOpt + "']";
			break;
		}
		
		
		fieldDesc = selector + option; // re-assign value.
		
		System.out.println("radiobutton processing. Input value = " + fieldInputVal + " = " + fieldDesc);
		
		
	}
	
	public static void sendValueToField (){
		
		
		try{
			switch(fieldType){
				case "radiobutton":
					driver.findElement(By.xpath (fieldDesc)).click();
					break;
				case "edit":
					driver.findElement(By.xpath(fieldDesc)).clear();	 
					driver.findElement(By.xpath (fieldDesc)).sendKeys(fieldInputVal);
					break;
				case "select":
					//driver.findElement(By.id(fieldDesc)).click();
					
					System.out.println("windows list select");
					driver.findElement(By.xpath(fieldDesc)).click();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					 switch (osSystem){
						case "Mac OS X":	
							 // need to document this
							//Thread.sleep(1000);
							new Select(driver.findElement(By.xpath(fieldDesc))).selectByVisibleText(fieldInputVal);
							break;
						default:
							System.out.println("windows list select");
							driver.findElement(By.id(fieldDesc)).click();
							driver.findElement(By.id(fieldDesc)).sendKeys(fieldInputVal);
							break;	
					 }
						
					break;
				default:
					driver.findElement(By.xpath (fieldDesc)).sendKeys(fieldInputVal);
					break;
			
			
			}
			
		} catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void findAllElementsOnPage(String elementClass){
		
		// //input
		// //img
		
		System.out.println("fail point check");
		
		List<WebElement> elements = driver.findElements(By.xpath(elementClass));	
		
		//List<WebElement> elements = driver.findElements(By.xpath("//*[@class='img' or @class='input']"));
		
		//List<WebElement> elements = driver.findElements(By.xpath("//img*"));
		
		//List<WebElement> elements2 = driver.findElements(By.xpath("//*[@class='input']"));
		
		
		//System.out.println("number of elements detected for" + elementClass + " = " + elements.size());
		
		for(WebElement ele : elements){
			String elementText = ele.getText();
			int hashCheck = elementText.hashCode();
			if (hashCheck != 0){
			
				System.out.println(ele.getText());
				//System.out.println(ele.getClass());
				System.out.println("Class Name:" + (ele.getAttribute("className")));
				System.out.println("ID: " + (ele.getAttribute("id")));
				System.out.println("Title: " + (ele.getAttribute("title")));
				System.out.println("Name: " + (ele.getAttribute("name")));
				System.out.println("Class: " + (ele.getAttribute("class")));
				//System.out.println(ele.getAttribute("title")));
				//System.out.println(ele.getAlt());
			//System.out.println(ele.get());
			//System.out.println(ele.getClassName());
			}
		}
		
		System.out.println("scan complete");
		
	}

	public static String specificItemCheck(){
		
		if (fieldInputVal.matches("(.*)specificitem(.*)") == true){
			String[] opt = fieldInputVal.split(" ");
			return opt[1];
		}
		
		return "";
	}

	public static String obtainHTMLFromAUT(String xpath){
		String actualTextOutput = null;
		
		
		try{
		//driver.highlightElement(By.xpath(xpath));
		//WebElement elem = driver.findElement(By.xpath(xpath));
		//elem.highlight();
		//actualTextOutput = elem.getAttribute("innerHTML");
		actualTextOutput = driver.getPageSource();
			
			
		}  catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return actualTextOutput;
	}
	
	public static String obtainTextFromElement(String xpath){
		String actualTextOutput = null;
		String findObj = null;
		
		try{
		//driver.highlightElement(By.xpath(xpath));
		//WebElement elem = driver.findElement(By.xpath(xpath));
		//elem.highlight();
		//actualTextOutput = elem.getAttribute("innerHTML");
			
		findObj = isElementPresent(xpath);
		
		if (findObj != "pass"){
			actualTextOutput = "element not found!";
			System.out.println(actualTextOutput);
			return actualTextOutput;
		}
		
		//if ()
			
		actualTextOutput = driver.findElement(By.xpath(xpath)).getText();
		//unicode checking
		actualTextOutput = findUnicodeChars(actualTextOutput);
		
		}  catch (NoSuchElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			actualTextOutput = "element not found!";
			return actualTextOutput;
		}
		
		return actualTextOutput;
	}
	
	public static Boolean findTextInString(String xpath, String arrRef){
		
		Boolean isFound = false;
		//if (xpath == "null"){
			//xpath = "//div[@id='main']"; // if we haven't specified an element, pull out everything from 'main'.
		//}
		
		String seekText = ActorLibrary.scenarioInfoContainer.get(arrRef);
		
		if (seekText == null){
			Verification.vpDetail = "nothing in scenario info hashmap for element " + arrRef + ", value has not been loaded.";
			System.out.println(Verification.vpDetail);
			
			return isFound;
			
		}
		
		
		String entireText = InteractionLayer.obtainTextFromElement(xpath);
		
		try {
			isFound = entireText.toLowerCase().contains(seekText.toLowerCase());
		} catch  (org.openqa.selenium.NoSuchElementException e){
			System.out.println(e);
		}
		
		return isFound;
		
		
	}
	
	
	public static void dumbWait(int milliseconds){
		
		System.out.println("dumb wait called for " + milliseconds + " milliseconds. Starting.....");
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("dumb wait finished.");
	
	}
	
	public static void screenCapture(){
		
		String outputPath;
		
		if (driver == null){
			return; // a preparatory step has failed; nothing to capture
		}
		
		//public interface TakesScreenshot;
		
		//String timeStamp = Reporting.getTimeStampForFailCapture();
		
		try{
		WebDriver augmentedDriver = new Augmenter().augment(driver);
		File source = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
		//outputPath = "./target/screenshots/fails/" + ActorLibrary.runName + "/" + ActorLibrary.scenarioIndex + "_" + ActorLibrary.operationalDataset +".png";
		outputPath = ActorLibrary.testDir + "/" + ActorLibrary.scenarioIndex + "_" + ActorLibrary.operationalDataset +".png";
		FileUtils.copyFile(source, new File(outputPath));
		
		ActorLibrary.failCaptureScreenshot = outputPath;
		
		ActorLibrary.failurePoint = ActorLibrary.failurePoint + " - image: " + ActorLibrary.failCaptureScreenshot;
		
		outputPath = null;
		
		} catch (IOException e){
			outputPath = "failed to capture screenshot: " + e.getMessage();
			
		}
		
		//File
		
	}
	
	public static String findUnicodeChars(String originalText){
		
		//int stringLen = originalText.length();
		//int pos;
		
		if (originalText == null){
			return originalText;
		}
		
		originalText = originalText.replace("\u2082", "subscript2");
		//System.out.println(originalText);
		
		originalText = originalText.replace("�", "subscript2");
		System.out.println(originalText);
		
		//String[] 
		
		//for (pos=0;pos<stringLen;pos++){
			
			
			
		//}
		
		return originalText;
		
	}

	public static void makeDir(String dir){
		
		//parameterize this
		File theDir = new File(dir);
		
		if (!theDir.exists()){
			System.out.println("creating directory: " + ActorLibrary.runName);
			boolean result = false;
			
			try{
				theDir.mkdir();
				result = true;
			} catch (SecurityException se){
				
			}
		
			if (result){
				System.out.println("DIR created");
			}
		
		
		}
		
	}

	public static String browserURLSelect(String navTarget) {
		// TODO Auto-generated method stub
		switch (navTarget){
			case "BASE_URL": // substitute value
			navTarget = Startup.BASE_URL;
			break;
		default: // url IS navTarget
			break;
		}
		
		driver.get(navTarget);
		driver.manage().window().maximize();
		//driver.manage().window().fullscreen();
		//driver.manage().window().setSize("1000","600");
		
		stepSuccess = "pass";
		return stepSuccess;
		}
	
}
