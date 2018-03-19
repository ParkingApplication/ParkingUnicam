import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class serverParcheggio {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		connectionToNode cToNode = new connectionToNode("http://2.226.207.189");
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		String rispostaPerTest = null;
		
		while(true) {
			System.out.println("Inserire Il Numero Relativo all'azione da Compiere");
			System.out.println("1 : Entrata");
			System.out.println("2 : Uscita");
			
			
			
			try {			
				String line = buffer.readLine();
				
				System.out.println("Inserire il QRCode per Accedere al Parcheggio :");
				String lineQRCode = buffer.readLine();
				
			
				char level = line.charAt(0);
				 
				switch(level) {
		         case '1' :
		            
		            rispostaPerTest = cToNode.inviaQRCode(lineQRCode, "/parcheggio/entrataAutomobilista");
		            System.out.println("Enter the parking"); 
		            break;
		         case '2' :
		            
		            rispostaPerTest = cToNode.inviaQRCode(lineQRCode, "/parcheggio/uscitaAutomobilista");
		            System.out.println("Wait while exit");
		            break;
		         default :
		            System.out.println("Non � Stato Selezionato un Corretto Input");
		      }
				
		   }
			catch(IOException e) { 
					System.out.println(e.getMessage());
		}

		System.out.println(rispostaPerTest);
	}
	}
}
