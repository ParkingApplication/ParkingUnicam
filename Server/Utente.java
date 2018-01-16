
public class Utente {
	protected String username, password, email, nome, cognome;
	protected int id;
	
	public Utente(int id, String username, String email, String password, String nome, String cognome) {
		if((password == null) || (email == null) || (username == null) || (nome == null) || (cognome == null))
			throw new IllegalArgumentException("Si sta tentando di creare un utente senza dei campi importanti");
		
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCognome() {
		return cognome;
	}
	
	public int getId() {
		return id;
	}
}