package com.yinc.util;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.yinc.ability.AbilityInfo;
import com.yinc.ability.ParamInfo;

public class SchemaBuilder {
	public static String generateMainReq(AbilityInfo ai) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n" + 
				"  \"$schema\" : \"http://json-schema.org/draft-04/schema#\",\n");
		
		sb.append("  \"title\" : \"ws");
		sb.append(ai.getName());
		sb.append("\",");
		
		sb.append("  \"type\" : \"object\",\n" + 
				"  \"properties\" : {\n" + 
				"    \"app_id\" : {\n" + 
				"      \"type\" : \"string\"\n" + 
				"    },\n" + 
				"    \"timestamp\" : {\n" + 
				"      \"type\" : \"string\"\n" + 
				"    },\n" + 
				"    \"trans_id\" : {\n" + 
				"      \"type\" : \"string\"\n" + 
				"    },\n" + 
				"    \"token\" : {\n" + 
				"      \"type\" : \"string\"\n" + 
				"    },\n" + 
				"    \"data\" : {\n" + 
				"      \"type\" : \"object\",\n" + 
				"      \"properties\" : {\n");
		
		TreeMap<String, ParamInfo> reqParams = ai.getRequestParams();
		Iterator<Entry<String, ParamInfo>> it = reqParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, ParamInfo> entry = (Entry<String, ParamInfo>) it.next();
			String name = entry.getKey();
			ParamInfo pi = entry.getValue();
			
			sb.append("        \"");
			sb.append(name);
			sb.append("\" : {\n");
			
			sb.append("          \"type\" : \"");
			sb.append(pi.type);
			sb.append("\"\n" + 
					"        }");
			
			if (it.hasNext()) {
				sb.append(",");
			}
			sb.append("\n");
		}
		
		sb.append("}\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"required\" : [ \"app_id\", \"timestamp\", \"trans_id\", \"token\" ]\n" + 
				"}");
		return sb.toString();
	}
	
	public static String generateMainRsp(AbilityInfo ai) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n" + 
				"  \"$schema\" : \"http://json-schema.org/draft-04/schema#\",\n" + 
				"  \"type\" : \"object\",\n" + 
				"  \"properties\" : {\n" + 
				"    \"status\" : {\n" + 
				"      \"type\" : \"string\"\n" + 
				"    },\n" + 
				"    \"message\" : {\n" + 
				"      \"type\" : \"string\"\n" + 
				"    },\n" + 
				"    \"data\" : {\n" + 
				"      \"type\" : \"object\",\n" + 
				"      \"properties\" : {\n" + 
				"        \"rspcode\" : {\n" + 
				"          \"type\" : \"string\"\n" + 
				"        },\n" + 
				"        \"rspdesc\" : {\n" + 
				"          \"type\" : [ \"string\", \"null\" ]\n" + 
				"        }\n" + 
				"      }\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"required\" : [ \"status\", \"message\" ]\n" + 
				"}");
		return sb.toString();
	}
	
	public static String generate(AbilityInfo ai) {
		StringBuffer sb = new StringBuffer("{}");
		return sb.toString();
	}
}
