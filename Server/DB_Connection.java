import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//	Classe per la gestione della connessione con un database MySql
public class DB_Connection {
	private String username, password, nomedb;
	//	Connessione con il database
	private Connection dbcon;
	
	//	Costruttore senza parametri
	public DB_Connection()
	{
		username = "root";
		password = "b21c4e08";	// devo toglierlo prima di caricare su git o sono stupido e lascio la password del mio db a tutti
	}
	
	//	Costruttore con parametri
	public DB_Connection(String user, String pass, String databaseName)
	{
		username = user;
		pass = password;
		nomedb = databaseName;
	}
	
	public boolean Connect()
	{
		try {
		      Class.forName("com.mysql.jdbc.Driver");	//	Carico i driver JDBC
		      String url = "jdbc:mysql://localhost:3306/" + nomedb;
		      
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
	
	//	Carica tutti i parcheggi presenti nel database e li restituisce come array
	public Parcheggio[] caricaParcheggi(){
		String query = "SELECT * FROM parcheggi;";
		Parcheggio[] parcheggi;
		
		// create the java statement
	     Statement st = null;
	     
		try {
			st = this.dbcon.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	      
	    try {
	    		ResultSet rs = st.executeQuery(query);
	    		if(rs.getRow() == 0) //	Se non ci sono parcheggi l'applicazione non ha motivo di essere in funzione
	    			return null;
			
	    		parcheggi = new Parcheggio[rs.getRow()];
	    		int[] postiPerTipo = new int[5];
	    		Indirizzo ind = null;
	    		int i = 0;
	    		
	    		
	    		while(rs.next()) {
	    			ind = new Indirizzo(rs.getString("citta"), rs.getString("via"), rs.getInt("numero_civico"), 
	    					rs.getString("provincia"), rs.getInt("cap"));
	    			
	    			postiPerTipo[TipoPosto.getValue(TipoPosto.Macchina)] = rs.getInt("nPostiMacchina");
	    			postiPerTipo[TipoPosto.getValue(TipoPosto.Camper)] = rs.getInt("nPostiCamper");
	    			postiPerTipo[TipoPosto.getValue(TipoPosto.Autobus)] = rs.getInt("nPostiAutobus");
	    			postiPerTipo[TipoPosto.getValue(TipoPosto.Moto)] = rs.getInt("nPostiMoto");
	    			postiPerTipo[TipoPosto.getValue(TipoPosto.Disabile)] = rs.getInt("nPostiDisabile");
	    			
	    			parcheggi[i] = new Parcheggio(ind, rs.getString("coordinataX"), rs.getString("coordinataY"), postiPerTipo, 
	    					rs.getDouble("tariffaOrariaLavorativi"), rs.getDouble("tariffaOrariaFestivi"));
		    	}
				
				st.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	    
	    return parcheggi;  
	}
	
	/*FUNZIONE CHE SELEZIONA L'AUTISTA IN BASE AD USERNAME E PASSWORDO
	*UNICO PROBLEMA è DA CONSIDERARE SE BISOGNA METTERE IL SALDO ALL'INTERNO DEL DATABASE PER L'UTENTE (DATO CHE SALDO NON è PRESENTE DA 
	*NESSUNA PARTE)
	*/
	public Autista selectAutista(String username, String password) {
		//controllare che username e password non siano nulli
		String query = "SELECT * FROM utenti WHERE username =\'" + username + "\' AND password =\'" + password + "\';";
		
		// create the java statement
	      Statement st = null;
		try {
			st = this.dbcon.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	      
	    // execute the query, and get a java resultset
	      try {
			ResultSet rs = st.executeQuery(query);
			if(rs.getRow() == 0) {
				return null;
			}
			
			int id = rs.getInt("idUtente");
			String email = rs.getString("email");
			String nome = rs.getString("nome");
			String cognome = rs.getString("cognome");
			Date dataDiNascita = rs.getDate("dataDiNascita");
			String telefono = rs.getString("telefono");
			float saldo = rs.getFloat("saldo");
			
			st.close();
			Autista autistaSelezionato = new Autista(username, email, password, id, " ", saldo, nome, cognome, dataDiNascita, telefono);
			return autistaSelezionato;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	      
	}
	
	
	/*
	 * FUNZIONE CHE IN BASE ALL'UTENTE TROVA LA RELATIVA CARTA DI CREDITO
	 */
	public String findNumeroCartaDiCredito(String titolareCartaDiCredito) {
				String query = "SELECT * FROM cartedicredito WHERE titolareCartaDiCredito =\'" + titolareCartaDiCredito + "\';";
				
				// create the java statement
			      Statement st = null;
				try {
					st = this.dbcon.createStatement();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			      
			    // execute the query, and get a java resultset
			      try {
					ResultSet rs = st.executeQuery(query);
					if(rs.getRow() == 0) {
						return null;
					}
					
					st.close();
					String numeroCarta = rs.getString("numeroCartaDiCredito");
					return numeroCarta;
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
	}
	
	
	/*
	 * Funzione che in base alla città in input ritorna la lista dei parcheggi presenti in quella città
	 */
	public List<Parcheggio> findParcheggi(String città){
		String query = "SELECT * FROM parcheggi WHERE città =\'" + città + "\';";
		List<Parcheggio> listaParcheggi = new ArrayList<Parcheggio>();
		// create the java statement
	      Statement st = null;
		try {
			st = this.dbcon.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	      
	    // execute the query, and get a java resultset
	      try {
			ResultSet rs = st.executeQuery(query);
			if(rs.getRow() == 0) {
				return null;
			}
			
			while(rs.next()) {
				//CHIEDERE A NICOLO' SU COME VOGLIAMO ORGANIZZARE IL DATABASE
				
			}
			st.close();
			return listaParcheggi;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	      
	}
	
	
	public List<Prenotazione> findPrenotazioneWithUsername(String username){
		String query = "SELECT * FROM utenti WHERE username =\'" + username + "\';";
		List<Prenotazione> listaPrenotazione = new ArrayList<Prenotazione>();
		// create the java statement
	      Statement st = null;
		try {
			st = this.dbcon.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	      
	    // execute the query, and get a java resultset
	      try {
			ResultSet rs = st.executeQuery(query);
			if(rs.getRow() == 0) {
				return null;
			}
			
			int idUtente = rs.getInt("idUtente");
			st.close();
			
			String query2 = "SELECT * FROM cartedicredito WHERE utente =\'" + idUtente + "\';";
			
			// create the java statement
		      Statement st1 = null;
			try {
				st1 = this.dbcon.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		      
		    // execute the query, and get a java resultset
		      try {
				ResultSet rs1 = st1.executeQuery(query2);
				if(rs.getRow() == 0) {
					return null;
				}
				
				while(rs1.next()) {
					int idParcheggio = rs1.getInt("parcheggio");
					//FINIRE E CHIEDERE A NICOLO' DATABASE INCASINATO
				}
				
				st1.close();
				return listaPrenotazione;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	      }
			
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		 }
		}


	/*
	 *FUNZIONE CHE INSERISCE UN UTENTE NEL DATABASE
	 */
	public void insertUtente(Autista aut) {
		
		int idUtente = aut.getIdAutista();
		String username = aut.getUsername();
		String password = aut.getPassword();
		String email = aut.getEmail();
		String nome = aut.getNome();
		String cognome = aut.getCognome();
		Date dataDiNascita = (Date) aut.getDataDiNascita();
		String telefono = aut.getTelefono();
		
		
		try {
		Statement st = dbcon.createStatement();

	      // note that i'm leaving "date_created" out of this insert statement
	      st.executeUpdate("INSERT INTO utenti (idUtente, username, password, email, nome, cognome, dataDiNascita, telefono) "
	          +"VALUES (\'" + idUtente + "\', '\" "+ username + "\', \' " + password + "\', \'" + email + "\', \'" + nome + "\', \'" + cognome + "\', \'" + dataDiNascita + "\', \'" + telefono + "\';) ");	     
	      dbcon.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception!");
	      System.err.println(e.getMessage());
	    }

	}
	
	/*
	 *FUNZIONE CHE INSERISCE UNA CARTA DI CREDITO NEL DATABASE
	 */
	public void insertCreditCard(int idUtente, String numeroCartaDiCredito, int pinDiVerifica, Date dataDiScadenza) {
		
		try {
		Statement st = dbcon.createStatement();

	      // note that i'm leaving "date_created" out of this insert statement
	      st.executeUpdate("INSERT INTO cartedicredito (idUtente, numeroCartaDiCredito, pinDiVerifica, dataDiScadenza) "
	          +"VALUES (\'" + idUtente + "\', '\" "+ numeroCartaDiCredito + "\', \'" + pinDiVerifica + "\', \'" + dataDiScadenza + "\';) ");	     
	      dbcon.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception!");
	      System.err.println(e.getMessage());
	    }

	}

	
	
	/*
	 *FUNZIONE CHE INSERISCE UN TIPO DI PARCHEGGIO NEL DATABASE
	 */
	public void insertTipoDiParcheggio(int idTipoParcheggio, String nome) {
		
		try {
		Statement st = dbcon.createStatement();

	      // note that i'm leaving "date_created" out of this insert statement
	      st.executeUpdate("INSERT INTO tipoparcheggio (idTipoParcheggio, nomeTipoParcheggio) "
	          +"VALUES (\'" + idTipoParcheggio + "\', '\" "+ nome + "\';) ");	     
	      dbcon.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception!");
	      System.err.println(e.getMessage());
	    }

	}

	/*
	 *FUNZIONE CHE INSERISCE UNA PRENOTAZIONE PAGATA NEL DATABASE
	 */
	/*public void insertPrenotazione(Prenotazione pren, int oraPermanenza, int idTipoParcheggio) {
		
		int idprenotazioniPagate = pren.getIdPrenotazione();
		int idUtente = pren.getIdUtente();
		int idParcheggio = pren.getIdParcheggio();
		float saldo = pren.getCostoPrenotazione();
		
		
		try {
		Statement st = dbcon.createStatement();

	      // note that i'm leaving "date_created" out of this insert statement
	      st.executeUpdate("INSERT INTO prenotazionipagate (idUtente, username, password, email, nome, cognome, dataDiNascita, telefono) "
	          +"VALUES (\'" + idprenotazioniPagate + "\', '\" "+ idUtente + "\', \' " + idParcheggio + "\', \'" + oraPermanenza + "\', \'" + saldo + "\', \'" + idTipoParcheggio + "\', \'" + false + "\';) ");	     
	      dbcon.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception!");
	      System.err.println(e.getMessage());
	    }

	}*/

	/*
	 *FUNZIONE CHE INSERISCE UN Parcheggio NEL DATABASE
	 *DA FINIRE POICHè BISOGNA CAPIRE LA SITUAZIONE DEI POSTI
	 */
	public void insertParcheggio(Autista aut) {
		
		int idUtente = aut.getIdAutista();
		String username = aut.getUsername();
		String password = aut.getPassword();
		String email = aut.getEmail();
		String nome = aut.getNome();
		String cognome = aut.getCognome();
		Date dataDiNascita = (Date) aut.getDataDiNascita();
		String telefono = aut.getTelefono();
		
		
		try {
		Statement st = dbcon.createStatement();

	      // note that i'm leaving "date_created" out of this insert statement
	      st.executeUpdate("INSERT INTO utenti (idUtente, username, password, email, nome, cognome, dataDiNascita, telefono) "
	          +"VALUES (\'" + idUtente + "\', '\" "+ username + "\', \' " + password + "\', \'" + email + "\', \'" + nome + "\', \'" + cognome + "\', \'" + dataDiNascita + "\', \'" + telefono + "\';) ");	     
	      dbcon.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception!");
	      System.err.println(e.getMessage());
	    }

	}
		
}
