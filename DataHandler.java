package taf;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

//import oracle.jdbc.pool.OracleDataSource;
//import selenium-java.

public class DataHandler {
	
	//Connection dbConnection;
	public static Connection fwDBConnection = null;
	public static Connection AUTDBConnection = null;
	public static Connection AUTSTUBConnection = null;
	public static Statement FWDBstatement = null; 
	public static CallableStatement FWDBcstmt = null;
	public static Statement AUTstatement = null; 
	public static CallableStatement AUTcstmt = null;
	public static Statement AUTSTUBstatement = null; 
	public static CallableStatement AUTSTUBcstmt = null;
	
	public static int totalRowsFound = 0;
	public static String rawHoldValue;
	public static String behaviouralMode; // input or verification....
	//private static int processRow;
	//private static String property;
	//private static String value;

//	public DataHandler (int processRow, String property, String value){
//		
//		DataHandler.processRow = 0;
//		DataHandler.property = " ";
//		DataHandler.value = " ";
//		
//	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String[] resultsGrid;
		//void ConnSuccess;
		//rs.close();	
		//resultsGrid = DataHandler.executeQuery();
		
		//Connection dbConnection = ConnectionHandler.connectionManageFWDB(); // establish connection.
		
	}
	
	public static HashMap<Integer,String> executeQuery(String table, String queryString, int mode, String appDBMode) {
		// execute query, return a HashMap object.
		/**
		MODES:
		1 = returns a singlar value (via hashmap). used where supplied queries are highly specific, containing 
		a singlar result (1 column, 1 row).
		example: select DATE from DUAL where CRITERIA = 1
		returns: single value (HashMap(1))
		2 = returns a single row (via hasmap, divided by column index) where column names are defined within the supplied statement
		example: select ONE, TWO, THREE, FOUR from DUAL where CRITERIA = 1
		returns: Hashmap(1-4)
		default = returns all rows and columns (a true results grid)
		all rows, all columns, tab delimited (along row) and ;; delimited (along column) in pairs:
		column name;;value
		... which are subsequently handled by other methods.
		**/
		
		System.out.println("query execution - requested command:" + queryString);
		
		// create the hashmap (container for the results set)
		HashMap <Integer,String> recordSetOutput = new HashMap <Integer,String>();
		
		
		//if (fwDBConnection == null){
			//fwDBConnection = null;
		//Connection fwDBConnection = null;
		
		System.out.println(appDBMode);
		
		String primaryDataQuery = queryString;
		CallableStatement cstmt = null;
		
		try { // establish the connection
		
			//Statement statement;
			
			switch (appDBMode){
			case "core":
				if (fwDBConnection == null){
					fwDBConnection = ConnectionHandler.connectionManageFWDB(fwDBConnection);
				}
				if (FWDBstatement == null){
					FWDBstatement = fwDBConnection.createStatement();
				}
				if (FWDBcstmt == null){
					FWDBcstmt = fwDBConnection.prepareCall(queryString);
				}
				break;
			case "INTERNAL":
				if (fwDBConnection == null){
					fwDBConnection = ConnectionHandler.connectionManageFWDB(fwDBConnection);
				}
				if (FWDBstatement == null){
					FWDBstatement = fwDBConnection.createStatement();
				}
				if (FWDBcstmt == null){
					FWDBcstmt = fwDBConnection.prepareCall(queryString);
				}
				break;
			case "EVL-STUB": // external.
				if (AUTSTUBConnection == null){
					AUTSTUBConnection = ConnectionHandler.connectionManageAUTDB(AUTSTUBConnection);
				}
				if (AUTSTUBstatement == null){
					AUTSTUBstatement = AUTSTUBConnection.createStatement();
				}
				if (AUTSTUBcstmt == null){
					
					AUTSTUBcstmt = AUTSTUBConnection.prepareCall(queryString);
				}
				break;
			default: // external.
				if (AUTDBConnection == null){
					AUTDBConnection = ConnectionHandler.connectionManageAUTDB(AUTDBConnection);
				}
				if (AUTstatement == null){
					AUTstatement = AUTDBConnection.createStatement();
				}
				if (AUTcstmt == null){
					
					AUTcstmt = AUTDBConnection.prepareCall(queryString);
				}
				
				 
				break;
			}
			
		
		//ResultSet rs = statement.executeQuery(primaryDataQuery);
		ResultSet rs = null;
		//cstmt = 		
		switch (appDBMode){
		case "core":
			rs = FWDBcstmt.executeQuery(primaryDataQuery);
			break;
		case "INTERNAL":
			rs = FWDBcstmt.executeQuery(primaryDataQuery);
			break;
		default:
			rs = AUTcstmt.executeQuery(primaryDataQuery);
			//AUTDBConnection.close(); // close off the connection.
			break;
		
		}
		//rs.absolute(1); // reset
		
		// get number of columns in resultset
		//int numberOfColumns = ((Object) rs).getColumnCount();
		//rs.getRow()
		//List resultSetArrayList = new ArrayList(1);
		//resultSetArrayList = 
		//var DataHandler = {processRow:"0", columnName: "dummy", gridVal: "dummy"};
				
		//ResultSet["rowCount"] = rowCount;
		ResultSetMetaData meta = rs.getMetaData();
		//columnResultSet = 
		int columnCount = meta.getColumnCount();
		//int recordsetRows = rs.
		//int recordCountFromMeta = meta.GetRows();
		System.out.println("number of columns for table " + table + "=" + columnCount);
		
		//String[] columnArray = new String [columnCount];
		//String columnName;
		int columnArrayInd;
		
		String column;
		String value = null;
		String rowConcat = "";
		//String resultArray[][];
		//resultArray = new String [][1];
		int row = 0;
		int rowCount = 0;
		int nullChk;
		String singleResult = null;
		
		switch (mode){
			case 0:// return the single result ONLY.
				if(rs.next()){
					singleResult = rs.getString(1);
				}
				System.out.println("oracle query - value returned = " + singleResult);
				recordSetOutput.put(1,singleResult);
				//rs.first();
				rs.close();
				//statement.close();
				return recordSetOutput;
				//break;
			case 2:
				
				System.out.println(rs);
				rs.next();
				String Result = rs.getString(1);
				System.out.println("oracle query - value returned = " + Result);
				
				for (columnArrayInd = 1; columnArrayInd <= columnCount; columnArrayInd++)
				{ 
					value = rs.getString(columnArrayInd);
					recordSetOutput.put(columnArrayInd,value);
				}

				rs.close();
				//statement.close();
				return recordSetOutput;	
				//break;
			case 3: // insert only.
				fwDBConnection.commit();
				rs.close();
				return recordSetOutput;
			default: // create a hashmap from the resultset
				while (rs.next()){
					
					for (columnArrayInd = 1; columnArrayInd <= columnCount; columnArrayInd++)
					{ // generate an array containing the column names
						//columnArray[columnArrayInd] = meta.getColumnName(columnArrayInd);
						//System.out.println(columnArray[columnArrayInd]);
						column = meta.getColumnName(columnArrayInd);
						value = rs.getString(column);
						// check value is not null - call function here.
						//boolean nullYess = nullCheck(value);
						
						row = rs.getRow();
						//System.out.println("row " + row + " column " + column +", value " + value);
						//resultArray[row][columnArrayInd] = value;			
						//if (nullCheck(value) != true){
							//System.out.println("null value caught " + value);
						//switch (mode){
							//case 2: // straight in without concatenation (sourced from explicit query where column order is known)
								//rowConcat = value + "	";
								//System.out.println(value);
							//default:
								rowConcat = rowConcat + "	" + column + ";;" + value;
						//}
							
						//} else {
							//rowConcat = rowConcat + "	";
						//}
					
					  }
					System.out.println(rowConcat);
					
					//nullChk = value.hashCode();
					// check that gui focus is not null (3392903)
					
					//if (!value.equals("null")){
						recordSetOutput.put(row, rowConcat);
						
					//}
					
					//recordSetOutput.put(row, rowConcat);
					
					rowConcat = null;
					
					rowCount++;
				//}
				
					
				//}
				//break;
				
				}
		}
		
		System.out.println("oracle query - total rows found = " + rowCount);
		totalRowsFound = rowCount;
		
//		if (rowCount == 0){
//			//OperationStepProcessing.stepResult = "fail";
//			
//		
//		}
		//System.out.println(Arrays.toString(resultArray));
		
		rs.close();
		//statement.close();
		//cstmt.close();
		
//		switch (appDBMode){
//		case "core":
//			//FWDBcstmt.close();
//			break;
//		case "INTERNAL":
//			//FWDBcstmt.close();
//			break;
//		default:
//			AUTcstmt.close();
//			//AUTDBConnection.close(); // close off the connection.
//			break;
//		
//		}
		
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		// clean up the recordset by removing nulls.
		
		return (recordSetOutput);
		
	}
	
	public static int countOperationsForScenario(int scenarioIndex) {
		// TODO Auto-generated method stub
		int count;
		System.out.println(scenarioIndex);
		
		
		
		
		count = 2*scenarioIndex;
		
		return count;
	}
	
	public static HashMap<String,String> convertRecordsetRowToArrayList(String HashRow){
		// receive a row from the recordset collected following query.
		HashMap<String,String> returnArrayList = new HashMap<String,String>();
		// split the HashRow
		String[] rowParams = new String[50]; 
		
		if (HashRow == null){
			return returnArrayList;
		}
		
		rowParams = HashRow.split("	"); // this will give us the column/value pairings
		
		//int commandParamCount = commandParams.length;
		int pair = 1;
		String[] command = new String[2];
		String propKey = null;
		String value = "";
		// cycle through each property / value pairing to obtain operation and operational dataset
		//System.out.println("processing operation " + operationIndex + " of " + ScInstructionCount)
		
		if (rowParams.length > 0){
		
			for (pair = 1; pair < rowParams.length; pair++){
				//System.out.println(commandParams[pair]);
				command = rowParams[pair].split(";;",2);
				
				int commandLengthCheck = command.length;
				if (commandLengthCheck > 1){
					propKey = command[0];
					value = null;
					value = command[1];
					
					boolean valueIsnull = nullCheck(value);
					if (valueIsnull != true){
						returnArrayList.put(propKey,value); // add the key & value to the returned hashmap
					}
				}
			//rowParams.Empty;
				
			}
		}
		return returnArrayList;
	}

	public static boolean nullCheck (String value){
		
//		int nullChk;
//		nullChk = 0;

//		try {
//			nullChk = value.hashCode();
//		} catch (NullPointerException e1) {
//			// TODO Auto-generated catch block
//			//e1.printStackTrace();
//			
		//System.out.println("null evaluation called against value " + value);
		
			if (value == null){
				//System.out.println("null evaluation called against value " + value + "=  true");
				return true;
			}
		//}
				
		//System.out.println("null evaluation called against value " + value + "=  FALSE");
		return false;
	}
	
	public static boolean isBlank(String str){
		
		if (str == ""){
			return true;
		}
		
		return false;
		
	}
	
	public static void triggerValueHandling(String rawValue){
		
		rawHoldValue = rawValue;
		
		// check for storage flag
		triggerStoreCheck(rawValue);
		// check for SQL
		//System.out.println("SQL check for field:" + OperationStepProcessing.autFieldAbsoluteRef);
		sqlTriggerCheck(OperationStepProcess.autFieldAbsoluteRef);
		
		//OperationStepProcessing.autFieldInputValue = rawValue;
		
		//return 
		System.out.println(OperationStepProcess.autFieldInputValue);
	}
	
	public static void triggerStoreCheck (String rawValue){
		// check for tilda '~' and if present, clean and store the value.
		if (rawValue.matches("(.*)~(.*)") == true){
			System.out.println("raw value "+ rawValue + " contains storage trigger. value will be stored.");	
			OperationStepProcess.autFieldInputValue = rawValue.replace("~","");
			ActorLibrary.scenarioInfoContainer.put(OperationStepProcess.autFieldAbsoluteRef, OperationStepProcess.autFieldInputValue); 
		}
		
		if (rawValue.matches("(.*)=(.*)") == true){ // then source the value from reference already collected 
			System.out.println("raw value "+ rawValue + " contains storage trigger. value ("+ rawValue +") will be retrieved from scenario info container.");	
			rawValue = rawValue.replace("=","");
			OperationStepProcess.autFieldInputValue = StatementPrepareAndExecute.captureStoredElement(rawValue);
			ActorLibrary.scenarioInfoContainer.put(OperationStepProcess.autFieldAbsoluteRef, OperationStepProcess.autFieldInputValue); 
		}
		
		if (rawValue.matches("(.*)SQL:(.*)") == true){
			System.out.println("raw value "+ rawValue + " contains sql statement.");	
			OperationStepProcess.autFieldInputValue = rawValue.replace("SQL:","");
			//ActorLibrary.scenarioInfoContainer.put(OperationStepProcessing.autFieldAbsoluteRef, OperationStepProcessing.autFieldInputValue); 
			//OperationStepProcessing.autFieldInputValue = statementPrepareAndExecute.captureStoredElement(rawValue);
			String simpleQuery = "select " + OperationStepProcess.autFieldInputValue + " from DUAL";
			HashMap<Integer, String> singleVal = executeQuery("dual", simpleQuery, 0, "core");
			OperationStepProcess.autFieldInputValue = singleVal.get(1);
			ActorLibrary.scenarioInfoContainer.put(OperationStepProcess.autFieldAbsoluteRef, singleVal.get(1)); 
			
		}
		
		if (rawValue.matches("(.*)calc V11(.*)") == true){
			System.out.println("raw value "+ rawValue + " contains storage trigger - calc v11.");	
			OperationStepProcess.autFieldInputValue = ExcelInterrogation.generateV11(); // generate the V11/
			ActorLibrary.scenarioInfoContainer.put(OperationStepProcess.autFieldAbsoluteRef, OperationStepProcess.autFieldInputValue); 
		}
		
		
		// else do nothing.
	}
	
	public static void sqlTriggerCheck (String fieldName){
		// check for tilda '~' and if present, clean and store the value.
		
		
		// is it a 'local' (i.e. select from DUAL) or an external statement?
		switch (fieldName){
		case "SQL_DATASET": // treat and prepare a previously stored statement (VP_SQL_STORE)
			System.out.println("raw value "+ fieldName + " contains SQL statement reference. Statement will be prepared and executed.");	
			
			if (OperationStepProcess.autFieldInputValue == null){
				OperationStepProcess.autFieldInputValue = rawHoldValue;
			}
			
			StatementPrepareAndExecute.collectStatement(OperationStepProcess.autFieldInputValue);
			
			break;
		default:
			// check what the statement is based on the supplied value i.e. is it a date statement etc
			break;
		}
		
		// else do nothing.
	}
	
	public static void executeUpdate(String queryString, String appDBMode) {
		// execute query, return a HashMap object.
		/**
		MODES:
		1 = returns a singlar value (via hashmap). used where supplied queries are highly specific, containing 
		a singlar result (1 column, 1 row).
		example: select DATE from DUAL where CRITERIA = 1
		returns: single value (HashMap(1))
		2 = returns a single row (via hasmap, divided by column index) where column names are defined within the supplied statement
		example: select ONE, TWO, THREE, FOUR from DUAL where CRITERIA = 1
		returns: Hashmap(1-4)
		default = returns all rows and columns (a true results grid)
		all rows, all columns, tab delimited (along row) and ;; delimited (along column) in pairs:
		column name;;value
		... which are subsequently handled by other methods.
		**/
		
		System.out.println("query execution - requested command:" + queryString);
		
		System.out.println(appDBMode);
		
		String primaryDataQuery = queryString;
		CallableStatement cstmt = null;
		
		try { // establish the connection
		
			//Statement statement;
			
			switch (appDBMode){
			case "core":
				fwDBConnection = ConnectionHandler.connectionManageFWDB(fwDBConnection);
				//statement = fwDBConnection.createStatement();
				cstmt = fwDBConnection.prepareCall(queryString);
				break;
			case "INTERNAL":
				fwDBConnection = ConnectionHandler.connectionManageFWDB(fwDBConnection);
				cstmt = fwDBConnection.prepareCall(queryString);
				break;
			default: // external.
				AUTDBConnection = ConnectionHandler.connectionManageAUTDB(AUTDBConnection);
				//statement = AUTDBConnection.createStatement();
				cstmt = AUTDBConnection.prepareCall(queryString);
				break;
			}
			
		
		//ResultSet rs = statement.executeQuery(primaryDataQuery);
		//ResultSet rs = null;
		//cstmt = 		
		//int rowsAffected = cstmt.execute(primaryDataQuery);
		
		cstmt.execute(primaryDataQuery);
		
		//System.out.println("rows affected by update / delete / insert = " + rowsAffected);
	
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		} // end of function
}