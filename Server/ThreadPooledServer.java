import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//	Thread per la gestione delle richieste socket in ingresso
public class ThreadPooledServer implements Runnable {

    int serverPort = 5050;
    ServerSocket serverSocket = null;
    boolean isStopped = false;
    Thread runningThread= null;
    // Imposto quanti thread client possono essere eseguiti contemporaneamente
    ExecutorService threadPool = Executors.newFixedThreadPool(8);

    public ThreadPooledServer(int port){
        this.serverPort = port;
    }

    @Override
    public void run() {
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        
        openServerSocket();
        
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped())
                    break;
          
                throw new RuntimeException("Error accepting client connection.", e);
            }
            //	Lancio il thread client con il socket associato (poi gli andranno passati altri oggetti)
            this.threadPool.execute(new ThreadUtente(clientSocket));
        }
        
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