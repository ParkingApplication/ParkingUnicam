
public class serverParcheggio {

	public static void main(String[] args) {
		BluecoveServer btServer;
		Thread serverMainThread;
		
		try {
			btServer = new BluecoveServer("http://172.16.0.212:5044");
		} catch (Exception e) {
			System.out.println("Server bluetooth non avviabile.\nApplicazione interrotta.");
			e.printStackTrace();
			return;
		}

		serverMainThread = new Thread(btServer);
		serverMainThread.start();
	}
}
