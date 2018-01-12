
public class Parcheggio {
	//id del parcheggio
	private int idParcheggio;
	//nome del parcheggio
	private String nomeParcheggio;
	//indirizzo del parcheggio
	private Indirizzo indirizzoParcheggio;
	//coordinate del parcheggio
	private String[] coordinate;
	//posti del parcheggio
	private Posto[] posti;
	
	public Parcheggio(int idParcheggio, String nomeParcheggio, Indirizzo indirizzoParcheggio, String coordinataX, String coordinataY, Posto[] posti) {		
		//conto il numero di posti che prende il parcheggio cosi creo un array con il numero di posti
		int controlloLunghezza = posti.length;
		//inizializzo i due array
		this.posti = new Posto[controlloLunghezza];
		this.coordinate = new String[2];
		
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

	public String[] getCoordinate() {
		return coordinate;
	}

	//	Non sono sicuro che serva realmente a qualcosa
	public Posto[] getPosti() {
		return posti;
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------
	
	//	Prenota il posto del tipo specificato e ritorna la posizione del posto prenotato o -1 se non ci sono posti disponibili di quel tipo
	public int prenotaPostoNormale(TipoPosto tPosto) {
		int ris = -1;
		
		for(int i = 0; i < posti.length; i++)
			if (posti[i].isLibero() && posti[i].getTipoPosto().equals(tPosto)) {
				posti[i].prenota();
				ris = i;
				break;
			}
		
		return ris;
	}
	
	//	Prenota un posto per disabili e ritorna la posizione del posto indicato oppure -1 se non ci sono posti liberi
	public int prenotaPostoDisabili() {
		int ris = -1;
		
		for(int i = 0; i < posti.length; i++)
			if (posti[i].isLibero() && posti[i].isDisabile()) {
				posti[i].prenota();
				ris = i;
				break;
			}
		
		return ris;
	}
	
	//	Libera il posto indicato e ritorna true se era occupato, else se era già libero
	public boolean liberaPosto(int nPosto) {
		return posti[nPosto].libera();
	}
	
	//	Ritorna il numero totale di posti per la tipologia di posto selezionata
	public int countPostiPerTipo(TipoPosto tPosto) {
		int count = 0;
		
		for(int i = 0; i < posti.length; i++)
			if (posti[i].getTipoPosto().equals(tPosto))
				count++;
		
		return count;
	}
	
	//	Ritorna il numero totale di posti per disabili
	public int countPostiDisabili() {
		int count = 0;
		
		for(int i = 0; i < posti.length; i++)
			if (posti[i].isDisabile())
				count++;
		
		return count;
	}
}
