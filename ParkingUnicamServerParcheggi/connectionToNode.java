import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class connectionToNode {
	 HttpURLConnection conn = null;
	 String url = " ";
	 
public connectionToNode(String stringaUrl) {
	this.url = stringaUrl;
}

//Funzione del server che controlla i QR code in entrata e chiede l'autenticazione al server
public String inviaQRCode(String qrCode, String path)  {	
	String result = " ";
	String urlPerConnessione = url;
	
	try {
	 URL url = new URL(urlPerConnessione + path);
     conn = (HttpURLConnection)url.openConnection();

     if ( conn != null ) {                 
         String strPostData = qrCode;
         JSONObject jsonObj = new JSONObject();
         jsonObj.put("QRCODE", strPostData);

         conn.setDoOutput(true);
         conn.setDoInput(true);
         conn.setUseCaches(false);
         conn.setRequestProperty( "Content-Type", "application/json" );
         conn.setRequestProperty("Accept", "application/json");
         conn.setRequestMethod("POST");
         
         DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
         dos.writeBytes(jsonObj.toString());
         
         int codice = conn.getResponseCode();
         
         //controlla il codice nel caso di successo
         if(codice == 200) {
        	 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             StringBuilder sb = new StringBuilder();
             String line;
             while ((line = br.readLine()) != null) {
                 sb.append(line+"\n");
             }
             br.close();
             result = sb.toString();
             result = estraiSuccessful(result);
         	}
         
         
         //controlla il codice nel caso di fallimento
         if (codice == 400) {
        	 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
             StringBuilder sb = new StringBuilder();
             String line;
             while ((line = br.readLine()) != null) {
                 sb.append(line+"\n");
             }
             br.close();
             result = sb.toString(); 
             result = estraiErrore(result);
         	}
         
         conn.disconnect();
     		}
		}
		catch(Exception e){
	}
	
	return result;
}




// Funzione per l'estrazione automatica del dato JSon error
private String estraiErrore(String data){
    String result = "";

    try {
        JSONObject response = new JSONObject(data);
        JSONObject error = response.getJSONObject("error");
        result = error.getString("info");
    } catch (Exception e) {
        result = "Impossibile leggere la risposta del server.";
    }

    return result;
}

// Funzione per l'estrazione automatica del dato JSon successful
private String estraiSuccessful(String data){
    String result = "";

    try {
        JSONObject response = new JSONObject(data);
        JSONObject successful = response.getJSONObject("successful");
        result = successful.getString("info");
    } catch (Exception e) {
        result = "Impossibile leggere la risposta del server.";
    }

    return result;
	}
}