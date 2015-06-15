package org.att.RyuAPI;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

public class RyuREST {
	// Ryu Controller Address
	public String ryuCtrlIP;
	
	// Connections
	private HttpURLConnection addFlowConn;
	private HttpURLConnection delFlowConn;
	private HttpURLConnection addGroupConn;
	private HttpURLConnection modGroupConn;
	private HttpURLConnection delGroupConn;
	
	public RyuREST(String ryuIP) {
		ryuCtrlIP = ryuIP;
		
		try{
			init();
		}catch (IOException e){
			System.err.println(e.getMessage());
		}
	}
	
	public void init() throws IOException{
		URL urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/flowentry/add");
		addFlowConn = (HttpURLConnection) urlStr.openConnection();
		if (addFlowConn.getResponseCode() != 200){
			throw new IOException(addFlowConn.getResponseMessage());
		}
		
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/flowentry/delete");
		delFlowConn = (HttpURLConnection) urlStr.openConnection();
		if (delFlowConn.getResponseCode() != 200){
			throw new IOException(delFlowConn.getResponseMessage());
		}
		
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/groupentry/add");
		addGroupConn = (HttpURLConnection) urlStr.openConnection();
		if (addGroupConn.getResponseCode() != 200){
			throw new IOException(addGroupConn.getResponseMessage());
		}
		
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/groupentry/modify");
		modGroupConn = (HttpURLConnection) urlStr.openConnection();
		if (modGroupConn.getResponseCode() != 200){
			throw new IOException(modGroupConn.getResponseMessage());
		}
		
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/groupentry/delete");
		delGroupConn = (HttpURLConnection) urlStr.openConnection();
		if (delGroupConn.getResponseCode() != 200){
			throw new IOException(delGroupConn.getResponseMessage());
		}
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
