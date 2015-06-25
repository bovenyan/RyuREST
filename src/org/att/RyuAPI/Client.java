package org.att.RyuAPI;
import java.util.EmptyStackException;
import java.util.Stack;

public class Client {
	String ipv4Addr;
	int RtpPort;
	int ClientID;
	
	public Client(String str, int port) {
		// TODO Auto-generated constructor stub
		ipv4Addr = str;
		RtpPort = port;
	}
	
	public boolean equals(Client other){
		return (ipv4Addr.equals(other.ipv4Addr) & 
				RtpPort == other.RtpPort);
	}
	
	@Override
	public int hashCode(){
		int result = 31;
		
		if (ipv4Addr != null){
			result = 37 * result + ipv4Addr.hashCode();
		}
		
		result = 37 * result + RtpPort;
		
		return result;
	}
}
