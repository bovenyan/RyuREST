package org.att.RyuAPI;
import java.io.*;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

public class RyuREST {
	// Ryu Controller Address
	public String ryuCtrlIP;
	//private int [] dpidList;
	private int dpid;
	
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
		// Fetch the switch dpid
		URL urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/switches");
	
		HttpURLConnection switchIDConn = makeConn(urlStr, false);
		
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(switchIDConn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine())!= null)
			sb.append(line);
		rd.close();
		String [] dpid_str = (sb.toString().replaceAll("\\[\\]","")).split(",");
		if (dpid_str.length>0){
			dpid = Integer.parseInt(dpid_str[0]);
		}
		else{
			throw new IOException("No valid switch");
		}
		switchIDConn.disconnect();
		
		// Establish the HTTP connections
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/flowentry/add");
		addFlowConn = makeConn(urlStr, true);
		
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/flowentry/delete");
		delFlowConn = makeConn(urlStr, true);
		
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/groupentry/add");
		addGroupConn = makeConn(urlStr, true);
		
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/groupentry/modify");
		modGroupConn = makeConn(urlStr, true);
		
		urlStr = new URL("http://"+ryuCtrlIP+":8080/stats/groupentry/delete");
		delGroupConn = makeConn(urlStr, true);
	}
	
	private HttpURLConnection makeConn(URL urlStr, boolean isPost) throws IOException{
		HttpURLConnection conn = (HttpURLConnection) urlStr.openConnection();
		
		if (conn.getResponseCode() != 200){
			throw new IOException(conn.getResponseMessage());
		}
		
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		
		if (isPost){
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(false);
		}
		else{
			conn.setRequestMethod("GET");
			conn.setDoOutput(false);
			conn.setDoInput(true);
		}
		
		return conn;
	}
	
	public void close(){
		addFlowConn.disconnect();
		delFlowConn.disconnect();
		addGroupConn.disconnect();
		modGroupConn.disconnect();
		delGroupConn.disconnect();
	}
}
