
public class Posto {
	//tipo del posto
	private TipoPosto tipoPosto;
	//boolean che dice se è un posto per disabili
	private boolean disabile;
	//boolean che dice se il posto è libero
	private boolean libero;
	
	public Posto(TipoPosto tipoPosto, boolean disabile) {
		this.tipoPosto = tipoPosto;
		this.disabile = disabile;
		this.libero = true;
	}	
	
//-----------------------------------------GETTERS AND SETTERS---------------------------------------------------------
	

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
	
	public boolean prenota() {
		if (!libero)
			return false;
		
		libero = false;
		
		return true;
	}
	
	public boolean libera() {
		if (libero)
			return false;
		
		libero = true;
		
		return true;
	}
}
