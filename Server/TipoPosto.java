
public enum TipoPosto {
	Macchina,	// 0
	Camper,		// 1
	Moto,		// 2
	Camion,		// 3
	Autobus;	// 4
	
	//	Potrebbe servire o no, in caso non serva verrà eliminato a fine progetto.
	public static int getValue(TipoPosto tp) {
		int ris = -1;
		
		switch (tp ) {
		case Macchina:
			ris = 0;
			break;
		case Camper:
			ris = 1;
			break;
		case Moto:
			ris = 2;
		break;
		case Camion:
			ris = 3;
			break;
		case Autobus:
			ris = 4;
			break;
		}
		return ris;
	}
}


