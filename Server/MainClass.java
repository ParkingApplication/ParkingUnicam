import java.util.*;

public class MainClass {

	public static void main(String[] args) {
		Date data = new Date();
		ThreadPooledServer server = new ThreadPooledServer(5050, data); // In seguito potrebbero servire più porte
		Scanner in = new Scanner(System.in);
		
		
		
		//	Avvio il server
		new Thread(server).start();
		System.out.println("Server avviato.\n");
		
		//	Menu' server
		while (true) {
			System.out.println("\t1 - Arresta il server");
			System.out.println("\t2 - Visualizza status server");
			System.out.println("\t3 - Possibili altre opzioni");
			
			String input = in.nextLine();
			
			if (input.compareTo("1") == 0)
			{
				server.stop();
				break;
			}
			else
				if (input.compareTo("2") == 0) {
					//TO DO
				} 
				else
					if (input.compareTo("3") == 0) {
						//TO DO
					}
		}
		
		in.close();
	}
}
