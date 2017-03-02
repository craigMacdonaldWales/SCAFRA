package taf;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionHandler {
	Connection Connection;
	Connection AUTconnection;
	Connection AUTSTUBconnection;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static Connection connectionManageFWDB(Connection connection){
		System.out.println("'core' FW db connection function called");
		/// handle connection to databases. 
		// import these parameters from the RUN_CONFIG table
		String FWDBHost = Startup.FW_DB_HOST_JDBC;
		//System.out.println(FWDBHost);
		String FWDB = Startup.FW_DB;
		//System.out.println(FWDB);
		String FWDBUser = Startup.FW_DB_UID;
		//System.out.println(FWDBUser);
		String FWDBPassword = Startup.FW_DB_PWD;
		//System.out.println(FWDBPassword);
		
		
		String connectionString = "jdbc:oracle:thin:"+ FWDBHost +":1521/" + FWDB;
		
		//String connectionString = "@jdbc:oracle:thin:SYSTEM/SYSTEM@localhost:1521/XE";
		
		System.out.println(connectionString);
		
		//String connectionString = "jdbc:oracle:thin:@PACTDEV12:1521:DEVWR02";
		Properties props = new Properties();
		props.setProperty("user",FWDBUser);
		props.setProperty("password",FWDBPassword);
		
		//Driver = DriverManager.getDriver(connectionString);
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//Class.forName("oracle");
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (connection == null){
			//Connection connection = null;
			
			//boolean alreadyConnected = checkConnection(connection);
			
			//if (alreadyConnected == false){
				//Connection connection = null;// = new Connection; //= null;
				
				try {
					connection = DriverManager.getConnection(connectionString,props);
					System.out.println(connection);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // connect to the database.
			//}
		}	
		return connection;
		
	}
	
	public static Connection connectionManageAUTDB(Connection AUTconnection){
		
//		if (AUTconnection != null){
//			try { // close and re-open the connection.
//				AUTconnection.close();
//				AUTconnection = null;
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
			
		System.out.println("external db connection function called");
		/// handle connection to databases. 
		// import these parameters from the RUN_CONFIG table
		String AUTDBHost = Startup.AUT_DB_HOST_JDBC;
		//System.out.println(FWDBHost);
		String AUTDB = Startup.AUT_DB;
		//System.out.println(FWDB);
		String AUTBUser = Startup.AUT_DB_UID;
		//System.out.println(FWDBUser);
		String AUTBPassword = Startup.AUT_DB_PWD;
		//System.out.println(FWDBPassword);
		
		
		String AUTconnectionString = "jdbc:oracle:thin:"+ AUTDBHost +":1521:" + AUTDB;
		System.out.println(AUTconnectionString);
		
		//String connectionString = "jdbc:oracle:thin:@PACTDEV12:1521:DEVWR02";
		Properties props = new Properties();
		props.setProperty("user",Startup.AUT_DB_UID);
		props.setProperty("password",Startup.AUT_DB_PWD);
		
		//Driver = DriverManager.getDriver(connectionString);
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//Class.forName("oracle");
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			AUTconnection = DriverManager.getConnection(AUTconnectionString,props);
			System.out.println(AUTconnection);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // 
		
		
		if (AUTconnection == null){
			//Connection connection = null;
			
			//boolean alreadyConnected = checkConnection(connection);
			
			//if (alreadyConnected == false){
//				//Connection connection = null;// = new Connection; //= null;
//				
				try {
					AUTconnection = DriverManager.getConnection(AUTconnectionString,props);
					System.out.println(AUTconnection);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // connect to the database.
//			//}a
		}	
		System.out.println("aut db connection:" + AUTconnection);
		return AUTconnection;
		
	}

	public static Connection connectionManageAUTDBSTUB(Connection AUTSTUBconnection){
		System.out.println("external db connection function called");
		/// handle connection to databases. 
		// import these parameters from the RUN_CONFIG table
		String AUTDBHost = Startup.AUT_DB_HOST_JDBC_STUB;
		//System.out.println(FWDBHost);
		String AUTDB = Startup.AUT_DB;
		//System.out.println(FWDB);
		String AUTBUser = Startup.AUT_DB_UID;
		//System.out.println(FWDBUser);
		String AUTBPassword = Startup.AUT_DB_PWD;
		//System.out.println(FWDBPassword);
		
		
		String AUTconnectionString = "jdbc:oracle:thin:"+ AUTDBHost +":1521:" + AUTDB;
		System.out.println(AUTconnectionString);
		
		//String connectionString = "jdbc:oracle:thin:@PACTDEV12:1521:DEVWR02";
		Properties props = new Properties();
		props.setProperty("user",Startup.AUT_DB_UID);
		props.setProperty("password",Startup.AUT_DB_PWD);
		
		//Driver = DriverManager.getDriver(connectionString);
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//Class.forName("oracle");
			
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (AUTSTUBconnection == null){
			//Connection connection = null;
			
			//boolean alreadyConnected = checkConnection(connection);
			
			//if (alreadyConnected == false){
				//Connection connection = null;// = new Connection; //= null;
				
				try {
					AUTSTUBconnection = DriverManager.getConnection(AUTconnectionString,props);
					System.out.println(AUTSTUBconnection);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // connect to the database.
			//}a
		}	
		System.out.println("aut db connection:" + AUTSTUBconnection);
		return AUTSTUBconnection;
		
	}

	
	public static boolean checkConnection(Connection conn) throws Exception{
		
		if (conn == null){
			return false;
		}
		
		if (conn.isClosed()){
			return false;
		}
		
		return true;
		
	}
}