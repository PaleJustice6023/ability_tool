package com.yinc;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.yinc.ability.AbilityException;
import com.yinc.ability.AbilityInfo;
import com.yinc.ability.ParamInfo;
import com.yinc.util.FileUtil;
import com.yinc.util.SchemaBuilder;
import com.yinc.util.ScriptBuilder;

public class AbilityTool {
	public static void main(String[] args) {
//		if (2 != args.length) {
//			System.out.println("Usage: <Specification File> <Output Path>");
//			return;
//		}
		
		XWPFDocument xwdoc = null;
		ArrayList<String> apiNames = null;
		ArrayList<AbilityInfo> abilities = null;
		String outputPath = "E:\\project\\ws\\iotgateway\\zhiwang\\output\\";
		
		try {
			xwdoc = new XWPFDocument(new FileInputStream("E:\\project\\ws\\iotgateway\\zhiwang\\CMP_智能网语音黑白名单对外接口.docx"));
			List<XWPFTable> tables = xwdoc.getTables();
			
			apiNames = getApiNames(tables);
			abilities = getAbilityInfos(apiNames, tables);
			
			for (int i = 0; i < abilities.size(); ++i) {
				AbilityInfo ai = abilities.get(i);
				System.out.println(ai.getName() + " processing...");
				
				FileUtil.writeTxtFile(outputPath + "ws" + ai.getName() + "_req.json", SchemaBuilder.generateMainReq(ai));
				FileUtil.writeTxtFile(outputPath + "ws" + ai.getName() + "_rsp.json", SchemaBuilder.generateMainRsp(ai));
				FileUtil.writeTxtFile(outputPath + "ws" + ai.getName() + ".js", ScriptBuilder.generate(ai));
			}
			
			System.out.println("All done.");;
		}
		catch (AbilityException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
		}
		finally {
			if (null != xwdoc) {
				try {
					xwdoc.close();
				} catch (IOException e) {
					System.out.println("Close with exception.");
					e.printStackTrace();
				}
			}
		}
	}
	
	private static ArrayList<String>getApiNames(List<XWPFTable> tables){
		ArrayList<String> apiNames = new ArrayList<String>();
		for (XWPFTable table : tables) {
		    // 获取表格的行
		    List<XWPFTableRow> rows = table.getRows();    
		    XWPFTableRow firstRow = rows.get(0);
		    List<XWPFTableCell> firstCells = firstRow.getTableCells();
		    String firstCell = firstCells.get(0).getText().trim();
		    if (firstCell.equalsIgnoreCase("API名称")) {
		    	String apiName = firstCells.get(1).getText().trim();
		    	String firstChar = apiName.substring(0, 1);
		    	String lastChars = apiName.substring(1);
		    	firstChar = firstChar.toUpperCase();
		    	apiNames.add(firstChar + lastChars);
		    	continue;
		    }
		}
		
		return apiNames;
	}
	
	private static ArrayList<AbilityInfo>getAbilityInfos(ArrayList<String> apiNames, List<XWPFTable> tables) throws AbilityException{
		ArrayList<AbilityInfo> abilities = new ArrayList<AbilityInfo>();
		
		int tabIdx = 0;
		int msgIdx = 0;
		do {
			XWPFTable table = tables.get(tabIdx);
			
			// 获取表格的第一行
		    List<XWPFTableRow> rows = table.getRows();    
		    XWPFTableRow firstRow = rows.get(0);
		    List<XWPFTableCell> firstCells = firstRow.getTableCells();
		    String firstCell = firstCells.get(0).getText().trim();
		    
		    if (!firstCell.equals("名称")) {
		    	tabIdx++;
		    	continue;
		    }
		    
		    msgIdx++;
		    if (apiNames.size() < msgIdx / 2) {
				throw new AbilityException("请求说明与参数数量不匹配");
			}
		    
		    TreeMap<String, ParamInfo> params = new TreeMap<String, ParamInfo>();
		    
		    int rowsCnt = rows.size();
		    for (int i = 1; i < rowsCnt; ++i) {
		    	XWPFTableRow row = rows.get(i);
		    	List<XWPFTableCell> tableCells = row.getTableCells();
		    	if (5 != tableCells.size()) {
		    		throw new AbilityException("Table format is invalid. tabIdx=" + tabIdx + ", rowsCnt=" + rowsCnt);
		    	}
		    	
		    	String name = tableCells.get(0).getText().trim();
		    	String type = tableCells.get(1).getText().trim();
		    	String mandatory = tableCells.get(2).getText().trim();
		    	String desc = tableCells.get(3).getText().trim();
		    	String comment = tableCells.get(4).getText().trim();
		    	
		    	ParamInfo pi = new ParamInfo(name, type);
		    	if (mandatory.equalsIgnoreCase("Y")) {
		    		pi.mandatory = true;
		    	}
		    	else {
		    		pi.mandatory = false;
		    	}
		    	pi.desc = desc;
		    	pi.comment = comment;
		    	params.put(name, pi);
		    }
		    
		    AbilityInfo ai = null;
		    if (0 == msgIdx % 2) {
		    	ai = abilities.get((msgIdx - 1)/2);
		    	ai.setAnswerParams(params);
		    }
		    else {
		    	ai = new AbilityInfo(apiNames.get((msgIdx - 1)/2));
		    	ai.setRequestParams(params);
		    	abilities.add(ai);
		    }

		    tabIdx++;
		} while(tabIdx < tables.size());

		return abilities;
	}
}
