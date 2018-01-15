
public class Parcheggio {
	//indirizzo del parcheggio
	private Indirizzo indirizzoParcheggio;
	//coordinate del parcheggio
	private String[] coordinate;
	//posti del parcheggio
	private boolean[][] posti;	//	Righe = TipoPosto Colonne = IdPosto / True = occupato False = libero
	//tariffe orarie
	private double tariffaLavorativi, tariffaFestivi;	//	Non sono sicuro sul DOUBLE andrebbe trovata un alternativa per i costi
	
	public Parcheggio(Indirizzo indirizzoParcheggio, String coordinataX, String coordinataY, int[] postiPerTipo, double tariffaOrariaLavorativi, double tariffaOrariaFestivi) {	
		this.coordinate = new String[2];
		posti = new boolean[5][];	//	5 righe, una per tipo di parcheggio esistente
		
		for (int i = 0; i < 5; i++)
			posti[i] = new boolean[postiPerTipo[i]];
		
		this.indirizzoParcheggio = indirizzoParcheggio;
		this.coordinate[0] = coordinataX;
		this.coordinate[1] = coordinataY;
		tariffaLavorativi = tariffaOrariaLavorativi;
		tariffaFestivi = tariffaOrariaFestivi;
	}
	
//---------------------------------------------------GETTERS AND SETTERS-----------------------------------------------------------------


	public Indirizzo getIndirizzoParcheggio() {
		return indirizzoParcheggio;
	}

	public String[] getCoordinate() {
		return coordinate;
	}
	
	public double getTariffaLavorativi() {
			return tariffaLavorativi;
	}
	
	public double getTariffaFestivi() {
		return tariffaFestivi;
	}
	
//---------------------------------------------------------------------------------------------------------------------------------------
	
	//	Prenota il posto del tipo specificato e ritorna la posizione del posto prenotato o -1 se non ci sono posti disponibili di quel tipo
	public int prenotaPosto(TipoPosto tPosto) {
		int ris = -1;
		int tipo = TipoPosto.getValue(tPosto);
		
		for(int i = 0; i < posti[tipo].length; i++)
			if (!posti[tipo][i]) {
				posti[tipo][i] = true;
				ris = i;
				break;
			}
		
		return ris;
	}

	//	Libera il posto indicato e ritorna true se era occupato, else se era già libero
	public boolean liberaPosto(TipoPosto tPosto, int nPosto) {
		if (nPosto < 0 || nPosto > posti[TipoPosto.getValue(tPosto)].length)
			return false;
		
		return posti[TipoPosto.getValue(tPosto)][nPosto] = false;
	}
}
