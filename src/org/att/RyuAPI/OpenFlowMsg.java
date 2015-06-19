package org.att.RyuAPI;
import java.util.*;
import org.json.simple.*;

public class OpenFlowMsg {
	String msg_type;
	Map<String, String> msg = new LinkedHashMap<String, String>();
	Map<String, String> match = new LinkedHashMap<String, String>();
	LinkedList<LinkedHashMap<String, String>> actions = 
			new LinkedList<LinkedHashMap<String, String>>();
	ArrayList<Integer> buckets = new ArrayList<Integer>();
	
	JSONObject msg_json = new JSONObject();
	
	public OpenFlowMsg(String type){
		msg_type = type;
	}
	// Msg
	public void set_dpid(int value){
		msg.put("dpid", Integer.toString(value));
	}
	
	public void set_in_port(int value){
		msg.put("in_port", Integer.toString(value));
	}
	
	public void set_cookie_mask(int cookie, long mask){
		msg.put("cookie", Integer.toString(cookie));
		msg.put("cookie", Long.toString(cookie));
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
		msg.put("priority", Integer.toString(value));
	}
	
	// Match
	public void set_ipv4_src(String value){
		String [] parts = value.split("/");
		
		match.put("eth_type", "2048");
		
		if (parts.length == 1){
			match.put("ip_src", value);
		}
		else{
			String ip_src_mask = parts[0]+"/";
			long mask_int = 0xffffffffL;
			ip_src_mask += longToIpv4(mask_int << (32-Integer.parseInt(parts[1])));
			match.put("ip_src_mask", ip_src_mask);
		}
	}
	
	public void set_ipv4_dst(String value){
		String [] parts = value.split("/");
		
		match.put("eth_type", "2048");
		
		if (parts.length == 1){
			match.put("ip_src", value);
		}
		else{
			String ip_dst_mask = parts[0]+"/";
			long mask_int = 0xffffffffL;
			ip_dst_mask += longToIpv4(mask_int << (32-Integer.parseInt(parts[1])));
			match.put("ip_dst_mask", ip_dst_mask);
		}
	}
	
	public void set_tcp_src(int value){
		match.put("eth_type", "2048");
		match.put("nw_proto", "6");
		match.put("tcp_src", Integer.toString(value));
	}
	
	public void set_tcp_dst(int value){
		match.put("nw_proto", "6");
		match.put("tcp_dst", Integer.toString(value));
	}
	
	public void set_udp_src(int value){
		match.put("nw_proto", "17");
		match.put("udp_src", Integer.toString(value));
	}
	
	public void set_udp_dst(int value){
		match.put("nw_proto", "17");
		match.put("udp_dst", Integer.toString(value));
	}
	
	// Actions
	public void action_output(int value){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "OUTPUT");
		output_action.put("port", Integer.toString(value));
		actions.add(output_action);
	}
	
	public void action_to_controller(){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "OUTPUT");
		output_action.put("port", Long.toString(Long.parseLong("0xfffd",16)));
		actions.add(output_action);
	}
	
	public void action_to_l2l3(){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "OUTPUT");
		output_action.put("port", Long.toString(Long.parseLong("0xfffa",16)));
		actions.add(output_action);
	}
	
	public void action_set_ipv4_src(String srcIP){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "ipv4_src");
		output_action.put("value", srcIP);
		actions.add(output_action);
	}
	
	public void action_set_ipv4_dst(String dstIP){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "ipv4_dst");
		output_action.put("value", dstIP);
		actions.add(output_action);
	}
	
	public void action_set_tcp_src(String srcPort){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "tcp_src");
		output_action.put("value", srcPort);
		actions.add(output_action);
	}
	
	public void action_set_tcp_dst(String dstPort){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "tcp_dst");
		output_action.put("value", dstPort);
		actions.add(output_action);
	}
	
	public void action_set_udp_src(String srcPort){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "udp_src");
		output_action.put("value", srcPort);
		actions.add(output_action);
	}
	
	public void action_set_udp_dst(String dstPort){
		LinkedHashMap<String, String> output_action = 
				new LinkedHashMap<String, String>();
		output_action.put("type", "SET_FIELD");
		output_action.put("field", "udp_dst");
		output_action.put("value", dstPort);
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
		for (int i = 0; i < 4; ++i){
			IpStr = IpStr + (Ipv4long & mask);
			Ipv4long >>= 4;
			IpStr += ".";
		}
		return IpStr.substring(0, IpStr.length()-1);
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
			
			
			
			msg_json.put("buckets", bucket_json);
			
			
			
		}
		return msg_json.toString();
	}

}
