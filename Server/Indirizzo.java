
public class Indirizzo {
	//nome della città
	private String citta;
	//via
	private String via;
	//numero civico
	private int numeroCivico;
	//Provincia
	private String provincia;
	//C.A.P.
	private int cap;
	
	
	
	public Indirizzo(String citta, String via, int numeroCivico, String provincia, int cap) {
		this.citta = citta;
		this.via = via;
		this.numeroCivico = numeroCivico;
		this.provincia = provincia;
		this.cap = cap;
	}
	
	public String toString() {
		return via + " " + numeroCivico + " - " + cap + " " + citta + " (" + provincia + ")";
	}
	
//----------------------------------------GETTERS AND SETTERS----------------------------------------------------------------
	

	public String getCitta() {
		return citta;
	}


	public String getVia() {
		return via;
	}


	public int getNumeroCivico() {
		return numeroCivico;
	}


	public String getProvincia() {
		return provincia;
	}


	public int getCap() {
		return cap;
	}
//---------------------------------------------------------------------------------------------------------------------
	
}
