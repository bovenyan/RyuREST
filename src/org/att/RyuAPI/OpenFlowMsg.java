package org.att.RyuAPI;
import java.util.*;
import org.json.simple.*;

public class OpenFlowMsg {
	String msg_type;
	Map<String, Object> msg = new LinkedHashMap<String, Object>();
	Map<String, Object> match = new LinkedHashMap<String, Object>();
	LinkedList<LinkedHashMap<String, Object>> actions = 
			new LinkedList<LinkedHashMap<String, Object>>();
	ArrayList<Integer> buckets = new ArrayList<Integer>();
	
	JSONObject msg_json = new JSONObject();
	
	public OpenFlowMsg(String type){
		msg_type = type;
	}
	// Msg
	public void set_dpid(String value){
		msg.put("dpid", value);
	}
	
	public void set_cookie_mask(int cookie, long mask){
		msg.put("cookie", cookie);
		msg.put("cookie_mask", 1);
	}
	
	public void set_table_id(int value){
		msg.put("table_id", Integer.toString(value));
	}
	
	public void set_group_id(int value){
		msg.put("group_id", Integer.toString(value));
		
		if (msg_type.equals("AddGroup"))
			msg.put("type", "ALL");
	}
	
	public void set_priority(int value){
		msg.put("priority", value);
	}
	
	// Match
	public void set_in_port(int value){
		match.put("in_port", value);
	}
	
	public void set_metadata(Long value){
		match.put("metadata", "0x"+Long.toHexString(value));
	}
	
	public void set_ipv4_src(String value){
		String [] parts = value.split("/");
	
		match.put("eth_type", 2048);
		
		if (parts.length == 1){
			match.put("ipv4_src", value);
		}
		else{
			String ip_src_mask = parts[0]+"/";
			long mask_int = 0xffffffffL;
			mask_int = mask_int & (mask_int<<(32-Integer.parseInt(parts[1])));
			ip_src_mask += longToIpv4(mask_int);
			match.put("ipv4_dst", ip_src_mask);
		}
	}
	
	public void set_ipv4_dst(String value){
		String [] parts = value.split("/");
		
		match.put("eth_type", 2048);
		
		if (parts.length == 1){
			match.put("ipv4_dst", value);
		}
		else{
			String ip_dst_mask = parts[0]+'/';
			long mask_int = 0xffffffffL;
			mask_int = mask_int & (mask_int<<(32-Integer.parseInt(parts[1])));
			ip_dst_mask += longToIpv4(mask_int);
			match.put("ipv4_dst", ip_dst_mask);
		}
	}
	
	public void set_tcp_src(int value){
		match.put("eth_type", 2048);
		match.put("nw_proto", 6);
		match.put("tcp_src", value);
	}
	
	public void set_tcp_dst(int value){
		match.put("nw_proto", 6);
		match.put("tcp_dst", value);
	}
	
	public void set_udp_src(int value){
		match.put("nw_proto", 17);
		match.put("udp_src", value);
	}
	
	public void set_udp_dst(int value){
		match.put("nw_proto", 17);
		match.put("udp_dst", value);
	}
	
	// Actions
	public void action_output_port(int value){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "OUTPUT");
		output_action.put("port", value);
		actions.add(output_action);
	}
	
	public void action_output_group(int value){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "OUTPUT");
		output_action.put("group", value);
		actions.add(output_action);
	}
	
	public void action_to_controller(){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "OUTPUT");
		output_action.put("port", Long.parseLong("0xfffd",16));
		actions.add(output_action);
	}
	
	public void action_to_l2l3(){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "OUTPUT");
		output_action.put("port", Long.parseLong("0xfffa",16));
		actions.add(output_action);
	}
	
	public void action_set_ipv4_src(String srcIP){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "ipv4_src");
		output_action.put("value", srcIP);
		actions.add(output_action);
	}
	
	public void action_set_ipv4_dst(String dstIP){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "ipv4_dst");
		output_action.put("value", dstIP);
		actions.add(output_action);
	}
	
	public void action_set_tcp_src(int srcPort){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "tcp_src");
		output_action.put("value", srcPort);
		actions.add(output_action);
	}
	
	public void action_set_tcp_dst(int dstPort){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "tcp_dst");
		output_action.put("value", dstPort);
		actions.add(output_action);
	}
	
	public void action_set_udp_src(int srcPort){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "udp_src");
		output_action.put("value", srcPort);
		actions.add(output_action);
	}
	
	public void action_set_udp_dst(int dstPort){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "udp_dst");
		output_action.put("value", dstPort);
		actions.add(output_action);
	}
	
	public void action_write_metadata(long metaData){
		LinkedHashMap<String, Object> output_action = 
				new LinkedHashMap<String, Object>();
		output_action.put("type", "WRITE_METADATA");
		output_action.put("metadata", "0x" + Long.toHexString(metaData));
		output_action.put("metadata_mask", "0xffffffff");
		actions.add(output_action);
	}
	
	// Buckets
	public void bucket_outputs(ArrayList<Integer> out_ports){
		buckets = new ArrayList<Integer>(out_ports);
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
		msg_json = new JSONObject(msg);
		
		if (msg_type.contains("Flow")){
			msg_json.put("match", match);
			
			if (msg_type.equals("AddFlow")){
				msg_json.put("actions", actions);
			}
		}
		else if (msg_type.contains("Group")){
			JSONObject bucket_json = new JSONObject();
			
			if (!msg_type.equals("DelGroup")){
				for (int port : buckets){
					JSONObject bucket_action = new JSONObject();
					bucket_action.put("type", "OUTPUT");
					bucket_action.put("port", port);
					JSONArray actions = new JSONArray();
					actions.add(bucket_action);
					bucket_json.put("actions", actions);
				}
			}
			
			msg_json.put("buckets", bucket_json);
		}
		
		return msg_json.toString().replace("\\", "");
	}
}
