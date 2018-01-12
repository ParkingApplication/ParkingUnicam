
public enum TipoPosto {
	Macchina,	// 0
	Camper,		// 1
	Moto,		// 2
	Autobus,	// 3
	Disabile;	// 4

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
		case Autobus:
			ris = 4;
			break;
		case Disabile:
			ris = 4;
			break;
	}
		return ris;
	}
}


