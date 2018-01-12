
public class Parcheggio {
	//nome del parcheggio
	private String nomeParcheggio;
	//indirizzo del parcheggio
	private Indirizzo indirizzoParcheggio;
	//coordinate del parcheggio
	private String[] coordinate;
	//posti del parcheggio
	private Posto[] posti;
	
	public Parcheggio(String nomeParcheggio, Indirizzo indirizzoParcheggio, String coordinataX, String coordinataY, Posto[] posti) {		
		//conto il numero di posti che prende il parcheggio cosi creo un array con il numero di posti
		int controlloLunghezza = posti.length;
		//inizializzo i due array
		this.posti = new Posto[controlloLunghezza];
		this.coordinate = new String[2];
		
		this.nomeParcheggio = nomeParcheggio;
		this.indirizzoParcheggio = indirizzoParcheggio;
		this.coordinate[0] = coordinataX;
		this.coordinate[1] = coordinataY;
		
		
		this.posti = posti;
	}

	
//---------------------------------------------------GETTERS AND SETTERS-----------------------------------------------------------------

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
	public int prenotaPosto(TipoPosto tPosto) {
		int ris = -1;
		
		for(int i = 0; i < posti.length; i++)
			if (posti[i].isLibero() && posti[i].getTipoPosto().equals(tPosto)) {
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
	
}
