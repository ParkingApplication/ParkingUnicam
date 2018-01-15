import java.util.Date;

public class Prenotazione {
	
	//id dell'utente che ha fatto la prenotazione
	private int idUtente;
	//id del parcheggio su cui è stata fatta la prenotazione
	private int idParcheggio;
	//data in cui è stata fatta la prenotazione
	private Date dataPrenotazione;
	//il tipo del parcheggio a cui è riferita la prenotazione
	private TipoPosto tipoParcheggio;
	private int idPosto;
	
	private boolean pagata;	//	Indica se la prenotazione è stata pagata o è in corso
	
	//dati per le prenotazioni già pagate e salvate nel db
	private int idPrenotazione;
	private double incasso;
	private int orePermanenza;
	
	public Prenotazione(int idUtente, int idParcheggio, TipoPosto tipoParcheggio, Date dataPrenotazione, int idPosto) {
		this.idPrenotazione = -1;
		this.idUtente = idUtente;
		this.idParcheggio = idParcheggio;
		this.tipoParcheggio = tipoParcheggio;
		this.dataPrenotazione = dataPrenotazione;
		this.orePermanenza = -1;
		this.incasso = 0;
		this.idPosto = idPosto;
		this.pagata = false;
	}
	
	public Prenotazione(int idPrenotazione, int idUtente, int idParcheggio, TipoPosto tipoParcheggio, Date dataPrenotazione, int orePermanenza, double incasso) {
		this.idPrenotazione = idPrenotazione;
		this.idUtente = idUtente;
		this.idParcheggio = idParcheggio;
		this.tipoParcheggio = tipoParcheggio;
		this.dataPrenotazione = dataPrenotazione;
		this.orePermanenza = orePermanenza;
		this.incasso = incasso;
		this.idPosto = -1;
		this.pagata = true;
	}
	
//-------------------------------GETTERS AND SETTERS------------------------------------------------

	public int getIdUtente() {
		return idUtente;
	}

	public int getIdParcheggio() {
		return idParcheggio;
	}

	public Date getDataPrenotazione() {
		return dataPrenotazione;
	}

	public int getIdPrenotazione() {
		return idPrenotazione;
	}
	
	public TipoPosto getTipoParcheggio() {
		return tipoParcheggio;
	}
	
	public int getIdPosto() {
		return idPosto;
	}
	
//--------------------------------------------------------------------------------------
	
}
