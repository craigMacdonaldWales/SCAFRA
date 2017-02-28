package taf;
import java.util.HashMap;

public class Verification {

	public static String expectedResultsIndex;
	public static String screen;
	public static String dimensionString;
	//public String contextWindow;
	public static String objectDesc;
	public static String vpType;	
	public static String vpCheckpointName;
	public static String liveOutputFile;
	public static String baselineFile;
	public static String vpDesignator;
	public static String SQLDataset;
	public static String vpBitmapThreshold;
	//USER_EXPLANATION	
	public static String dropFlag;	
	public static String application;	
	public static String vpDetail;
	public static String globalVPResult = "pass";
	public static String singleVPResult = null;
	public static String expectedResult = null;
	public static String actualResult = null;
	public static String textExclusionList = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void verificatonPoint(){
		objectDesc = null;
		// query verification point table for VP info
		
		String queryString = "SELECT * from VERIFICATION_POINT_INDEX_TABLE where OPERATIONAL_DATASET = '" + ActorLibrary.operationalDataset + "'";
		
		String table = "VERIFICATION_POINT_INDEX_TABLE";
		//String resultArray[];	
		HashMap<Integer,String> VPMatrixOut = DataHandler.executeQuery(table,queryString,1,"core");
		
		String opControlHashOutput = VPMatrixOut.get(1);
		HashMap<String,String> VPMatrix = DataHandler.convertRecordsetRowToArrayList(opControlHashOutput);
		
		
		expectedResultsIndex = VPMatrix.get("EXPECTED_RESULTS_INDEX");
		screen = VPMatrix.get("SCREEN");
		dimensionString = VPMatrix.get("DIMENSION_STRING");
		//navigationInstructionSet = opControl.get("CONTEXT_WINDOW");
		objectDesc = VPMatrix.get("OBJECT_DESC"); // the target for extraction / verification
		
		
		
		vpType = VPMatrix.get("VP_TYPE");
		
		if (vpType == null){
			return;
		}
		vpCheckpointName = VPMatrix.get("VP_CHECKPOINT_NAME");
		liveOutputFile = VPMatrix.get("LIVE_OUTPUT_FILE");
		baselineFile = VPMatrix.get("BASELINE_FILE");
		vpDesignator = VPMatrix.get("VP_DESIGNATOR");
		SQLDataset = VPMatrix.get("SQL_DATASET");
		vpBitmapThreshold = VPMatrix.get("VP_BITMAP_THRESHOLD");
		dropFlag = VPMatrix.get("DROP_FLAG");
		application = VPMatrix.get("APPLICATION");
		textExclusionList = VPMatrix.get("DYNAMIC_EXCLUSIONS");
		
		String verAppKey = ActorLibrary.globalInfoContainer.get("VERSION_APPEND_INDEX");
		String GUIqueryString = "select SUBSTITUTE_VALUE from VERSION_VALUE_SUBSTITUTE_TABLE where ORIGINAL_VALUE = '" + objectDesc + "' and VERSION_key = '" + verAppKey +"'";
		HashMap<Integer,String> GUIreturn = DataHandler.executeQuery("",GUIqueryString,0,"core");
		
		String GUIalias = GUIreturn.get(1);
		
		System.out.println("gui string returned = " + GUIalias);
		
		if (GUIalias != null){
			objectDesc = GUIalias; // only assign the alias value if one exists.
		}
		
		
		switch (vpType){
		case "bitmap":
			//
			break;
		case "vp_sql":
			//
			break;
		case "table_output":
			//
			break;
		case "text":
			//
			System.out.println(objectDesc.hashCode());
			
			if (objectDesc.hashCode() == 3392903){
				objectDesc = "//div[@id='main']"; // if we haven't specified an element, pull out everything from 'main'.
			}
			
			VPTextCheck();
			
			
			break;
		default:
			//
			break;
		
		}
	
		// write results
		Reporting.VPExecutedInsert();
		VPMatrix.clear(); // empty the hashmap.
		
		
	}
	
