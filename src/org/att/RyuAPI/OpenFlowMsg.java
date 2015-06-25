package org.att.RyuAPI;
import java.util.*;
import org.json.simple.*;

public class OpenFlowMsg {
	String msgType;
	Map<String, Object> msg = new LinkedHashMap<String, Object>();
	Map<String, Object> match = new LinkedHashMap<String, Object>();
	LinkedList<LinkedHashMap<String, Object>> actions = 
			new LinkedList<LinkedHashMap<String, Object>>();
	ArrayList<Integer> buckets = new ArrayList<Integer>();
	
	JSONObject msgJson = new JSONObject();
	
	public OpenFlowMsg(String type){
		msgType = type;
	}
	// Msg
	public void setDpid(String value){
		msg.put("dpid", value);
	}
	
	public void setCookieMask(int cookie, long mask){
		msg.put("cookie", cookie);
		msg.put("cookie_mask", 1);
	}
	
	public void setIdleTimeout(int value){
		msg.put("idle_timeout", value);
	}
	
	public void setTableID(int value){
		msg.put("table_id", Integer.toString(value));
	}
	
	public void setGroupID(int value){
		msg.put("group_id", Integer.toString(value));
		
		if (msgType.equals("AddGroup"))
			msg.put("type", "ALL");
	}
	
	public void setPriority(int value){
		msg.put("priority", value);
	}
	
	// Match
	public void setInPort(int value){
		match.put("in_port", value);
	}
	
	public void setMetadata(Long value){
		match.put("metadata", "0x"+Long.toHexString(value));
	}
	
	public void setIpv4Src(String value){
		String [] parts = value.split("/");
	
		match.put("eth_type", 2048);
		
		if (parts.length == 1){
			match.put("ipv4_src", value);
		}
		else{
			String ipSrcMask = parts[0]+"/";
			long mask_int = 0xffffffffL;
			mask_int = mask_int & (mask_int<<(32-Integer.parseInt(parts[1])));
			ipSrcMask += longToIpv4(mask_int);
			match.put("ipv4_dst", ipSrcMask);
		}
	}
	
	public void setIpv4Dst(String value){
		String [] parts = value.split("/");
		
		match.put("eth_type", 2048);
		
		if (parts.length == 1){
			match.put("ipv4_dst", value);
		}
		else{
			String ipDstMask = parts[0]+'/';
			long mask_int = 0xffffffffL;
			mask_int = mask_int & (mask_int<<(32-Integer.parseInt(parts[1])));
			ipDstMask += longToIpv4(mask_int);
			match.put("ipv4_dst", ipDstMask);
		}
	}
	
	public void setTcpSrc(int value){
		match.put("eth_type", 2048);
		match.put("nw_proto", 6);
		match.put("tcp_src", value);
	}
	
	public void setTcpDst(int value){
		match.put("nw_proto", 6);
		match.put("tcp_dst", value);
	}
	
	public void setUdpSrc(int value){
		match.put("nw_proto", 17);
		match.put("udp_src", value);
	}
	
	public void setUdpDst(int value){
		match.put("nw_proto", 17);
		match.put("udp_dst", value);
	}
	
	// Actions
	public void actionOutputPort(int value){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "OUTPUT");
		outputAction.put("port", value);
		actions.add(outputAction);
	}
	
	public void actionOutputGroup(int value){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "OUTPUT");
		outputAction.put("group", value);
		actions.add(outputAction);
	}
	
	public void actionToController(){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "OUTPUT");
		outputAction.put("port", Integer.parseInt("0xfffd",16));
		actions.add(outputAction);
	}
	
	public void actionToL2l3(){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "OUTPUT");
		outputAction.put("port", Integer.parseInt("0xfffa",16));
		actions.add(outputAction);
	}
	
	public void actionSetIpv4Src(String srcIP){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "SET_FIELD");
		outputAction.put("field", "ipv4_src");
		outputAction.put("value", srcIP);
		actions.add(outputAction);
	}
	
	public void actionSetIpv4Dst(String dstIP){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "SET_FIELD");
		outputAction.put("field", "ipv4_dst");
		outputAction.put("value", dstIP);
		actions.add(outputAction);
	}
	
	public void actionSetTcpSrc(int srcPort){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "SET_FIELD");
		outputAction.put("field", "tcp_src");
		outputAction.put("value", srcPort);
		actions.add(outputAction);
	}
	
	public void actionSetTcpDst(int dstPort){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "SET_FIELD");
		outputAction.put("field", "tcp_dst");
		outputAction.put("value", dstPort);
		actions.add(outputAction);
	}
	
	public void actionSetUdpSrc(int srcPort){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "SET_FIELD");
		outputAction.put("field", "udp_src");
		outputAction.put("value", srcPort);
		actions.add(outputAction);
	}
	
	public void actionSetUdpDst(int dstPort){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "SET_FIELD");
		outputAction.put("field", "udp_dst");
		outputAction.put("value", dstPort);
		actions.add(outputAction);
	}
	
	public void actionWriteMetadata(long metaData){
		LinkedHashMap<String, Object> outputAction = 
				new LinkedHashMap<String, Object>();
		outputAction.put("type", "WRITE_METADATA");
		outputAction.put("metadata", "0x" + Long.toHexString(metaData));
		outputAction.put("metadata_mask", "0xffffffff");
		actions.add(outputAction);
	}
	
	// Buckets
	public void setBucketOutputs(ArrayList<Integer> outPorts){
		buckets = new ArrayList<Integer>(outPorts);
	}
	
	/**
	 * Transfer Ipv4 address from long to String in dot format
	 * @param Ipv4long Ipv4 address in long integer format 
	 * @return Ipv4 address in String format
	 */
	private String longToIpv4(long Ipv4long){
		String IpStr = "";
		final long mask = 0xff;
		
		IpStr = Long.toString(Ipv4long & mask);
		for (int i = 1; i < 4; ++i){
			Ipv4long = Ipv4long >> 8;
			IpStr = (Ipv4long & mask) + "." + IpStr;
		}
		return IpStr;
	}
	
	public String toJson(){
		msgJson = new JSONObject(msg);
		
		if (msgType.contains("Flow")){
			msgJson.put("match", match);
			
			if (msgType.equals("AddFlow")){
				msgJson.put("actions", actions);
			}
		}
		else if (msgType.contains("Group")){
			JSONObject bucket_json = new JSONObject();
			
			if (!msgType.equals("DelGroup")){
				for (int port : buckets){
					JSONObject bucket_action = new JSONObject();
					bucket_action.put("type", "OUTPUT");
					bucket_action.put("port", port);
					JSONArray actions = new JSONArray();
					actions.add(bucket_action);
					bucket_json.put("actions", actions);
				}
			}
			
			msgJson.put("buckets", bucket_json);
		}
		
		return msgJson.toString().replace("\\", "");
	}
}
