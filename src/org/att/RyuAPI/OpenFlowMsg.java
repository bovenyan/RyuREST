package org.att.RyuAPI;
import java.util.*;

public class OpenFlowMsg {	
	String msg_type;
	
	// Setting
	int dpid = 0;
	int in_port = 0;
	long cookie = 0;
	long cookie_mask = 0xffffffffL;
	int table_id = 0;
	
	// Match
	final int eth_type = 2048;
	String ip_src = "";
	String ip_src_mask = "";
	String ip_dst = "";
	String ip_dst_mask = "";
	int nw_proto = 0;
	int tcp_src = 0;
	int tcp_dst = 0;
	int udp_src = 0;
	int udp_dst = 0;

	// Flow Actions
	int out_port = 0;
	int out_group = 0;
	String mod_ip_dst = "";
	String mod_ip_src = "";
	String mod_udp_dst = "";
	String mod_udp_src = "";
	
	// Group Actions
	int group_id = 0;
	List<Integer> out_ports = new ArrayList<Integer>();
	
	// Setting Map
	Map<String, Boolean> setting = new HashMap<String, Boolean>();
	
	public OpenFlowMsg(String type) {
		msg_type = type;
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
	
	/**
	 * @param field the field that is set using (String, String) pair.
	 * Including ip_src, ip_src. ip_src can be specified as 10.0.0.1, 
	 * Or 10.0.0.0/24, which will set ip_src or ip_src_mask, respectively
	 * @param value the value given to the corresponding field
	 */
	public void setField(String field, String value){
		if (field.equals("ip_src")){
			String [] parts = value.split("/");
			
			if (parts.length == 1){
				ip_src = value;
				setting.put("ip_src", true);
			}
			else{
				ip_src_mask = parts[0]+"/";
				long mask_int = 0xffffffffL;
				ip_src_mask += longToIpv4(mask_int << (32-Integer.parseInt(parts[1])));
				setting.put("ip_src_mask", true);
			}
		}
		if (field.equals("ip_dst")){
			String [] parts = value.split("/");
			if (parts.length == 1){
				ip_dst = value;
				setting.put("ip_dst", true);
			}
			else{
				ip_dst_mask = parts[0]+"/";
				long mask_int = 0xffffffffL;
				ip_dst_mask += longToIpv4(mask_int << (32-Integer.parseInt(parts[1])));
				setting.put("ip_dst_mask", true);
			}
		}
	}
	
	
	public void setGroup(int gId, )
	
	/**
	 * @param field the field that is set using (String, int) pair. 
	 * Including: in_port, cookie, cookie_mask, nw_proto, tcp_src, 
	 * tcp_dst, udp_src 
	 * @param value the value given to the corresponding field.
	 * e.g. setField("cookie_mask", 24) sets the cookie_mask to 0xffffff00 
	 */
	public void setField(String field, int value){
		if (field.equals("in_port")){
			in_port = value;
			setting.put("in_port", true);
			return;
		}
		if (field.equals("cookie")){
			cookie = value;
			setting.put("cookie", true);
			return;
		}
		if (field.equals("cookie_mask")){
			cookie_mask = 0xffffffffL & (0xffffffffL << (32-value));
			setting.put("cookie_mask", true);
			return;
		}
		if (field.equals("table_id")){
			table_id = value;
			setting.put("table_id", true);
			return;
		}
		if (field.equals("tcp_src")){
			nw_proto = 6;
			tcp_src = value;
			setting.put("tcp_src", true);
			setting.put("nw_proto", true);
			return;
		}
		if (field.equals("tcp_dst")){
			nw_proto = 6;
			tcp_dst = value;
			setting.put("tcp_dst", true);
			setting.put("nw_proto", true);
			return;
		}
		if (field.equals("udp_src")){
			nw_proto = 17;
			udp_src = value;
			setting.put("udp_src", true);
			setting.put("nw_proto", true);
			return;
		}
		if (field.equals("udp_dst")){
			nw_proto = 17;
			udp_dst = value;
			setting.put("udp_dst", true);
			setting.put("nw_proto", true);
			return;
		}
	}
	
	
	
	public String toREST(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if (setting.get("dpid")){
			sb.append("\"dpid:\"");
			sb.append(dpid);
			sb.append(",");
		}
		else{ // missing dpid
			return "";
		}
		
		if (setting.get("cookie")){
			sb.append("\"cookie\":");
			sb.append(cookie);
			sb.append(",");
		}
		if (setting.get("cookie_mask")){
			sb.append("\"cookie_mask\": ");
			sb.append(cookie_mask);
			sb.append(",");
		}
		if (setting.get("table_id")){
			sb.append("\"table_id\": ");
			sb.append(table_id);
			sb.append(",");
		}
		
		// build match
		if (msg_type.contains("Flow")){
			sb.append("\"match\":{");
			
			if (setting.get("in_port")){
				sb.append("\"in_port\":");
				sb.append(in_port);
				sb.append(",");
			}
			
			if (setting.get("ip_src")){
				sb.append("\"ip_src\":");
				sb.append(ip_src);
				sb.append(",");
			}
			
			if (setting.get("ip_dst")){
				sb.append("\"ip_dst\":");
				sb.append(ip_dst);
				sb.append(",");
			}
			
			if (setting.get("tcp_src")){
				sb.append("\"tcp_src\":");
				sb.append(tcp_src);
				sb.append(",");
			}
			
			if (setting.get("tcp_dst")){
				sb.append("\"tcp_dst\":");
				sb.append(tcp_dst);
				sb.append(",");
			}
			
			if (setting.get("udp_src")){
				sb.append("\"udp_src\":");
				sb.append(udp_src);
				sb.append(",");
			}
			
			if (setting.get("udp_dst")){
				sb.append("\"udp_dst\":");
				sb.append(udp_dst);
				sb.append(",");
			}
			
			if (sb.length() > 0){ // delete last comma
				sb.setLength(sb.length()-1);
			}
			
			sb.append("},");
			
			if (msg_type.equals("AddFlow")){
				sb.append("\"actions\":[");
				if (setting.get("to_controller")){
					
				}
				sb.append("]");
			}
		}
		
		if (msg_type.contains("Group")){
			if (setting.get("group_id")){
				
				sb.append("\"group_id\":");
				sb.append(group_id);
				sb.append(",");
				
				if (msg_type.equals("AddGroup")){
					sb.append("\"type\": \"ALL\",");
					
					sb.append("\"buckets\": [");
				}
			}
			else{
				return "";
			}
		}
		
		
		
		
		
		sb.append("}");
		
		return sb.toString();
	}

}