	public static void VPTextCheck(){ // compare AUT displayed text (from any source) against established baseline
	
		expectedResult = null;
		actualResult = null;
		
		String[] expectedResultsGenericCheck = expectedResultsIndex.split(":");
		
		String resultSwitch = expectedResultsGenericCheck[0];
		
		switch (resultSwitch){ // define expected text
		case "generic": // positional stamped results index.
			// create the expected results index here.
			String expectedResultStamp = ActorLibrary.operationalDataset  + "/SEL" + ActorLibrary.scenarioIndex + "/" + ActorLibrary.rawStepSequence;
			System.out.println("generic result index generated (positional) = " + expectedResultStamp);
			
			//actualResult = InteractionLayer.obtainHTMLFromAUT(objectDesc); // get ALL HTML from current page.
			
			actualResult = InteractionLayer.obtainTextFromElement(objectDesc);
			
			if (textExclusionList.hashCode() != 3392903){ // is not null!
				actualResult = filterDynamicTextElements(actualResult);
			}
			
			//actualResult = HTMLTidy(actualResult);
			
			if (Startup.RUN_MODE == 1){ // 1 = update.
				
				globalVPResult = "pass";
				
				UpdateExpectedText(expectedResultStamp, actualResult);
				return; // do nothing else.
			}
			
			expectedResult = fetchExpectedText(expectedResultStamp);
			
			expectedResult = InteractionLayer.findUnicodeChars(expectedResult);
			// temp workaround for CP1252 character issue (CO2, EVL)
			//expectedResult = expectedResult.replace("CO2","CO?");
			if (textExclusionList.hashCode() != 3392903){
				expectedResult = filterDynamicTextElements(expectedResult); // dynamic replacement.
			}
			
			
			System.out.println(expectedResult);
			
			// handle the exclusion list
			break;
		case "array":
			// we're looking for dynamic text. 
			String varRef = expectedResultsGenericCheck[1];
			//actualResult = InteractionLayer.obtainTextFromElement(objectDesc);
			Boolean isPresent = InteractionLayer.findTextInString(objectDesc, varRef);
			
			if (isPresent == true){
				singleVPResult = "pass";
				//globalVPResult = "pass";
				vpDetail = "dynamic text "+ varRef + "found in scanned element / page text.";
				System.out.println(vpDetail);
				return;
			} else {
				singleVPResult = "fail";
				globalVPResult = "fail"; 
				vpDetail = "dynamic text "+ varRef + "NOT found in scanned element / page text.";
				System.out.println(vpDetail);
				return;
			}
			
			//break;
		
		default: // get from alternate source OR FIXED REFERENCE POINT
			break;
		}
		
		// have we got baseline results to compare against?
		if (expectedResult == null){
			System.out.println("possible missing baseline for VP " + ActorLibrary.operationalDataset + ": warning recorded");
			globalVPResult = "warning";
			singleVPResult = "warning";
			return;
		}
		
		// html treatment
		if (dimensionString.matches("(.*)html(.*)") == true){ // are we dealing with an HTML specific VP?
			//actualResult = InteractionLayer.obtainHTMLFromAUT(objectDesc); // get ALL HTML from current page.
			//actualResult = InteractionLayer.obtainTextFromElement(objectDesc);	
			if (textExclusionList.hashCode() != 3392903){
				actualResult = filterDynamicTextElements(actualResult); // dynamic replacement.
			}
			//actualResult = HTMLTidy(actualResult);
			
			//actualText = actualText.replace(trimRight," ");

			// flatten
			System.out.println("actual text after cleanup = " + actualResult);
			
		}

		// perform comparison
		
		int expHash = expectedResult.hashCode();
		int actHash = actualResult.hashCode();
		
		//if (expectedResult.equals(actualResult)){
  		if (expHash == actHash){
			singleVPResult = "pass";
			if (globalVPResult != "vp mismatch detected"){
				globalVPResult = "pass"; // set global to pass if there aren't any fails already.
			}
			System.out.println("text VP (" + ActorLibrary.operationalDataset + " passed, expected and actual results match");
			// update vp executed
		} else {
			
			vpDetail = "text VP (" + ActorLibrary.operationalDataset + ") failed, expected and actual results do not match";
			System.out.println(vpDetail);
			singleVPResult = "fail";
			globalVPResult = "vp mismatch detected";
			
		}
		

		
	}
	
	public static String fetchExpectedText(String expectedResultStamp){
		
		//DRP - visible container text - BODY/267/18
		
		String queryString = "SELECT EXPECTED_TEXT from EXPECTED_TEXT_STORE where RESULT_INDEX = '" + expectedResultStamp + "'";
		String table = "AVAILABLE_OPERATIONS";
		//String resultArray[];	// get control table.
		//String appDBmode = "core";
		HashMap<Integer,String> expectedResultMatrix = DataHandler.executeQuery(table,queryString,0,"core"); // establish the control table for operations.
		
		String expectedText = expectedResultMatrix.get(1);
		
		return expectedText;
	}
	
