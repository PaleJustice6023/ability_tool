package com.yinc.ability;

import java.util.TreeMap;

public class AbilityInfo {
	private String name = null;
	private TreeMap<String, ParamInfo> request = null;
	private TreeMap<String, ParamInfo> answer = null;
	
	public AbilityInfo(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRequestParams(TreeMap<String, ParamInfo> request) {
		this.request = request;
	}
	
	public TreeMap<String, ParamInfo> getRequestParams(){
		return request;
	}
	
	public void setAnswerParams(TreeMap<String, ParamInfo> answer) {
		this.answer = answer;
	}
	
	public TreeMap<String, ParamInfo> getAnswerParams(){
		return answer;
	}
}
