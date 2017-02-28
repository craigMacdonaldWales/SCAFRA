package taf;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class StatementPrepareAndExecute {

	
	static String sqlString;
	static String filename;
	static String variableElements;
	static String comments;
	static String appDBMode;
	static String statementType;
	static int selectGridRowCount;
	public static String execResult = "pass";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	// collect statement from VP_SQL_STORE table, analyze elements, replace with values if neccesary
	public static void collectStatement(String statementDataset){
		
		execResult = "pass";
		
		String queryString = "SELECT SQL_STRING, FILENAME, VARIABLE_ELEMENTS,COMMENTS, APP_DB_MODE FROM VP_SQL_STORE WHERE OPERATIONAL_DATASET = '" + statementDataset +"'";
		
		HashMap<Integer,String> SQLStatementInfo = DataHandler.executeQuery("",queryString,2,"core"); // establish the control table for operations.
		
		sqlString = SQLStatementInfo.get(1);
		
		if (sqlString == null){
			System.out.println("SQL statement for statement dataset '" + statementDataset + "'  is null. processing will abort.");
			return;
		}
		
		// check to see if sqlString refers to a text or .sql file, and if so, load it into memory.		
		SQLSourceChecker();
		
		filename = SQLStatementInfo.get(2);
		variableElements = SQLStatementInfo.get(3);
		comments = SQLStatementInfo.get(4);
		appDBMode = SQLStatementInfo.get(5);
		
		//System.out.println(sqlString + " " + filename + " " + variableElements + " "+ comments + " " + appDBMode);
		
		prepareStatement();
		
		setStatementType(sqlString); //recheck.
		
		if (sqlString != null){
			executeExternalStatement(sqlString);
		} else {
			System.out.println("sql statement obtained is null. investigate");
			//warning??
			//execResult = "";
		}
		
		if (DataHandler.totalRowsFound == 0){
			execResult = "fail";
		}
		
		
		
	}
	
	public static void prepareStatement(){ // local against FWDB but non-core
		
		if (variableElements == null){
			System.out.println("no variable elements detected for statement");
			return;
		}
		
		if (sqlString == null){
			System.out.println("no sql to manipulate.");
			return;
		}
		
		// does string 
		variableElements = variableElements.replace("$$",",");
		
		String[] variableElementSplit = variableElements.split(",");
		int elementCount = variableElementSplit.length;
		System.out.println("number of variable element references found = " + elementCount);
		
		String valueReference;
		String liveValue;
		String tempSQL;
		int elementRef = 0;
		
		tempSQL = sqlString;
		
		while (elementRef < elementCount){
		//for (elementRef = 1; elementRef <= elementCount; elementRef++){
			
			valueReference = variableElementSplit[elementRef].toUpperCase();
			// obtain the corresponding pre-loaded value.
			liveValue = captureStoredElement(valueReference);
			
			System.out.println("element to capture = "+ valueReference + ", actual value captured = " + liveValue);
			String elementIndStr = String.valueOf(elementRef+1);
			System.out.println(elementIndStr);
			
			tempSQL = tempSQL.replace("'"+ elementIndStr + "'", "'" + liveValue + "'");
			sqlString = sqlString.replace("'"+ elementIndStr + "'", "'" + liveValue + "'");
			
			//sqlString = sqlString.replace(elementIndStr,liveValue);
			
			elementRef++;
		}
		
		System.out.println("statement post treatment = " + sqlString);
		//System.out.println("statement post treatment = " + tempSQL);
		
	}
	
	public static String captureStoredElement(String elementRef){
		
		String caughtElementRef = ActorLibrary.scenarioInfoContainer.get(elementRef);
		
		
		return caughtElementRef;
	}
	
	public static void executeLocalStatement(String SQLstatement){ // local against FWDB but non-core
		
		HashMap<Integer,String> dummyContainer = DataHandler.executeQuery("",SQLstatement,3,"core");
		dummyContainer.clear();
		
	}
	
	public static void executeExternalStatement(String SQLstatement){ // fire off the query
		
		if (sqlString == null){
			return;
		}
		
		setStatementType(sqlString);
		
		System.out.println("SQL statement assessed as type:" + statementType);
		
		
		// are we assuming we are running a targeted query that will only return one row?
		// what kind of query are we running?
		// if it's a select statement, generate and collect. 
		switch (statementType){
		case "select":
			HashMap<Integer,String> SQLresultSet = DataHandler.executeQuery("",sqlString,1,appDBMode); // establish the control table for operations.			
			selectGridRowCount = SQLresultSet.size(); // how many rows
			
			if (selectGridRowCount != 0){
				loadGridIntoContainer(SQLresultSet);	
			}
			//ActorLibrary.scenarioInfoContainer.put(elementRef);
//		case "other":
//			HashMap<Integer,String> SQLresultSet1 = DataHandler.executeQuery("",sqlString,1,appDBMode); // establish the control table for operations.			
//			selectGridRowCount = SQLresultSet1.size(); // how many rows
//			loadGridIntoContainer(SQLresultSet1);
			break;
		case "delete":
			DataHandler.executeUpdate(sqlString, appDBMode); // no resultset to handle.
			break;
		case "update":
			DataHandler.executeUpdate(sqlString, appDBMode); // no resultset to handle.
			break;
		default:
			// do nothing (for now)
		}
		// splice hashmap & load collected values into 
		//ActorLibrary.scenarioInfoContainer.put(elementRef);
		
		//for any other statement, disregard.
		
		
	}
	
	public static void setStatementType(String SQLstatement){
		/** select,insert, update, delete, execute **/
		
		if (sqlString == null){
			return;
		}
		
		String uCaseStatement = sqlString.toUpperCase();
		//System.out.println("ucase SQL " + uCaseStatement);
		String[] statementCheckArray = uCaseStatement.split(" ");
		
		String statementChk = statementCheckArray[0];
		System.out.println("statement check: "+ statementChk);
		
		switch (statementChk){
		case "SELECT":
			statementType = "select";
			return;
			//break;
		case "DELETE":
			statementType = "delete";
			return;
			//break;
		case "UPDATE":
			statementType = "update";
//			return;
			break;
		default:
			statementType = "select";
			return;
			//break;
	
		}
		
	}
	
	public static void loadGridIntoContainer(HashMap <Integer,String> rawGridHashMap){ // load the generated hashmap
		// pass in the resultset.
		System.out.println("total rows detected after select query execution = "+ selectGridRowCount);
		
		String gridRowWhole;
		
		int gridRow; 
		for (gridRow = 1; gridRow <= selectGridRowCount; gridRow++){
			
			gridRowWhole = rawGridHashMap.get(gridRow);
			// separate and prepare the row for loading
			HashMap<String,String>preparedRow = DataHandler.convertRecordsetRowToArrayList(gridRowWhole);
			
			Collection<String> keyset = preparedRow.keySet();
			String[] keyArray = keyset.toArray(new String[0]);
			
			System.out.println("key array for hashmap (grid row "+ gridRow + " = " + keyArray);
			
			int columnIndex;
			String columnRef;
			String columnValue;
			
			for (columnIndex = 0; columnIndex < keyArray.length-1; columnIndex++){
				columnRef = keyArray[columnIndex];
				columnValue = preparedRow.get(columnRef);
				
				if (selectGridRowCount > 1){
					// multi row handling
					columnRef = columnRef + gridRow;
				}
				//if (guiFocusChk != 3392903){
				System.out.println(columnRef + " = " + columnValue);	
				// add the key and value to the scenario info container
				ActorLibrary.scenarioInfoContainer.put(columnRef,columnValue);
			
				}
					
		} 
		
		
	}// end of function
	
	public static void SQLSourceChecker(){
		
		if (sqlString == null){
			return;
		}
		
		String uCaseStatement = sqlString.toUpperCase();
		String SQLSourceFilename;
		
		if (uCaseStatement.matches("(.*)TXT(.*)") == true){
			SQLSourceFilename = ActorLibrary.globalInfoContainer.get("DATALOAD_PATH_BACKSLASH") + sqlString;
		} else if (uCaseStatement.matches("(.*)SQL(.*)") == true){
			SQLSourceFilename = ActorLibrary.globalInfoContainer.get("DATALOAD_PATH_BACKSLASH") + sqlString;
		}else{
			return; // do nothing, exit
		}
		
		//SQLSourceFilename = SQLSourceFilename.replace("\"","/");
		System.out.println ("source file for SQL execution = " +  SQLSourceFilename);
		
		// now load the file. 
		try {
			sqlString = readSQLFile(sqlString);
			//System.out.println("SQL loaded from file:" + sqlString);
			//setStatementType(sqlString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//setStatementType(sqlString);
		
	}
	
	static String readSQLFile(String filePath) throws IOException{

		File file = new File(ActorLibrary.globalInfoContainer.get("DATALOAD_PATH_BACKSLASH"),sqlString);
		StringBuilder fileContents = new StringBuilder((int)file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");
		
		try {
			while(scanner.hasNextLine()){
				fileContents.append(scanner.nextLine() + lineSeparator);				
			}
			//System.out.println(fileContents.toString());
			return fileContents.toString();
		} finally {
			scanner.close();
		}
		
	}
	
	
}