	public static void UpdateExpectedText(String expectedResultStamp, String text){
		
		//DRP - visible container text - BODY/267/18
		String queryString = "DELETE FROM EXPECTED_TEXT_STORE WHERE RESULT_INDEX = '" + expectedResultStamp + "'";
		String table = "AVAILABLE_OPERATIONS";
		//String resultArray[];	// get control table.
		//String appDBmode = "core";
		HashMap<Integer,String> dummyDelete = DataHandler.executeQuery(table,queryString,3,"core"); // establish the control table for operations.
		
		String timeStamp = Reporting.getTimeStamp();
	
		queryString = "INSERT INTO EXPECTED_TEXT_STORE (RESULT_INDEX, EXPECTED_TEXT, CREATION_TIMESTAMP) VALUES ('" + expectedResultStamp + "','" + text + "','" + timeStamp + "')";
		table = "AVAILABLE_OPERATIONS";
		//String resultArray[];	// get control table.
		//String appDBmode = "core";
		HashMap<Integer,String> expectedResultMatrix = DataHandler.executeQuery(table,queryString,3,"core"); // establish the control table for operations.
		
		//String expectedText = expectedResultMatrix.get(1);
		
		//return expectedText;
	}
	
	public static String filterDynamicTextElements(String rawText){
		
		String dynamicReplacement = "<DYN VALUE FOUND>"; // change this if neccesary.
		String[] exclusionArray = textExclusionList.split(";");
		// count the number of elements
		int elementFilterCount = exclusionArray.length;
		int i;
		String element = null;
		for (i = 0; i < elementFilterCount; i++){
			
			try {
				element = ActorLibrary.scenarioInfoContainer.get(exclusionArray[i]);
				
				if (element.hashCode() != 3392903){
					rawText = rawText.replace(element, dynamicReplacement);
				}
			
			} catch (NullPointerException e){
				System.out.println(e);
			}
				
			
		}
		// loop
		// obtain the dynamic values from the scenarioInfoContainer hashmap
		return rawText;
	}
	
	public static String HTMLTidy(String rawHTML){
		
		if (rawHTML == null){
			return rawHTML; // don't do anything.
		}
		
		dimensionString = dimensionString.replace(" | ", "��");
		
		String[] htmlTreatmentArray = dimensionString.split("��");
		String trimLeft = htmlTreatmentArray[1];
		String trimRight = htmlTreatmentArray[2];
		
		// trim
		
		rawHTML = rawHTML.replace("DIV","div");
		rawHTML = rawHTML.replace(". <",".<");
		rawHTML = rawHTML.replace("<A","<a");
		rawHTML = rawHTML.replace("</span>","</SPAN>");
		rawHTML = rawHTML.replace("label","LABEL");
		rawHTML = rawHTML.replace("img","IMG");
		rawHTML = rawHTML.replace("table>","TABLE>");
		rawHTML = rawHTML.replace("span class","SPAN class");
		rawHTML = rawHTML.replace("colspan","colSpan");
		rawHTML = rawHTML.replace("tbody>","TBODY>");
		rawHTML = rawHTML.replace("thead>","THEAD>");
		rawHTML = rawHTML.replace("₂","2");
		
		rawHTML = rawHTML.replace("td>","TD>");
		rawHTML = rawHTML.replace("tr>","TR>");
		rawHTML = rawHTML.replace("<td","<TD");
		//rawHTML = rawHTML.replace("	",""); // clear out tabs.
		
		rawHTML = rawHTML.replace("\n","");
		rawHTML = rawHTML.replace("\t","");
		rawHTML = rawHTML.replace("\"","");
		
		rawHTML = rawHTML.replace("  ","");
		rawHTML = rawHTML.replace("> <","><");
		
		if (trimLeft != null){
			String[] splitStart = rawHTML.split(trimLeft); // first split
			
			System.out.println(splitStart.length);
			
			if (splitStart.length == 1){
				return rawHTML; // get out, we can't split.
			}
				
			String firstTrim = splitStart[1];
			
			if (trimRight != null){
			String[] splitFinish = firstTrim.split(trimRight);
			rawHTML = splitFinish[0];	
			}
		}
		
		
		return rawHTML;
	}
}