package taf;
//import ConnectionHandler;
//import DataHandler;
//import ExcelInterrogation;
//import InteractionLayer;
//import OperationStepProcess;
//import ReadFile;
//import Reporting;
//import Startup;
//import StatementPrepareAndExecute;
//import Verification;
//import DataHandler,java;
import java.util.HashMap;

public class ActorLibrary {
	/**
	 * levels:
	 * test execution
	 * 		scenario execution
	 * 			operation execution
	 * 				navigation (& verification)
	 * 					data input
	 * fsdsdf
	**/
	// scenario level
	public static String scenarioIndex;
	static String includeFlag = null;
	static String operation;
	static String operationalDataset;
	static String debugPause = null;
	static String versionBranch;
	static String comments;
	static String machine;
	static String subSystem;
	static String runName;
	static String sequenceIndex;
	static String failurePoint;
	static String scenarioResult;
	static int stepIndex;
	static double stepSequenceIndex;
	static String rawStepSequence;
	static String containerFilespace;
	static boolean ignoreDebugFlags;
	static String silent;
	public static String failCaptureScreenshot;
	public static HashMap <String,String> globalInfoContainer = new HashMap<String, String>(); // config info.
	//public static HashMap <String,String> globalInfoContainer;
	public static HashMap <String,String> scenarioInfoContainer = new HashMap<String, String>(); // scenario storage
	//static HashMap <String,String> scenarioDataContainer = new HashMap(); // scenario storage
	// operation
	public static String testDir;
	public static String centralRepository;
	
//	public static Spine actorSpine = new Spine();
	
	public static SpineReturn main(String[] argArray){
		boolean individualScInd = false;
		
		containerFilespace = "C:/TAF/";
		centralRepository = "//pact02/winrunner/autoframework/framework output/"; // overridable
	//public static void main (String){
		// put something here.
		//String[] argArray = args;
		
		// command line arguments:
		// 1. machine name
		// 2. mode (0 = normal, 1 update)
		// 3. scenario index
		// 4. subsystem
		
		//for (String s: argArray){
			//System.out.println(s);
		//}
		globalInfoContainer = Startup.LoadIni();
		String execResult;
		//execResult = scenarioExecution("1"); // comment this line out to run everything and for 'proper' builds
		
		int cmdArgs = argArray.length;
		System.out.println("number of command line arguments passed = " + cmdArgs);
		
		if (cmdArgs > 0){ // we've received some command line arguments. act upon them.
			
			int i;
			for (i = 0; i < cmdArgs; i++){
				System.out.println(argArray[i]);
				
				String[] cmdSplit = argArray[i].split(":");
				String cmdOverride = cmdSplit[0];
				String overrideVal = cmdSplit[1];
				// ADD MORE OPTIONS TO THIS SWITCH AS FUNCTIONALITY EXPANDS.
				// IF 
				switch (cmdOverride.toUpperCase()){
					case ("IGNOREDEBUGFLAGS"): 
						ignoreDebugFlags = Boolean.valueOf(overrideVal);
						break;
					case ("MODE"):  // PROTECTED IF DECLARED
						Startup.RUN_MODE = Integer.valueOf(overrideVal);
						
						if (cmdOverride.toUpperCase() == "1"){
							System.out.println("run mode = update");
						}
						
						break;
					case ("SCENARIOINDEX"):  // USE TO TRIGGER SCENARIO EXECUTION ONLY.
						scenarioIndex = overrideVal;
						System.out.println("specific scenario triggered:" + scenarioIndex);
						individualScInd = true; // don't do anything else.
						// hand driver back?
						//return InteractionLayer.driver;
						break;
					case ("MACHINE"): // PROTECTED IF DECLARED
						machine = overrideVal;
						break;
					case ("SUBSYSTEM"): // PROTECTED IF DECLARED
						subSystem = overrideVal;
						break;
					case ("RESULTDESTINATION"):
						//"
						centralRepository = overrideVal;
						break;
					case ("SILENT"): // don't activate the progressmeter
						silent = overrideVal;
						break;
					default:
						break;
				}
							
			}
			
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			if (individualScInd){
				execResult = scenarioExecution(scenarioIndex); // JUST RUN THE SCENARIO.
				System.out.println("scenario "+ scenarioIndex + " executed. Result = " + execResult);
				
			} else {
				fireOffTests(); // run everything
			}
		
		}
			//String execresult;
		
		SpineReturn rs = new SpineReturn(InteractionLayer.driver, scenarioInfoContainer);
		
		return rs;
	}
	
