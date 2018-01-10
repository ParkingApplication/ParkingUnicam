import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

//	Thread per la gestione della connessione (via socket) con ogni singolo utente
public class ThreadUtente implements Runnable {
	//	La connessione corrente con il client
	private Socket connessione = null;
	private boolean isStopped = false;
	
	//	Stream e buffer di lettura
	//private InputStreamReader in;
	//private BufferedReader reader;
	
	public ThreadUtente(Socket sock) {
		connessione = sock;
	}
	
	@Override
	public void run() {
		String output = "";
		InputStreamReader in = null;
		BufferedReader reader = null;
		
		try
		{
			//	Flusso in ingresso da socket
			in = new InputStreamReader(connessione.getInputStream());
			reader = new BufferedReader(in);
			//reader.close(); // Richiudo
		}
		catch (IOException e) {
			System.out.println(e);
		}
		
		while (!isStopped()) {
			try {
				output = reader.readLine();
				System.out.println(output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void stop(){
        this.isStopped = true;
        
        try {
            this.connessione.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing threadUtente socket.", e);
        }
    }
	
	private synchronized boolean isStopped() {
        return this.isStopped;
    }
	
	//	##### FUNZIONI DI TEST SOCKET #####

	//	Legge da socket reinizializzando il buffer reader ad ogni esecuzione
	private String leggiDaSocket()
	{
		String output = "";
		InputStreamReader in;
		BufferedReader reader;
		
		try
		{
			//	Flusso in ingresso da socket
			in = new InputStreamReader(connessione.getInputStream());
			reader = new BufferedReader(in);
			output = reader.readLine();
			reader.close(); // Richiudo
		}
		catch (IOException e) {
			System.out.println(e);
		}
		
		return output;
	}
	
	//	Scrive su socket reinizializzando il buffer writer ad ogni esecuzione
	private void scriviSuSocket(String input)
	{
		OutputStream out;
		PrintWriter writer;
		
		try 
		{
			out = connessione.getOutputStream();
			writer = new PrintWriter(out);
			writer.println(input);
			writer.flush();
			writer.close(); // Richiudo
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
