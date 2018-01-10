import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//	Thread per la gestione delle richieste socket in ingresso
public class ThreadPooledServer implements Runnable {

	private int serverPort = 5050;
	private ServerSocket serverSocket = null;
	private boolean isStopped = false;
	private Thread runningThread= null;	// Non ho ancora capito a cosa serve esattamente
	private List<ThreadUtente> lUtenti;
   
	private ExecutorService threadPool = Executors.newFixedThreadPool(8); // Imposto quanti thread client possono essere eseguiti contemporaneamente

    public ThreadPooledServer(int port){
        this.serverPort = port;
    }

    @Override
    public void run() {
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        
        openServerSocket();
        Socket clientSocket;
        ThreadUtente curThread;
        lUtenti = new ArrayList<ThreadUtente>();
        
        while (!isStopped()) {
        	curThread = null;
        	clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped())
                    break;
          
                throw new RuntimeException("Error accepting client connection.", e);
            }
            //	Lancio il thread client con il socket associato (poi gli andranno passati altri oggetti)
            curThread = new ThreadUtente(clientSocket);
            this.threadPool.execute(curThread);
            lUtenti.add(curThread);
            System.out.println("\nUna nuova connessione e' stata accettata.\n");
        }
        
        //	Fermo tutti i thread utenti in esecuzione
        for (ThreadUtente utente: lUtenti)
        	utente.stop();
        
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server.", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }
}