	public static void fireOffTests(){
	//Connection connection;
			
			runName = globalInfoContainer.get("run_name");
					
			testDir = centralRepository + runName;
			
			InteractionLayer.makeDir(testDir); // generate results folder
			
			//Connection connection = connectionHandler.connectionManageFWDB(connection); // connect to the FWDB.
			testExecution(); // uncomment this line to run everything. 
			
			
			// consider using an OBJECT to store test results & global config info?

			// put test execution function at this level.
			//String testResult;
			//scenarioExecution("358");
			//testResult = scenarioExecution("358");
	
	//public enum scenarioParams {
		
	}

	public static String scenarioExecution(String scenarioIndexref) {
		// return test result.
		
		scenarioInfoContainer.clear(); // clear out existing test data.
		
		scenarioIndex = scenarioIndexref;
		
		scenarioInfoContainer.put("STATUS", "pass"); // assume it's passing until / if a fail occurs
		
		scenarioResult = "PASS"; // reporting
		String execResult = "pass"; //add more classifications later based on return from operation execution.
		
		String queryString = "SELECT * from SCENARIO_CONTROL_TABLE where SCENARIO_INDEX = " + scenarioIndex + " and INCLUDE_FLAG is not null order by OPERATION_SEQUENCE_INDEX";
		
		String table = "SCENARIO_CONTROL_TABLE";
		//String resultArray[];	
		HashMap<Integer,String> operationMatrix = DataHandler.executeQuery(table,queryString,1,"core");
		
		int ScInstructionCount = operationMatrix.size(); // number of operations to perform
		
		System.out.println("total number of operations to execute for scenario "+ scenarioIndex +" = " + ScInstructionCount);
 		
		
		//float stepPercentage = 0;
		float step = 0;
		
		//stepPercentage = (float) ScInstructionCount * t;
		float percentComplete = 0;
		
		dialogs.scenarioProgressMeter("new",0);
		
		String executionRow;
		// execution loop. run through each 'row' in the scenario record
		// records without include flags are ignored.
		
		failurePoint = null; // reset.
		// attempts loop here, enclose the scenario execution loop!
		for (stepIndex = 1; stepIndex <= ScInstructionCount; stepIndex++){
			
			executionRow = operationMatrix.get(stepIndex); // get the instructionset.
			
			step = stepIndex;
			
			//System.out.println("raw instructions for operation step "+ operationIndex + " of " + ScInstructionCount + "= " + executionRow);
			
			//String[] commandParams = new String[8]; 
			//commandParams = executionRow.split(",",8); // this will give us the column/value pairings
			
			//int commandParamCount = commandParams.length;
			//int pair = 1;
			//String[] command = new String[2];
			//String property;
			//String value;
			// cycle through each property / value pairing to obtain operation and operational dataset
			System.out.println("processing operation " + stepIndex + " of " + ScInstructionCount);
			
			HashMap<String,String> scInstructionRow = DataHandler.convertRecordsetRowToArrayList(executionRow);
			
			includeFlag = scInstructionRow.get("INCLUDE_FLAG");
			System.out.println("include flag assessed " + includeFlag);
			operation = scInstructionRow.get("OPERATION");
			operationalDataset = scInstructionRow.get("OPERATIONAL_DATASET");
			debugPause = scInstructionRow.get("DEBUG_PAUSE");
			rawStepSequence = scInstructionRow.get("OPERATION_SEQUENCE_INDEX");
			
			//try{
//			stepSequenceIndex = Integer.parseDouble(rawStepSequence);
//			} catch (NumberFormatException e){
//				System.out.println(e);
//			}
//			
			
			System.out.println(debugPause);
			int debugPauseChk = debugPause.hashCode();
			
			
			if (ignoreDebugFlags != true){
				if (debugPauseChk != 3392903){
					System.out.println("debug pause called");
					//pair = commandParams.length; // abort loop.
					//JOptionPane.showMessageDialog(null,"Debug pause called for step: "+ operationalDataset);
					int option = dialogs.debugPauseDialog();
					System.out.println("debug pause option selected = " + option);
					
					switch (option){
						case 0:// terminate
							System.exit(0);
							break;
						case 1:
							break;
						case 2:
							break;
					}
					
					
				} 
				
			}
			debugPause = scInstructionRow.get("VERSION_BRANCH");
			comments = scInstructionRow.get("COMMENTS");
		
			System.out.println(comments);
			//break;
		
				// use collected information to execute step.
				
			// max number of repeat attempts.
			System.out.println("operation called:" + operation + ", operational dataset: " + operationalDataset);
			// execute operation here.
			execResult = OperationStepProcess.operationExecution(operation,operationalDataset);
			
			if (execResult != "pass"){ // input action step pass, not verification failure (flow level fail)
				System.out.println("scenario " + scenarioIndex + ": step '" + operationalDataset + "' failed.");
				failurePoint = operationalDataset;
				//scenarioResult = "FAIL";
				execResult = "restarted";
				scenarioInfoContainer.put("STATUS", "fail");
				dialogs.scenarioProgressMeter("Done",100);
				//scenariosExecutedInsert
				return execResult;
			}
			
				System.out.println("scenario " + scenarioIndex + ": step '" + operationalDataset + "' passed.");
				
				percentComplete = (step / ScInstructionCount) * 100;
				System.out.println("scenario percentage complete = " + percentComplete);
				dialogs.scenarioProgressMeter("increment",(int) percentComplete);
		}
			//}
			
		//}
		
		System.out.println("scenario " + scenarioIndex + ": execution complete.");
		
		
		//failurePoint = null;
		//scenarioResult = "PASS";
		execResult = "completed";
		
		dialogs.scenarioProgressMeter("finished",100);
		
		return execResult;
		
	}
	
	
	
