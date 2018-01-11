
public class Utente {
	
	//id dell'utente	
	private int idUtente;
	//email, username e password dell'utente
	private String username;
	private String email;
	private String password;
	
	public Utente(String username, String email, String password, int idUtente) {
		if((password == null) || (email == null) || (username == null))
			throw new IllegalArgumentException("Si sta tentando di creare un utente senza dei campi importanti");
		
		this.username = username;
		this.email = email;
		this.password = password;
		this.idUtente = idUtente;
	}

//-----------------------------------GETTERS AND SETTERS------------------------------------------------
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if(username == null)
			throw new IllegalArgumentException("Non è stato inserito alcun username");
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email == null)
			throw new IllegalArgumentException("Non è stato inserito alcun username");
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if(password == null)
			throw new IllegalArgumentException("Non è stato inserito alcun username");
		this.password = password;
	}

	public int getIdUtente() {
		return idUtente;
	}
	
//-------------------------------------------------------------------------------------------------------	
	
}