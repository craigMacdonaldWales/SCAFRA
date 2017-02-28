package taf;

import java.util.Collection;
import java.util.HashMap;


public class OperationStepProcess {
	// OC CONTROL LEVEL
	public static String navigationInstructionSet;
	public static String dataIdentifier;
	public static String comments;
	public static String inputSkipFlag;
	public static String dropFlag;
	public static String controlledShutdownProcedure;
	// NAV LEVEL
	public static String navAction; // action to perform
	public static String navTarget; // gui object target
	public static String contextWindow; // the parent window (if applicable)
	public static String autTableName; // the name of the AUT input table (if applicable)
	public static String autTargetFieldGuiDesc; // the identifier for the field in the AUT GUI
	public static String autFieldInputValue; //  the finalized input value
	public static String autFieldAbsoluteRef; // the linking value between field data and field identifier (common key)
	public static String seleniumDesc;
	public static HashMap<Integer,String> navTargetOptions = new HashMap<Integer, String>();
	public static String stepResult;
	public static String declaredFieldType;
	//static String GUITranslatedAlreadyFlag;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
			// todo
	
		String operationExecResult = "pass"; 
		
	}
	public static String operationExecution(String operation, String operationalDataset){
		// process the instruction.
		String operationExecResult = "pass"; // set to fail if necessary.
		
		InteractionLayer.globalWait = Integer.parseInt(ActorLibrary.globalInfoContainer.get("maximum_wait_time_in_seconds"));
		
		switch(operation){
			case "verification_point": // how do verification points fit into the selenium model?
				//assert?
				Verification.verificatonPoint();
				break;
			case "batch_run":
				break;
			case "SQL_execute": // 
				StatementPrepareAndExecute.collectStatement(operationalDataset);
				operationExecResult = StatementPrepareAndExecute.execResult;
				return operationExecResult;
				// but what about external SQL files?
				//break;
			case "wait_specific_time":
				break;
			default:
				String queryString = "SELECT CONTROLLING_TABLE from AVAILABLE_OPERATIONS where OPERATION = '" + operation + "'";
				String table = "AVAILABLE_OPERATIONS";
				//String resultArray[];	// get control table.
				//String appDBmode = "core";
				HashMap<Integer,String> controlTableMatrix = DataHandler.executeQuery(table,queryString,0,"core"); // establish the control table for operations.
				
				String controlTable = controlTableMatrix.get(1);
				System.out.println("OC table target check for operation:" + operation + ", source data table for operation = " + controlTable);	
				
				queryString = "SELECT * from " + controlTable + " where OPERATIONAL_DATASET = '" + operationalDataset + "'";
				table = controlTable;
				
				HashMap<Integer,String> controlOperationMatrix = DataHandler.executeQuery(table,queryString,1,"core"); // collect operation information.
				
				// should only ever return a single row?
				int opControlCount = controlOperationMatrix.size();
				System.out.println("number of control rows returned for operational dataset " + operationalDataset + " = " + opControlCount);	
				
				String opControlHashOutput = controlOperationMatrix.get(1);
				
				//System.out.println("operational dataset control data returned: " + opControlHashOutput);	
				//convert the returned row.
				HashMap<String,String> opControl = DataHandler.convertRecordsetRowToArrayList(opControlHashOutput);
				
				navigationInstructionSet = opControl.get("REFERENCE_NAVIGATION_OPERATION");
				dataIdentifier = opControl.get("DATA_IDENTIFIER");
				comments = opControl.get("COMMENTS");
				//opControl("AUT_DATA_SOURCE");
				inputSkipFlag = opControl.get("INPUT_SKIP_FLAG");
				dropFlag = opControl.get("DROP_FLAG");
				controlledShutdownProcedure = opControl.get("CONTROLLED_SHUTDOWN_PROCEDURE");
				
				System.out.println("nav instructionset: " + navigationInstructionSet);	
				System.out.println("data identifier: " + dataIdentifier);
				System.out.println("comments: " + comments);
				System.out.println("input skip flag: " + inputSkipFlag);
				System.out.println("drop flag: " + dropFlag);
				System.out.println("controlled shutdown procedure: " + controlledShutdownProcedure);
				
				// process the operation
				//String stepResult;
				operationExecResult = processStep();// perform the physical actions.
				
		}
		
		
		//String operationExecResult = "PASS";
		return operationExecResult;
		
	}
	
	public static String processStep(){ // sequence and trigger nav actions.
		String returnedResult = "pass";
		stepResult = "pass";
		// get navigation records.
		String queryString = "SELECT * from NAVIGATION_DATA_BY_OPERATION where NAVIGATION_OPERATION_NAME = '" + navigationInstructionSet + "' ORDER BY NAVIGATION_INDEX";
		
		HashMap<Integer,String> controlNavMatrix = DataHandler.executeQuery("",queryString,1,"core"); // collect operation information.
		
		int navInstructionCount = controlNavMatrix.size(); 
		
		String executionRow;
		// execution loop. run through each 'row' in the scenario record
		// records without include flags are ignored.
		
		int navIndex;
		
		for (navIndex = 1; navIndex <= navInstructionCount; navIndex++){
			
			System.out.println("processing nav instruction " + navIndex + " of " + navInstructionCount);
			executionRow = controlNavMatrix.get(navIndex); // get the instructionset.
			
			HashMap<String,String> navInstructionRow = DataHandler.convertRecordsetRowToArrayList(executionRow);
			
			// nav action extraction function
			navAction = extractNavAction(navInstructionRow);
			System.out.println("navigation processing: detected action trigger = " + navAction + ", GUI target = " + navTarget);
			
			// nav execution function
			switch (navAction){
			case "AUT table input":
				// aut table input function
				stepResult = autTableInput();
				
				if (StatementPrepareAndExecute.execResult != "pass"){
					stepResult = "fail";
				}
				
				break;
			default:
				// trigger nav processing
				returnedResult = navigationProcessing();
				System.out.println("nav function returned result = "+ returnedResult);
				//stepResult = InteractionLayer.stepSuccess;
				switch (returnedResult){
				case "pass":
					break;
				case "fail":
					System.out.println("nav function returned fail - investigate");
					stepResult = "fail";
					
					return stepResult;
					//break;
				default: // null
					System.out.println("nav break!");
					stepResult = "pass";
					break;
				}
				
				
				break;
			}
			
			
			if (returnedResult != "pass"){
				return stepResult;
			}
			
		}		
			//System.out.println(debugPause);
			
		
		
		return stepResult;
		
	}
	
	
	public static String extractNavAction (HashMap<String,String> navInstructionRow){
		
		String action = null;
		String guiFocus = null;
		autTableName = null;
		navAction = null;
		navTarget = null;
		
		int guiFocusChk;
		int autTableChk;
		
		
		int keyIndex = 1;
		//
		contextWindow = navInstructionRow.get("CONTEXT_WINDOW");
		autTableName = navInstructionRow.get("AUT_TABLE_NAME");
		
		//cleanup the nav array.
		navInstructionRow.remove("CONTEXT_WINDOW");
		navInstructionRow.remove("NAVIGATION_INDEX");
		navInstructionRow.remove("AUT_TABLE_NAME");
		navInstructionRow.remove("NAVIGATION_OPERATION_NAME");
		navInstructionRow.remove("ID");
		navInstructionRow.remove("VERSION");
		navInstructionRow.remove("COMMENTS");
		navInstructionRow.remove("OPTIONAL_NAV");
		navInstructionRow.remove("NAV_DESTINATION");
		// leaving just the action to perform
		System.out.println("non option - context window: " + contextWindow);
		System.out.println("non option - aut table name " + autTableName);
		
		autTableChk = autTableName.hashCode();
		if (autTableChk != 3392903) // if table name is NOT NULL
		{
		
		switch (autTableName){
			case "<navigation_step_only>": // no table input
				//return action;
				break;
			default: // a value has been passed for aut table name, trigger table input functionality
				navAction = "AUT table input";
				navTarget = autTableName;
				System.out.println("AUT table input trigger detected (data input step): table " + autTableName);
				return navAction;
			}
		}
		Collection<String> keyset = navInstructionRow.keySet();
		String[] keyArray = keyset.toArray(new String[0]);
		
		System.out.println(keyArray);
		
		
		for (keyIndex = 1; keyIndex < keyArray.length-1; keyIndex++){
			action = keyArray[keyIndex];
			guiFocus = navInstructionRow.get(action);
			guiFocusChk = guiFocus.hashCode();
			// check that gui focus is not null (3392903)
			
			if (guiFocusChk != 3392903){
				System.out.println(action + " " + guiFocus);	
				// by this point we should only have an action and a target object.
				navAction = action;
				navTarget = guiFocus;
				//System.out.println(guiFocusChk);
				return action;
			}
				
			//System.out.println(action + " " + guiFocus);
		
		}
			
		//}
		
		return action;
		
	}
	
	public static String autTableInput(){
		
		String inputSuccess = "pass";
		
		String queryString = "SELECT * from "+ autTableName +" where DATA_IDENTIFIER = '" + dataIdentifier + "'";
		
		// collect input data.
		HashMap<Integer,String> inputDataPool = DataHandler.executeQuery("",queryString,1,"core"); // collect operation information.
		String dataPoolRaw = inputDataPool.get(1);
		
		//HashMap<String,String> integratedDataPool;
		
		inputDataPool.remove("DATA_IDENTIFIER");
		inputDataPool.remove("ID");
		inputDataPool.remove("VERSION");
		//HashMap<String,String> dataPoolRow = DataHandler.convertRecordsetRowToArrayList(dataPoolRaw);
		
		//Collection<String> keyset = dataPoolRow.keySet();
		
		System.out.println("data pool raw: " + dataPoolRaw);
		//		
		if (dataPoolRaw == null){ // no data found.
			inputSuccess = "fail";
			System.out.println("no AUT input data found for provided data identifer " + dataIdentifier + " which indicates a test config error. processing will abort");
			return inputSuccess;
		}
		
		String[] dataPool = dataPoolRaw.split("	");
			
		System.out.println("data pool raw: " + dataPool);
		// clean out the nulls
		int sourceColumnCount = dataPool.length;
		String[] fieldAbsoluteArray = new String[sourceColumnCount];
		int sourceColumnIndex;
		String colValue;
		String[] colValuePair;//= {null, null};
		String sendValue;
		HashMap<String,String> dataPoolPaired = new HashMap<String,String>(); 
		String column;
		
		int sequentialCount = 1;
		for (sourceColumnIndex = 1; sourceColumnIndex < sourceColumnCount; sourceColumnIndex++){
			colValue = dataPool[sourceColumnIndex];
			//System.out.println(colValue);
			colValuePair = colValue.split(";;");
			int lengthChk = colValuePair.length;
			//System.out.println("length check " + lengthChk);
			
			if (lengthChk > 1){
				sendValue = colValuePair[1]; // the value to send, literal or 'trigger'
				
				int sendValueHash = sendValue.hashCode();
	
					if (sendValueHash != 3392903){
						//System.out.println("sendValue is not null");
						column = colValuePair[0];
						switch (column){
							case "ID": // we don't care about this
								break;
							case "VERSION": // or this
								break;
							case "DATA_IDENTIFIER": // and not this.
								break;
							case "COMMENTS":
								break;
							default:
								System.out.println("colvalue " + sendValue);
								autFieldAbsoluteRef = column;
								autFieldInputValue = sendValue;
								System.out.println(autFieldAbsoluteRef);
								// trigger value processing
								DataHandler.triggerValueHandling(sendValue);
								
								dataPoolPaired.put(autFieldAbsoluteRef,autFieldInputValue);// add to hashmap.
								fieldAbsoluteArray[sequentialCount] = autFieldAbsoluteRef;
								sequentialCount++;
								break;
						}
						
						
						
					//} else {
						//dataPoolPaired.put(autFieldAbsoluteRef,"ignore");
						//sequentialCount++;
						//System.out.println("sendValue is null");
					}
							
				//}
			}
		}
		
		// input skip flag check: if input skip flag is NOT NULL, input will be skipped as this step is for a data collection.
		int skipValueHash = inputSkipFlag.hashCode();
		if (skipValueHash != 3392903){
			return inputSuccess; // get out of table input.
		}
		
		
		// use the HashMap to establish how many data items we need to treat.
		//int fieldItemsCount = dataPoolPaired.size();
		System.out.println("table: " + autTableName + ", number of fields to process = " + sequentialCount);
		
		//System.out.println(fieldAbsoluteArray);
		
		//String seleniumDesc;
		String AKA;
		String tabOrder;
		
		int field;
		// get the field_type_list info for the table.
		queryString = "SELECT SELENIUM_DESC, AKA, FIELD_INDEX, FIELD_TYPE from FIELD_TYPE_LIST where TABLE_NAME = '"+ autTableName + "' order by FIELD_INDEX";		
		HashMap<Integer,String> fieldGuiInfoGrid = DataHandler.executeQuery("",queryString,1,"core");
		int GUIFieldCount = fieldGuiInfoGrid.size();
		
		//String aka;
		for (field = 1; field <= GUIFieldCount; field++){
			//aka = fieldAbsoluteArray[field];
			
			String GUIInfoRow = fieldGuiInfoGrid.get(field);
			HashMap<String,String> fieldGUIInfoRow = DataHandler.convertRecordsetRowToArrayList(GUIInfoRow);
			
			seleniumDesc = fieldGUIInfoRow.get("SELENIUM_DESC");
			declaredFieldType = fieldGUIInfoRow.get("FIELD_TYPE");
			
			skipValueHash = seleniumDesc.hashCode();
			
			AKA = fieldGUIInfoRow.get("AKA");
			tabOrder = fieldGUIInfoRow.get("FIELD_INDEX");
			
			
			//if (skipValueHash != 3392903){// if there's no description provided, it must be a collection field (dummy).
			
			if (dataPoolPaired.containsKey(AKA)){
				
			autFieldInputValue = dataPoolPaired.get(AKA);
			
			if (autFieldInputValue != null){
				
				int ignoreValueHash = autFieldInputValue.hashCode();
				
					if (ignoreValueHash != 3392903){
						//send the value
						//navTarget = seleniumDesc;
						System.out.println("field "+ AKA + " input triggered, send value = " + autFieldInputValue);
						
						if (ActorLibrary.silent == "OFF"){ // silent mode
							Progressbar.propertyChangeData(AKA, autFieldInputValue);
						}
						// handle return code
						inputSuccess = InteractionLayer.sendValueToField(seleniumDesc, autFieldInputValue);
						
						if (inputSuccess != "pass"){
							return inputSuccess;
						}
						
					
					}
				}
			}
		
		}
	
	
		return inputSuccess;
		
	}
	
	public static String navigationProcessing(){
		
		InteractionLayer.stepSuccess = "pass";
		
		if (navAction != "AUT table input"){
			isTargetLegacyDesc(); // handle object desc string conversion.
		}
		
		String navSuccess = "pass";
		
		switch (navAction){
			case "BUTTON_PRESS":
				break;
			case "STATUSBAR_SYNC":
				break;
			case "WAIT_SPECIFIC_TIME":
				int milliseconds = (Integer.parseInt(navTarget) * 1000);
				InteractionLayer.dumbWait(milliseconds);
				break;
			case "COMMENTS":
				break;
			case "LINK_CLICK":
				System.out.println("obj click: " + navTarget);
				navSuccess = InteractionLayer.objectClick();
				break;
			case "CLOSE_SPECIFIC_WINDOW":
				break;
			case "WAIT_FOR_WINDOW":
				break;
			case "BROWSER_URL_SELECT":
				navSuccess = InteractionLayer.browserURLSelect(navTarget);
				break;
			case "BROWSER_INVOKE":
				try{
				InteractionLayer.browserInvoke();
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					}
				break;
			case "KILL_WINDOWS":
				InteractionLayer.killBrowser();
				break;
			case "WIN_TYPE":
				break;
			case "WIN_TREAT":
				// need to deal with specific treatments eventually
				InteractionLayer.maximiseBrowser();
				break;
			case "NAV_DESTINATION":
				break;
			case "OPTIONAL_NAV":
				break;
			case "OBJ_CLICK":
				System.out.println("obj click: " + navTarget);
				navSuccess = InteractionLayer.objectClick();
				break;
			case "OBJ_MOUSEOVER":
				break;
			case "WAIT_FOR_OBJECT":
				if (navTarget != null){
					navSuccess = InteractionLayer.isElementPresent(navTarget);
			}
			break;
		default:
			break;
		}
		
		
		
		navAction = null;
		return navSuccess;
	
	}
	
	public static void isTargetLegacyDesc(){
		// check to see if provided description is a legacy (QTP) descriptor.
		// if so, trigger translation based on original class.
		// winrunner delimiter: ','
		// QTP delimiter: '+'
		
		if (Startup.verAppind == null){
			return; // no conversion necessary
		}
		
		
		if (navTarget == null){
			return;
		}
		
		
		if (navTarget.matches("(.*)//(.*)") == true){
			// we've already got a 'valid' Selenese GUI description.
			//GUITranslatedAlreadyFlag = "yes";
			return;
		}
		
		//winrunner example
		// {class: object,MSW_class: html_rect,html_name: "Apply for a tax disc"}
		
		//QTP example
		// Class Name=Image+alt=Apply for a tax disc
		
		//selenium example
		// //img[@alt='Apply for a tax disc']
		
		
		// get alias?
		//String verAppKey = ActorLibrary.globalInfoContainer.get("VERSION_APPEND_INDEX");
		
		String GUIqueryString = "select SUBSTITUTE_VALUE from VERSION_VALUE_SUBSTITUTE_TABLE where ORIGINAL_VALUE = '" + navTarget + "' and VERSION_key = '" + Startup.verAppind +"'";
		HashMap<Integer,String> GUIreturn = DataHandler.executeQuery("",GUIqueryString,0,"core");
		
		String GUIalias = GUIreturn.get(1);
		
		System.out.println("gui string returned = " + GUIalias);
		
		if (GUIalias != null){
			navTarget = GUIalias; // only assign the alias value if one exists.
		} else {
			System.out.println("no gui alias value returned. is this correct?");
		}
		
		// translate
		GUITranslate();
		// check that object is visible in webpage?
		
		
	}
	
	public static void GUITranslate(){
		
		if (navTarget.matches("(.*)//(.*)") == true){
			// we've already got a 'valid' Selenese GUI description.
			//GUITranslatedAlreadyFlag = "yes";
			return;
		}
		
		if (navTarget.matches("(.*):(.*)") == true){
			// it's a winrunner description
			WRDescConvert();
			return;
		}
		
		if (navTarget.matches("(.*)alt(.*)") == true){
			// it's a QTP description
			QTPDescConvert();
			return;
		}
		
		
		
	}
	
	public static void WRDescConvert(){
		
		// get rid of the curly brackets
		navTarget = navTarget.replace("{","");
		navTarget = navTarget.replace("}","");
		
		String[] translateArray = navTarget.split(",");
		
		int pairCount = translateArray.length;
		
		int pairInd;
		String xpathTag = "";
		String alt = "";
		String seleniumXpathDesc;
		
		for (pairInd = 1; pairInd < pairCount; pairInd++){
			
			String pair = translateArray[pairInd];
			String[] propValPair = pair.split(":");
			
			String property = propValPair[0];
			String value = propValPair[1];
			
			
			switch (property) 
			{
				case "html_name":
					alt = value;
					break;
				case "MSW_class":
					switch (value){
					case " html_rect":
						xpathTag = "img";
						break;
					case "html_rect":
						xpathTag = "img";
						break;
					default:
						xpathTag = "img";
					}
					
					break;
			
			}
			
		}
		
		//img[@alt='Apply for a tax disc']
		seleniumXpathDesc = "//" + xpathTag + "[@alt=" + alt + "]";
		System.out.println("xpath desc assembled = " + seleniumXpathDesc);
		
		navTarget = seleniumXpathDesc;
		
	}
	
	public static void QTPDescConvert(){
		
		if (navTarget.matches("(.*)//(.*)") == true){
			// it's already an xpath identifier. do nothing.
			//WRDescConvert();
			System.out.println("selenium description detected.");
			return;
		}
		
		//input[@name='Next']
		// get rid of the curly brackets
				navTarget = navTarget.replace("+",";;");
				navTargetOptions.clear(); // empty and reset.
				//navTargetOptions.();
				//navTarget = navTarget.replace("}","");
				
				String[] translateArray = navTarget.split(";;");
				
				int pairCount = translateArray.length;
				
				int pairInd;
				String xpathTag = "";
				String alt = "";
				String seleniumXpathDesc;
				String[] elementTypeOptionArray = {"img","input"}; // options where a single button can actually span 2 object classes.
				String[] identifierOptionArray = {"alt","name", "id"};
				
				int classOptIndex;
				//int descOptIndex = 0;
				for (classOptIndex = 0; classOptIndex < elementTypeOptionArray.length; classOptIndex++){
					String classOption = elementTypeOptionArray[classOptIndex];
	
					for (pairInd = 0; pairInd < pairCount; pairInd++){
						
						String pair = translateArray[pairInd];
						String[] propValPair = pair.split("=");
						
						String property = propValPair[0];
						String value = propValPair[1];
						
						String identifier = "name";
						
						//int idOpt;
						//for (idOpt = 0; idOpt < identifierOptionArray.length; idOpt++)
						
							//identifier = identifierOptionArray[idOpt];
						
							switch (property) 
							{
								case "alt":
									alt = value + "']";
									break;
								case "Class Name":
									switch (value){
									case "Image":
										xpathTag = classOption + "[@name='";
										//xpathTagAlt 
										break;
									case " Image":
										xpathTag = classOption + "[@name='";
										break;
									default:
										xpathTag = classOption + "[@name='";
									}
									
									break;
							}
			
					seleniumXpathDesc = "//" + xpathTag + alt;
					navTargetOptions.put(classOptIndex, seleniumXpathDesc);
					//classOptIndex++;
					}	
				
				}	
				
				int idOpt;
				int arrscan;
				String desc;
				String option;
				
				//classOptIndex = classOptIndex+1;
				
				int descCount = navTargetOptions.size();
				int tempcount = descCount;
				for (arrscan = 0; arrscan < descCount; arrscan++){
					desc = navTargetOptions.get(arrscan);
					
					for (idOpt = 0; idOpt < identifierOptionArray.length; idOpt++){
						option = identifierOptionArray[idOpt];
						desc = desc.replace("name",option);
						navTargetOptions.put(tempcount, desc);
						
						tempcount++;
					}
					
					
				}
				
				
				//img[@alt='Apply for a tax disc']
				seleniumXpathDesc = "//" + xpathTag + alt;
				System.out.println("xpath desc assembled = " + seleniumXpathDesc);
				
				navTarget = seleniumXpathDesc;
			
	}
}