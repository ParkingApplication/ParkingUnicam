
public class Indirizzo {
	//nome della citt�
	private String citt�;
	//via
	private String via;
	//numero civico
	private int numeroCivico;
	//Provincia
	private String provincia;
	//C.A.P.
	private int cap;
	
	
	
	public Indirizzo(String citt�, String via, int numeroCivico, String provincia, int cap) {
		this.citt� = citt�;
		this.via = via;
		this.numeroCivico = numeroCivico;
		this.provincia = provincia;
		this.cap = cap;
	}
	
//----------------------------------------GETTERS AND SETTERS----------------------------------------------------------------
	

	public String getCitt�() {
		return citt�;
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
