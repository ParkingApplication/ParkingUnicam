public class GestoreParcheggi {
	private Parcheggio[] parcheggi;

	public GestoreParcheggi(Parcheggio[] parcheggi) {
		this.parcheggi = parcheggi;
	}
	
	public int prenotaPosto(int idParcheggio, TipoPosto tPosto) {
		if (idParcheggio < 0 || idParcheggio >= parcheggi.length)
			return -1;
		
		return parcheggi[idParcheggio].prenotaPosto(tPosto);
	}
	
	public boolean liberaPosto(int idParcheggio, TipoPosto tPosto, int nPosto) {
		if (idParcheggio < 0 || idParcheggio >= parcheggi.length)
			return false;
		
		return parcheggi[idParcheggio].liberaPosto(tPosto, nPosto);
	}
	
	public Parcheggio getParcheggio(int index) {
		return parcheggi[index];
	}
}
