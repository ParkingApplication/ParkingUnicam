
public class Parcheggio {

	//id del parcheggio
	private int idParcheggio;
	//nome del parcheggio
	private String nomeParcheggio;
	//indirizzo del parcheggio
	private Indirizzo indirizzoParcheggio;
	//coordinate del parcheggio
	private int[] coordinate;
	//posti del parcheggio
	private Posto[] posti;
	
	public Parcheggio(int idParcheggio, String nomeParcheggio, Indirizzo indirizzoParcheggio, int coordinataX, int coordinataY, Posto[] posti) {		
		//conto il numero di posti che prende il parcheggio cosi creo un array con il numero di posti
		int controlloLunghezza = posti.length;
		//inizializzo i due array
		this.posti = new Posto[controlloLunghezza];
		this.coordinate = new int [2];
		
		
		this.idParcheggio = idParcheggio;
		this.nomeParcheggio = nomeParcheggio;
		this.indirizzoParcheggio = indirizzoParcheggio;
		this.coordinate[0] = coordinataX;
		this.coordinate[1] = coordinataY;
		
		
		this.posti = posti;
	}

	
	
	
//---------------------------------------------------GETTERS AND SETTERS-----------------------------------------------------------------
	
	public int getIdParcheggio() {
		return idParcheggio;
	}

	public String getNomeParcheggio() {
		return nomeParcheggio;
	}

	public Indirizzo getIndirizzoParcheggio() {
		return indirizzoParcheggio;
	}

	public int[] getCoordinate() {
		return coordinate;
	}

	public Posto[] getPosti() {
		return posti;
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------
	
	public boolean prenotaPosto(int numeroPosto) {
		return true;
	}
}
