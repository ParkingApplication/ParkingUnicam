
public class GestoreParcheggi {
	private Parcheggio[] parcheggi;
	private int[][] postiLiberi; //	Matrice dei posti liberi di ogni parcheggio divisi per tipologia

	public GestoreParcheggi(Parcheggio[] parcheggi) {
		this.parcheggi = parcheggi;
		postiLiberi = new int[parcheggi.length][6];
		
		for (int i = 0; i < parcheggi.length; i++)
		{
			postiLiberi[i][TipoPosto.getValue(TipoPosto.Macchina)] = parcheggi[i].countPostiPerTipo(TipoPosto.Macchina);
			postiLiberi[i][TipoPosto.getValue(TipoPosto.Autobus)] = parcheggi[i].countPostiPerTipo(TipoPosto.Autobus);
			postiLiberi[i][TipoPosto.getValue(TipoPosto.Camion)] = parcheggi[i].countPostiPerTipo(TipoPosto.Camion);
			postiLiberi[i][TipoPosto.getValue(TipoPosto.Camper)] = parcheggi[i].countPostiPerTipo(TipoPosto.Camper);
			postiLiberi[i][TipoPosto.getValue(TipoPosto.Moto)] = parcheggi[i].countPostiPerTipo(TipoPosto.Moto);
			postiLiberi[i][5] = parcheggi[i].countPostiDisabili();	//	Posti per disabili
		}
	}
}
