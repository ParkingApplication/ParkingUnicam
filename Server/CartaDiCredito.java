import java.util.Date;

public class CartaDiCredito {
	private String numeroCartaDiCredito, pin;
	private Date dataDiScadenza;
	
	public CartaDiCredito(String numeroCarta, String pin, Date dataScadenza) {
		if((numeroCarta == null) || (pin == null) || (dataScadenza == null))
			throw new IllegalArgumentException("Si sta tentando di creare una carta di credito senza dei campi fondamentali per essa.");
		
		numeroCartaDiCredito = numeroCarta;
		this.pin = pin;
		dataDiScadenza = dataScadenza;
	}
	
	public String getnumeroCartaDiCredito() {
		return numeroCartaDiCredito;
	}
	
	public String getPin() {
		return pin;
	}
	
	public Date getDataDiScadenza() {
		return dataDiScadenza;
	}
}
