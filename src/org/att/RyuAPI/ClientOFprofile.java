package org.att.RyuAPI;
import java.util.*;

@SuppressWarnings("serial")
public class ClientOFprofile extends HashMap<Client, Integer>{
	// A pool of client ID
	private static final int MAX_CLIENT_NO = 1000;
	
	Stack<Integer> availClientID;
	
	public ClientOFprofile(){
		for (int i = MAX_CLIENT_NO; i > 0; --i){
			availClientID.push(new Integer(i));
		}
	}
	
}
