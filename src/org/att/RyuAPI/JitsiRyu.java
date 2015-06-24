package org.att.RyuAPI;
import java.util.*;

public class JitsiRyu extends RyuREST {
	int clientClassifier = 0; 
	int protoClassifer = 1;
	
	Set<Integer> activeConferenceIDs = new HashSet<Integer>();
	Map<String, Integer> RTPConferenceBindings = new HashMap<String, Integer>();
	
	public JitsiRyu(String ryuIP) {
		super(ryuIP);
		// TODO Auto-generated constructor stub
	}
	
	/** Sets the table IDs for client classifier and protocol classifier. 
	 *  Note that for Open vSwitch and Pica8 switch this is unnecessary.
	 *  But HP switch's table starts at 100, which needs to be set up explicitly 
	 *  
	 * @param client_table Sets the table id for the client classifier
	 * @param proto_table Sets the table id for the protocol classifier
	 */
	public void setClassifierTables(int clientTable, int protoTable){
		clientClassifier = clientTable;
		protoClassifer = protoTable;
	}
	
	public void defaultChannel(){
		
	}
	
	public void addClient(String clientIpv4, int RTPport){
		// TODO: Client parsing
		
		// TODO: Add a conference if it does not exist
		
		// TODO: update conference entry
	}
	
	public void delClient(){
		// TODO: Delete Client Classifier 
		
		// TODO: Delete the conference if this is the last client
		
		// TODO: delete the client from the group table
	}
}
