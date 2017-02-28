package taf;
//import java.util.date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Reporting {
	
	public String resultClass;
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void sequenceStoreUpdate(String mode){
		
		String query = null;
		String preQuery = null;
		
		switch (mode){
		case "startup":
			// check for an existing record.
			preQuery = "select SEQUENCE FROM SEQUENCE_STORE WHERE RUN_NAME = '" + ActorLibrary.runName + "' and MACHINE = '" + ActorLibrary.machine + "'";
			
			HashMap<Integer,String> preQueryReturn = DataHandler.executeQuery("",preQuery,0,"core");
			
			String alreadyStarted = preQueryReturn.get(1);
			
			if (alreadyStarted != null){
				return; // don't insert another record.
			}
			query = "insert into SEQUENCE_STORE (RUN_NAME, SEQUENCE,MACHINE) values ( '"+ ActorLibrary.runName +"', '0', '"+ ActorLibrary.machine +"')";
			
			break;
		case "update":
			query = "update SEQUENCE_STORE set SEQUENCE = '" + ActorLibrary.sequenceIndex + "' where RUN_NAME = '" + ActorLibrary.runName + "' and MACHINE = '" + ActorLibrary.machine + "'";
			break;
		}
		
		StatementPrepareAndExecute.executeLocalStatement(query);
			
	}
	
	public static void scenariosExecutedInsert(){

		String query = null;
		
		String timeStamp = getTimeStamp();
		
		query = "insert INTO SCENARIOS_EXECUTED (RUN_NAME, SCENARIO_INDEX, MACHINE, TIME, PASS_FAIL,FAILURE_POINT) values ( '"+ ActorLibrary.runName +"', '" + ActorLibrary.scenarioIndex +"', '"+ ActorLibrary.machine +"',  '"+ timeStamp +"', '"+ ActorLibrary.scenarioResult +"', '"+ ActorLibrary.failurePoint + "')";
		
		StatementPrepareAndExecute.executeLocalStatement(query);		
						
	}
	
	public static void VPExecutedInsert(){

		//REM INSERTING into VP_EXECUTED
	
		String query = null;
		
		String timeStamp = getTimeStamp();
		
		//insert INTO VP_EXECUTED (RUN_NAME, SCENARIO_INDEX, MACHINE, TIME, PASS_FAIL,FAILURE_POINT) values ( '"+ ActorLibrary.runName +"', '" + ActorLibrary.scenarioIndex +"', '"+ ActorLibrary.machine +"',  '"+ timeStamp +"', '"+ ActorLibrary.scenarioResult +"', '"+ ActorLibrary.failurePoint +"')";
		query = "Insert into VP_EXECUTED (RUN_NAME,SCENARIO_INDEX,STEP,VP_CHECKPOINT_NAME,VP_TYPE,DYNAMIC_FLAG,MACHINE,TIME,RESULT,REPORT_LINK,DETAIL,ORA_TIMESTAMP) values (" +
				"'"+ ActorLibrary.runName + "'," +
				"'"+ ActorLibrary.scenarioIndex + "'," +
				"'"+ ActorLibrary.rawStepSequence + "'," +
				"'"+ ActorLibrary.operationalDataset +"'," +
				"'"+ Verification.vpType +"'," +
				"''," +
				"'" + ActorLibrary.machine + "',"+
				"'" + timeStamp + "',"+
				"'"+ Verification.singleVPResult + "',"+
				"'"+ Verification.vpDetail + "',"+
				"'"+ Verification.expectedResult + " " +  Verification.actualResult + "',"+ 
				"'')";
		
		StatementPrepareAndExecute.executeLocalStatement(query);		
						
	}
	
	
	public static String getTimeStamp(){
		String stamp = null;

		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		//Date date = new Date(0, 0, 0);
		Calendar cal = Calendar.getInstance();
		stamp = dateFormat.format(cal.getTime());
		
		System.out.println(stamp);
		
		
		return stamp;
		
	}
	
	public static String getTimeStampForFailCapture(){
		String stamp = null;

		
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy HH:mm:ss");
		//Date date = new Date(0, 0, 0);
		Calendar cal = Calendar.getInstance();
		stamp = dateFormat.format(cal.getTime());
		
		System.out.println(stamp);
		
		
		return stamp;
		
	}

//	framework_DB_query = "INSERT INTO  EXPECTED_TEXT_STORE (RESULT_INDEX, EXPECTED_TEXT) VALUES ('" + expected_results_index + "','" + query_value + "')"
//
//
//
//			framework_DB_query = "UPDATE EXPECTED_TEXT_STORE  set EXPECTED_TEXT = '" + query_value + "' where RESULT_INDEX = '" +  expected_results_index + "'"
//
//			framework_DB_query = "delete from SEQUENCE_STORE where RUN_NAME = '"+ run_name +"' and MACHINE = '"+ WR_MACHINE_NAME + "'"
//
//
//			framework_DB_query = "SELECT  SEQUENCE from SEQUENCE_STORE  where RUN_NAME ='"+ run_name +"' and  MACHINE = '"+ WR_MACHINE_NAME +"'"
//
//
//			framework_DB_query = "insert INTO SCENARIOS_EXECUTED (RUN_NAME, SCENARIO_INDEX, MACHINE, TIME, PASS_FAIL,FAILURE_POINT) values ( '"+ run_name +"', '" + scInd +"', '"+ WR_MACHINE_NAME +"',  '"+ timeStamp +"', '"+ query_value +"', '"+ failurePoint +"')"
//
//			framework_DB_query = "Insert into SCENARIO_DETAIL_LISTING (SCENARIO_INDEX,OPERATION,OPERATIONAL_DATASET,RUN_NAME,STEP,MACHINE,DETAIL,REPORT_LINK,PASS_FAIL,STAMP_TIME) values  ('"+ scInd  +"', '"+  operation +"', '"+ operational_dataset +"', '"+  run_name +"','"+ operationSequenceIndex +"','"+ WR_MACHINE_NAME +"','"+ imgCaptured +"','','"+ query_value +"','"+ timeStamp +"')"
//
//				framework_DB_query = "insert INTO FAILED_NAV (NAVIGATION_OPERATION_NAME,NAV_STRING) values ( '"+ NAVIGATION_OPERATION_NAME  +"', '"+ query_value +"')"
//
//			framework_DB_query = "insert INTO VP_EXECUTED (RUN_NAME,SCENARIO_INDEX,STEP,VP_CHECKPOINT_NAME,VP_TYPE,MACHINE,TIME,ORA_TIMESTAMP,RESULT, REPORT_LINK, DETAIL) values ( '"+ run_name  +"', '"+ scenario_index +"', '"+ step_index + "','" + checkpoint_dataset + "','"+ checkpoint_type +"',  '"+ WR_MACHINE_NAME +"','" + timeStamp + "'," + "to_date('" + timeStamp + "', 'HH24:MI:SS DD/MM/YYYY'), '" + result + "', '" + reportLink + "','" + detail + "' )"


	
	
}