
public enum TipoPosto {
	Macchina,	// 1
	Camper,		// 2
	Moto,		// 3
	Camion,		// 4
	Autobus;	// 5
	
	//	Potrebbe servire o no, in caso non serva verrà eliminato a fine progetto.
	public static int getValue(TipoPosto tp) {
		int ris = -1;
		
		switch (tp ) {
		case Macchina:
			ris = 1;
			break;
		case Camper:
			ris = 2;
			break;
		case Moto:
			ris = 3;
		break;
		case Camion:
			ris = 4;
			break;
		case Autobus:
			ris = 5;
			break;
		}
		return ris;
	}
}


