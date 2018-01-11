import java.util.Date;

public class Prenotazione {
	
	//id dell'utente che ha fatto la prenotazione
	private int idUtente;
	//id del parcheggio su cui è stata fatta la prenotazione
	private int idParcheggio;
	//data in cui è stata fatta la prenotazione
	private Date dataPrenotazione;
	//id del posto a cui è riferita la prenotazione
	private int idPosto;
	//ammontare del costo
	private float costoPrenotazione;
	
	public Prenotazione(int idUtente, int idParcheggio, int idPosto, Date dataPrenotazione, float costoPrenotazione) {
		this.idUtente = idUtente;
		this.idParcheggio = idParcheggio;
		this.idPosto = idPosto;
		this.dataPrenotazione = dataPrenotazione;
		this.costoPrenotazione = costoPrenotazione;
	}
	
	
//-------------------------------GETTERS AND SETTERS------------------------------------------------
	


	public float getCostoPrenotazione() {
		return costoPrenotazione;
	}

	public void setCostoPrenotazione(float costoPrenotazione) {
		this.costoPrenotazione = costoPrenotazione;
	}

	public int getIdUtente() {
		return idUtente;
	}

	public int getIdParcheggio() {
		return idParcheggio;
	}

	public Date getDataPrenotazione() {
		return dataPrenotazione;
	}

	public int getIdPosto() {
		return idPosto;
	}
	
}
