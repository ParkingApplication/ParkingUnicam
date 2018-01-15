import java.util.Date;

public class Autista {
		//id dell'utente	
		private int idAutista;
		//email, username e password dell'utente
		private String username;
		private String email;
		private String password;
		//carta di credito dell'autista
		private String numeroCartaDiCredito;
		//saldo dell'autista
		private float saldo;
		
		private String nome;
		private String cognome;
		private Date dataDiNascita;
		private String telefono;
		
		
		public Autista(String username, String email, String password, int idAutista, String numeroCartaDiCredito, float saldo, String nome, String cognome, Date dataDiNascita, String telefono) {
			if((password == null) || (email == null) || (username == null))
				throw new IllegalArgumentException("Si sta tentando di creare un utente senza dei campi importanti");
			
			this.username = username;
			this.email = email;
			this.password = password;
			this.idAutista = idAutista;
			
			if(numeroCartaDiCredito != null) {
				this.numeroCartaDiCredito = numeroCartaDiCredito;
			}
			
			this.saldo = saldo;
			this.nome = nome;
			this.cognome = cognome;
			this.dataDiNascita = dataDiNascita;
			this.telefono = telefono;
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

		public int getIdAutista() {
			return idAutista;
		}

		public String getNumeroCartaDiCredito() {
			return numeroCartaDiCredito;
		}

		public void setNumeroCartaDiCredito(String numeroCartaDiCredito) {
			if(numeroCartaDiCredito == null)
				throw new IllegalArgumentException("Numero carta di credito non inserita");
			this.numeroCartaDiCredito = numeroCartaDiCredito;
		}

		public float getSaldo() {
			return saldo;
		}

		public void setSaldo(float saldo) {
			if(saldo != 0) {
			this.saldo = saldo;
			}
		}

		public String getNome() {
			return nome;
		}

		public String getCognome() {
			return cognome;
		}

		public Date getDataDiNascita() {
			return dataDiNascita;
		}

		public String getTelefono() {
			return telefono;
		}
		
		
	//-------------------------------------------------------------------------------------------------------	
		
}
