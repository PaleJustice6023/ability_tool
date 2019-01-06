package com.yinc.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.yinc.ability.AbilityInfo;
import com.yinc.ability.ParamInfo;

public class ScriptBuilder {
	public static String generate(AbilityInfo ai) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("importPackage(java.lang);\n" + 
				"importPackage(java.text);\n" + 
				"importPackage(java.util);\n" + 
				"importPackage(java.sql);\n" + 
				"importPackage(org.dom4j);\n" + 
				"importPackage(com.tydic.esb.utils);\n" + 
				"importPackage(com.tydic.esb);\n" + 
				"importPackage(com.tydic.esb.sc);\n" + 
				"importPackage(com.tydic.esb.sc.json);\n" + 
				"\n" + 
				"var commconfigMap = getScCommConfigMap();\n" + 
				"\n" + 
				"//获取请求对象\n" + 
				"var jsonReqNode = msgCtx.getReqObj();\n" + 
				"\n" + 
				"/* 获取请求中的数据 */\n" + 
				"var appId = jsonReqNode.path(\"app_id\").asText();\n" + 
				"var data = jsonReqNode.path(\"data\");\n");
		
		ArrayList<String> dataParams = new ArrayList<String>();
		TreeMap<String, ParamInfo> reqParams = ai.getRequestParams();
		Iterator<Entry<String, ParamInfo>> it = reqParams.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, ParamInfo> entry = (Entry<String, ParamInfo>) it.next();
			String name = entry.getKey();
			dataParams.add(name);
			
			sb.append("var arg_");
			sb.append(name);
			sb.append(" = data.path(\"");
			sb.append(name);
			sb.append("\");\n");
		}
		
		sb.append("\n" + 
				"ESB.log.info(\"能力使用者消息码流：\" + jsonReqNode);\n" + 
				"/* 获取请求中的数据结束 */\n" + 
				"\n" + 
				"ESB.log.info(\"***********开始调用子服务***********\");\n" + 
				"var rspObj = callLivegetfourSon(msgCtx);\n" + 
				"var status = \"203\";\n" + 
				"var message = \"CONGESTION IN MOBILE NETWORK\";\n" + 
				"var rspMap = new HashMap();\n" + 
				"if (rspObj == null)\n" + 
				"{\n" + 
				"	// 响应超时\n" + 
				"}\n" + 
				"else\n" + 
				"{\n" + 
				"	//将返回对象转为Map\n" + 
				"	var resultMap = ESB.json2Map(rspObj.getRspText());\n" + 
				"	ESB.log.info(\"能力提供者响应消息码流：\" + resultMap);\n" + 
				"\n" + 
				"	if (resultMap != null)\n" + 
				"	{\n" + 
				"		status = \"0\";\n" + 
				"		message = \"OK\";\n" + 
				"		\n" + 
				"		if (resultMap.containsKey(\"resultCode\")){\n" + 
				"			var rspcode = resultMap.get(\"resultCode\");\n" + 
				"			resultMap.remove(\"resultCode\");\n" + 
				"			resultMap.put(\"rspcode\", rspcode);\n" + 
				"		}\n" + 
				"		\n" + 
				"		if (resultMap.containsKey(\"resultDesc\")){\n" + 
				"			var rspdesc = resultMap.get(\"resultDesc\");\n" + 
				"			resultMap.remove(\"resultDesc\");\n" + 
				"			resultMap.put(\"rspdesc\", rspdesc);\n" + 
				"		}\n" + 
				"	}\n" + 
				"	else {\n" + 
				"		resultMap= new HashMap();\n" + 
				"		resultMap.put(\"rspcode\", \"1\");\n" + 
				"		resultMap.put(\"rspdesc\", \"Result is null.\");\n" + 
				"	}\n" + 
				"	\n" + 
				"	rspMap.put(\"data\", resultMap);\n" + 
				"}\n" + 
				"\n" + 
				"//构建返回信息Map\n" + 
				"rspMap.put(\"status\", status);\n" + 
				"rspMap.put(\"message\", message);\n" + 
				"ESB.log.info(\"能力使用者响应消息码流：\" + rspMap);\n" + 
				"/* 构建返回信息结束 */\n" + 
				"\n" + 
				"//将返回信息转为JSON字符串并返回\n" + 
				"return net.sf.json.JSONObject.fromObject(rspMap).toString();\n" + 
				"\n" + 
				"\n" + 
				"//调用微服务接口\n" + 
				"function callLivegetfourSon(msgCtx){\n");
		
		sb.append("	//定义请求的服务名\n");
		sb.append("	var subSvcName = \"cn_ws");
		sb.append(ai.getName());
		sb.append("_V1_1\";\n");
		sb.append("	//定义请求的方法名\n");
		sb.append("	var methodName = \"cn_ws");
		sb.append(ai.getName());
		sb.append("_V1_1\";\n");
		
		sb.append("	//定义请求的服务版本号\n" + 
				"	var svcVersion = \"V1.1\";\n" + 
				"	/* 构建请求报文 */\n" + 
				"	var bodyMap = new HashMap();\n");
		
		for (int i = 0; i < dataParams.size(); ++i) {
			sb.append("	if (null != arg_");
			sb.append(dataParams.get(i));
			sb.append("){\n");
			
			sb.append("		bodyMap.put(\"");
			sb.append(dataParams.get(i));
			sb.append("\", arg_");
			sb.append(dataParams.get(i));
			sb.append(".asText());\n");
			
			sb.append("	}\n");
		}
		
		sb.append("	\n" + 
				"	//构建请求Map\n" + 
				"	//将请求Map转换为JSON\n" + 
				"	var reqJson = JsonUtil.toJson(bodyMap);\n" + 
				"	ESB.log.info(\"能力提供者消息码流：\" + reqJson);\n" + 
				"	/* 构建请求报文结束 */\n" + 
				"\n" + 
				"	//定义返回对象\n" + 
				"	var rspObj = null;\n" + 
				"\n" + 
				"	try{\n" + 
				"		//打日志\n" + 
				"		ESB.log.info(\"：======================\"+subSvcName);\n" + 
				"		//发送请求，将返回扔给返回对象\n" + 
				"		rspObj = ESB.invoke(msgCtx,subSvcName,svcVersion,methodName,null,null,null,null,null,null,reqJson,false);\n" + 
				"	}catch(e){\n" + 
				"		if (e.javaException instanceof com.tydic.esb.EsbException) {\n" + 
				"			ESB.log.error(\"调用子服务\"+subSvcName+\"时出错：\",e.javaException);;\n" + 
				"		}else{\n" + 
				"			ESB.log.error(\"调用子服务\"+subSvcName+\"时出错：\"+e.message);\n" + 
				"		}\n" + 
				"		//ESB.throwExp(\"服务暂不可用\");	\n" + 
				"	}\n" + 
				"	return rspObj;\n" + 
				"}");
		return sb.toString();
	}
}
