
public class Posto {
	//id posto
	private int idPosto;
	//tipo del posto
	private TipoPosto tipoPosto;
	//boolean che dice se è un posto per disabili
	private boolean disabile;
	//boolean che dice se il posto è libero
	private boolean libero;
	
	public Posto(int idPosto, TipoPosto tipoPosto, boolean disabile) {
		this.idPosto = idPosto;
		this.tipoPosto = tipoPosto;
		this.disabile = disabile;
		this.libero = true;
	}	
	
//-----------------------------------------GETTERS AND SETTERS---------------------------------------------------------
	
	public int getIdPosto() {
		return idPosto;
	}

	public TipoPosto getTipoPosto() {
		return tipoPosto;
	}

	public boolean isDisabile() {
		return disabile;
	}

	public boolean isLibero() {
		return libero;
	}
	
//------------------------------------------------------------------------------------------------------------------------
	
}
