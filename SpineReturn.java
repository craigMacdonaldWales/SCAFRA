package taf;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;

public class SpineReturn {
	
	private WebDriver driver;
	private HashMap <String,String> scenarioInfoContainer;
	
	public SpineReturn (){
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public HashMap<String, String> getScenarioInfoContainer() {
		return scenarioInfoContainer;
	}

	public void setScenarioInfoContainer(HashMap<String, String> scenarioInfoContainer) {
		this.scenarioInfoContainer = scenarioInfoContainer;
	}

	public SpineReturn(WebDriver driver, HashMap<String, String> scenarioInfoContainer) {
		super();
		this.driver = driver;
		this.scenarioInfoContainer = scenarioInfoContainer;
	}
	
	
	
	
	
	
}
