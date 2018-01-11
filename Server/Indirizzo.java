
public class Indirizzo {
	//nome della città
	private String città;
	//via
	private String via;
	//numero civico
	private int numeroCivico;
	//Provincia
	private String provincia;
	//C.A.P.
	private int cap;
	
	
	
	public Indirizzo(String città, String via, int numeroCivico, String provincia, int cap) {
		this.città = città;
		this.via = via;
		this.numeroCivico = numeroCivico;
		this.provincia = provincia;
		this.cap = cap;
	}
	
//----------------------------------------GETTERS AND SETTERS----------------------------------------------------------------
	

	public String getCittà() {
		return città;
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
