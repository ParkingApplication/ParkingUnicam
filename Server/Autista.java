import java.util.Date;

public class Autista extends Utente {
		//carta di credito dell'autista
		private CartaDiCredito carta;
		private Date dataDiNascita;
		private String telefono;
		private double saldo;
		
		
		public Autista(int id, String username, String email, String password, String nome, String cognome, CartaDiCredito carta, Date dataDiNascita, String telefono, double saldo) {
			super(id, username, email, password, nome, cognome);
			
			if((dataDiNascita == null) || (telefono == null) || (carta == null))
				throw new IllegalArgumentException("Si sta tentando di creare un utente senza dei campi importanti");
			
			this.carta = carta;
			this.dataDiNascita = dataDiNascita;
			this.telefono = telefono;
			this.saldo = saldo;
		}

	//-----------------------------------GETTERS AND SETTERS------------------------------------------------

		public CartaDiCredito getCartaDiCredito() {
			return carta;
		}

		public double getSaldo() {
			return saldo;
		}

		public Date getDataDiNascita() {
			return dataDiNascita;
		}

		public String getTelefono() {
			return telefono;
		}
		
	//-------------------------------------------------------------------------------------------------------		
}
