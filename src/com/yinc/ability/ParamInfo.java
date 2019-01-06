package com.yinc.ability;

public class ParamInfo {
	public String name = null;
	public String type = null;
	public String desc = "";
	public String comment = "";
	public boolean mandatory = false;
	
	public ParamInfo(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public ParamInfo(String name, String type, boolean mandatory) {
		this.name = name;
		this.type = type;
		this.mandatory = mandatory;
	}
}
