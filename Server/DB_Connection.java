import java.sql.*;

//	Classe per la gestione della connessione con un database MySql
public class DB_Connection {
	private String username, password;
	//	Connessione con il database
	private Connection dbcon;
	
	//	Costruttore senza parametri
	public DB_Connection()
	{
		username = "root";
		password = "root";
	}
	
	//	Costruttore con parametri
	public DB_Connection(String user, String pass)
	{
		username = user;
		pass = password;
	}
	
	public boolean Connect()
	{
		try {
		      Class.forName("com.mysql.jdbc.Driver");	//	Carico i driver JDBC
		      String url = "jdbc:mysql://localhost:3306/";
		      
		      //	Tento la connessione
		      dbcon = DriverManager.getConnection(url, username, password);
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
	    }

		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean Disconnect()
	{
		try { 
			dbcon.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}