	public static boolean nullChk(String S){
		if (S == null){
			System.out.println("null value detected");
			return true;
		}
		System.out.println("string not null");
		return false;
		
	}
	
	
	public static void testExecution() { // run everything as configured.
		
		// startup - write code for ini file and DB startup config info collection
		
		
		String applicationUnderTest = globalInfoContainer.get("APP_PLATFORM");
		
		if (machine == null){
			machine = globalInfoContainer.get("wr_machine_name");
		}//String subsystem = globalInfoContainer.get("subsystem");
		
		Reporting.sequenceStoreUpdate("startup");
		
		String table = "SCENARIO_INDEX_TABLE";
		// sequence store check (on auto resume)
		
		// query with no subsystem declared
		//String queryString = "SELECT scenario_index, execution_sequence from SCENARIO_INDEX_TABLE where APPLICATION = '" + applicationUnderTest + "' and ACTIVE is not null and " + machine + " is not null order by EXECUTION_SEQUENCE";
		String queryString = "SELECT scenario_index, execution_sequence from SCENARIO_INDEX_TABLE where APPLICATION = '"+ applicationUnderTest +"' and ACTIVE is not null and " + machine + " is not null and " +
				"EXECUTION_SEQUENCE >=" + 
				"("+
				"SELECT SEQUENCE_STORE.SEQUENCE FROM SEQUENCE_STORE WHERE MACHINE = '" + machine + "' and run_name = '" + runName + "'" +
				")"+
				"order by EXECUTION_SEQUENCE";
		
		// query wiut
		 // create a row.
		
		HashMap<Integer,String> scenarioWorkloadMatrix = DataHandler.executeQuery(table,queryString,1,"core"); // get list of scenarios to execute.
		
		int scenarioCount = scenarioWorkloadMatrix.size(); // number of operations to perform
		
		System.out.println("total number of scenarios to execute = " + scenarioCount);
 		
		int scenarioRecord;
		
		for (scenarioRecord = 1; scenarioRecord <= scenarioCount; scenarioRecord++){
			String rawInstRow = scenarioWorkloadMatrix.get(scenarioRecord);
			//String[] scCommandParams = new String[3]; 
			//scCommandParams = rawInstRow.split(",",3);
			
			HashMap<String,String> scControl = DataHandler.convertRecordsetRowToArrayList(rawInstRow);
			
			scenarioResult = "PASS";
			
			scenarioIndex = scControl.get("SCENARIO_INDEX");
			sequenceIndex = scControl.get("EXECUTION_SEQUENCE");
			
			Reporting.sequenceStoreUpdate("update");
			
			System.out.println("scenario index for scenario " + scenarioRecord + " of " + scenarioCount + " = " + scenarioIndex);	
			
			//}
			
			
			// stamp the sequence store table.
	 			
		
			int maxRepeatAttempts = 2;
			int attempt;
			
			String scenarioExecResult = null;
			//int scenarioIndexAsInt = Integer.parseInt(scenarioIndex);
			
			for (attempt = 1; attempt <= maxRepeatAttempts; attempt++){
				scenarioExecResult = scenarioExecution(scenarioIndex); // run the scenario.
				
				if (scenarioExecResult == "completed"){
					attempt = maxRepeatAttempts+1; // break out of restart loop.
				}
				
				//attempt++;
			
			}
			
			switch (scenarioExecResult){
			case "restarted":
				scenarioResult = "FAIL";
				// get screenshot.
				InteractionLayer.screenCapture();
				break;
			case "completed":
				
				switch(Verification.globalVPResult){
					case "warning":
						scenarioResult = "WARNING";
						break;
					case "vp mismatch detected":
						scenarioResult = "VP MISMATCH DETECTED";
						break;
					case "pass":
						scenarioResult = "PASS";
				}
				
				
				break;
			default: // termimated.
				// TERMINATED
				scenarioResult = "SCENARIO_TERMINATED";
				// get screenshot.
				InteractionLayer.screenCapture();
				
			}
			
			Reporting.scenariosExecutedInsert();
			
			
		}
	System.out.println("test execution finished");
		
	System.exit(0);
	}

	
}

