package org.att.RyuAPI;
import java.util.*;
import java.io.*;

public class JitsiRyu extends RyuREST {
	int clientClassifier = 0; 
	int protoClassifer = 1;
	
	Set<Integer> activeConferenceIDs = new HashSet<Integer>();
	
	Map<ClientOFprofile, Integer> ClientConfBindings = 
			new HashMap<ClientOFprofile, Integer>();
	
	RyuREST ryuAPI;
	
	public JitsiRyu(String ryuIP) {
		super(ryuIP);
		// TODO Auto-generated constructor stub
		try {
			ryuAPI.init();
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		
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
	
	public void defaultChannel(String XMPPIpv4, int XMPPport){
		// All non-matching traffic goes to L2/L3 mode
		OpenFlowMsg defaultRule = new OpenFlowMsg("AddFlow");
		defaultRule.setPriority(0);
		defaultRule.setTableID(clientClassifier);
		defaultRule.actionToL2l3();
		
		ryuAPI.addFlowEntry(defaultRule);
		
		// New XMPP traffic copy to controller
		OpenFlowMsg newXMPPRule = new OpenFlowMsg("AddFlow");
		newXMPPRule.setPriority(1);
		newXMPPRule.setTableID(clientClassifier);
		newXMPPRule.setIpv4Dst(XMPPIpv4);
		newXMPPRule.setTcpDst(XMPPport);
		newXMPPRule.actionOutputGroup(0);  // TODO: Check whether group 0 is available
		
		OpenFlowMsg newXMPPcopyGroup = new OpenFlowMsg("AddGroup");
		
		ArrayList<Integer> XMPPcopyGroupOut = new ArrayList<Integer>();
		XMPPcopyGroupOut.add(Integer.parseInt("fffd")); // to controller
		XMPPcopyGroupOut.add(Integer.parseInt("fffa")); // to l2l3
		
		newXMPPcopyGroup.setGroupID(0);
		newXMPPcopyGroup.setBucketOutputs(XMPPcopyGroupOut);
		
		ryuAPI.addFlowEntry(newXMPPRule);
		
		// Client XMPP goes to normal L2L3
		OpenFlowMsg clientXMPPRule = new OpenFlowMsg("AddFlow");
		clientXMPPRule.setPriority(0);
		clientXMPPRule.setTableID(protoClassifer);
		clientXMPPRule.actionToL2l3();
		
		ryuAPI.addFlowEntry(clientXMPPRule);
	}
	
	public void addClient(String clientIpv4, int RTPport, String ConfName){
		// TODO: Client parsing
		
		// TODO: Add a conference if it does not exist
		
		// TODO: update conference entry
	}
	
	public void delClient(String clientIpv4, int RTPport){
		// TODO: Delete Client Classifier 
		
		// TODO: Delete the conference if this is the last client
		
		// TODO: delete the client from the group table
	}
}
