import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//	Thread per la gestione della connessione (via socket) con ogni singolo utente
public class ThreadUtente implements Runnable {
	//	La connessione corrente con il client
	private Socket connessione = null;
	private boolean isStopped = false;
	private Autista curUser = null;	//	Verrà caricato appena ricevuti e convalidati i dati di login
	private List<Prenotazione> prenotazioni; //	Lista delle prenotazioni in corso
	private GestoreParcheggi gParcheggi;
	
	public ThreadUtente(Socket sock, GestoreParcheggi gParcheggi) {
		connessione = sock;
		prenotazioni = new ArrayList<Prenotazione>();
		this.gParcheggi = gParcheggi;
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
	
	private boolean effettuaPrenotazione(int idParcheggio, TipoPosto tPosto) {
		int pos = prenotaPostoParcheggio(idParcheggio, tPosto);
		
		if (pos >= 0) {
			prenotazioni.add(new Prenotazione(curUser.getId(), idParcheggio, tPosto, new Date(), pos));
			return true;
		}
		
		return false;
	}
	
	//	Effettua la prenotazione sul gestore dei parcheggi in modo sincrono (compete con altri processi utente per gli stessi parcheggi)
	private synchronized int prenotaPostoParcheggio(int idParcheggio, TipoPosto tPosto) {
        return gParcheggi.prenotaPosto(idParcheggio, tPosto);
    }
	
	public boolean annullaPrenotaizone(int nPrenotazione) {
		if (nPrenotazione < 0 || nPrenotazione >= prenotazioni.size())
			return false;
		
		Prenotazione p = prenotazioni.remove(nPrenotazione);
		
		return liberaPostoParcheggio(p.getIdParcheggio(), p.getTipoParcheggio(), p.getIdPosto());
	}
	
	private synchronized boolean liberaPostoParcheggio(int idParcheggio, TipoPosto tPosto, int nPosto) {
        return gParcheggi.liberaPosto(idParcheggio, tPosto, nPosto);
    }
	
	//	Chiude la connessione ed arresta il thread
	public synchronized void stop(){
        this.isStopped = true;
        
        try {
            this.connessione.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing threadUtente socket.", e);
        }
    }
	
	public synchronized boolean isStopped() {
        return this.isStopped;
    }
	
	//	Identifica l'identità dell' utente e stabilisce gli algoritmi di sicurezza e le chiavi correlate per la comunicazione criptata
	public boolean handshake() {
		
		return true;
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
