package taf;

import java.io.IOException;
import java.util.HashMap;

public class Startup {
	
	// locally stored (ini file) test run parameter constants:
	
	
	public static String FW_DB;
	public static String FW_DB_UID;
	public static String FW_DB_PWD;
	public static String FW_DB_HOST_JDBC;
	
	public static String AUT_DB;
	public static String AUT_DB_UID;
	public static String AUT_DB_PWD;
	public static String AUT_DB_HOST_JDBC;
	
	public static String AUT_DB_EVL_STUB;
	public static String AUT_DB_UID_EVL_STUB;
	public static String AUT_DB_PWD_EVL_STUB;
	public static String AUT_DB_HOST_JDBC_STUB;
	
	public static String BASE_URL;
	public static String verAppind;
	
	public static int RUN_MODE;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LoadIni();
	}
	
	public static HashMap<String,String> LoadIni(){
	
	HashMap<String,String> configMap = new HashMap();

	String osSystem = System.getProperty("os.name");
	String filePath;
	
	System.out.println(osSystem);
	switch (osSystem){
		case "Mac OS X":	
			filePath = "/Users/craigmacdonald/git/SCAFRA/lib/scafraini.txt";
			break;
		default:
			filePath = "C:/SCAFRA/scafraini.txt"; // need to document this
			break;
	}
	
		// load up startup parameters from INI file
	
	
	
	
	
	
	String runGroupId;
	
	try{
		ReadFile file = new ReadFile(filePath);
		String[] fileOut = file.OpenFile();
		
		int fileLineCount = fileOut.length;
		int line;
		String configLine;
		
		String[] configItemSplit;
		for (line = 0; line < fileLineCount; line++){
			//System.out.println(fileOut[line]);
			configLine = fileOut[line];
			
			boolean ignoreFlag = configLine.contains("--");
			
			//System.out.println(ignoreFlag);
			if (ignoreFlag != true && configLine.contains("=")){// it's not an ignore line
				configItemSplit = configLine.split("=");
				System.out.println(configItemSplit[0] + " = " + configItemSplit[1]);
				//System.out.println();
				configMap.put(configItemSplit[0], configItemSplit[1]);
			}
			
		}
		
		//System.out.println(fileOut);
	}
	catch (IOException e){
		System.out.println(e.getMessage());
	}
	
	
	runGroupId = configMap.get("RUN_GROUP_ID");
	System.out.println(configMap.get("RUN_GROUP_ID"));
	
	FW_DB = configMap.get("FW_DB");
	FW_DB_UID = configMap.get("FW_DB_UID");
	FW_DB_PWD = configMap.get("FW_DB_PWD");
	FW_DB_HOST_JDBC = configMap.get("FW_DB_HOST_JDBC");
	
			
	// now extract from the database	
		
	String queryString = "select * from run_config where run_group_id = '" + runGroupId +"'";
	HashMap<Integer,String> runConfigMatrix = DataHandler.executeQuery("",queryString,1,"core");
	
	int paramCount = runConfigMatrix.size();
	System.out.println("total number of run_config parameters collected for run group ID " + paramCount);
	
	int paramRow;
	String paramRaw;
	String param;
	String paramValue;
	for(paramRow = 1; paramRow <= paramCount; paramRow++){
		paramRaw = runConfigMatrix.get(paramRow);
		System.out.println(paramRaw);
		
		HashMap<String,String> paramSplit = DataHandler.convertRecordsetRowToArrayList(paramRaw);
		param = paramSplit.get("PARAMETER_NAME");
		paramValue = paramSplit.get("VALUE");
		
		switch (param){
		
		case "AUT_DB":
			configMap.put(param,paramValue);
			AUT_DB = configMap.get("AUT_DB");
			break;
		case "AUT_DB_UID":
			configMap.put(param,paramValue);
			AUT_DB_UID = configMap.get("AUT_DB_UID");
			break;
		case "AUT_DB_PWD":
			configMap.put(param,paramValue);
			AUT_DB_PWD = configMap.get("AUT_DB_PWD");
		case "AUT_DB_HOST_JDBC":
			configMap.put(param,paramValue);
			AUT_DB_HOST_JDBC = configMap.get("AUT_DB_HOST_JDBC");
			break;
		case "AUT_DB_EVL-STUB":
			configMap.put(param,paramValue);
			AUT_DB_EVL_STUB = configMap.get("AUT_DB_EVL-STUB");
			break;
		case "AUT_DB_UID_EVL-STUB":
			configMap.put(param,paramValue);
			AUT_DB_UID_EVL_STUB = configMap.get("AUT_DB_UID_EVL-STUB");
			break;
		case "AUT_DB_PWD_EVL-STUB":
			configMap.put(param,paramValue);
			AUT_DB_PWD_EVL_STUB = configMap.get("AUT_DB_PWD_EVL-STUB");
		case "AUT_DB_HOST_EVL_JDBC-STUB":
			configMap.put(param,paramValue);
			AUT_DB_HOST_JDBC_STUB = configMap.get("AUT_DB_HOST_EVL_JDBC-STUB");
			break;
		case "RUN_MODE":
			configMap.put(param,paramValue);
			RUN_MODE = Integer.valueOf(configMap.get("RUN_MODE"));
			break;
		case "version_under_test":	
			//String 
			verAppind = configMap.get("version_under_test");
			String GUIqueryString = "select VERSION_APPEND_INDEX from VERSION_LOOKUP where VERSION_NAME = '" + verAppind +"'";
			HashMap<Integer,String> GUIreturn = DataHandler.executeQuery("",GUIqueryString,0,"core");
			configMap.put("VERSION_APPEND_INDEX",GUIreturn.get(1));
			
			break;
		case "BASE_URL":
			configMap.put(param,paramValue);
			BASE_URL = configMap.get("BASE_URL");
			break;
		case "":
			break;
		case "null":
			break;
		default:
			configMap.put(param,paramValue);
			break;
		}
			
		//configMap.put(param,paramValue);
	}
	
	return (configMap); // done
	
	
	}
}