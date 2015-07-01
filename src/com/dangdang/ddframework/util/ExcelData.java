package com.dangdang.ddframework.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.dangdang.ddframework.core.TestGroups;

import jxl.*;
import jxl.write.WritableWorkbook;


public class ExcelData implements Iterator<Object[]> {
	private Workbook book = null;
	private Sheet sheet = null;
	private int rowNum = 0;// 行数
	private int curRowNo = 0;// 当前行数
	private int columnNum = 0;// 列数
	//private String[] columnnName;// 列名
	
	protected static Logger logger = Logger.getLogger(ExcelData.class);
	
	private final String CASE_NAME = "用例名称";
	private final String EXPECTED = "期望";
	
	private List<String> includeGropupList=new ArrayList<String>();
	private List<String> excludeGropupList=new ArrayList<String>();
	private Map<String,Integer> columnMap=new HashMap<String,Integer>();

	/*
	 * 在TestNG中，由@DataProvider（dataProvider="name"）修饰的方法读取Exel时，调用此类的构造方法（
	 * 此方法会得到列名并将当前行移到下一行）执行完后，转到TestNG自己的方法中去，然后由他们调用此类实现的hasNext()、next() 方法；
	 * 得到一行数据，然后返回给由@Test（dataProvider="name"）修饰的方法，如此反复到数据读完为止。
	 * 
	 * @param filepath Excel文件名
	 * 
	 * @param casename用例名
	 */
	public ExcelData(String filepath, String casename,String[] incluldGroups,String[] excluldGroups) {
		try {
			
			//String ss = "open.anniewang.newexcel.";
			//book = Workbook.getWorkbook(new File(filepath + ".xls"));
			book = Workbook.getWorkbook(ResourceLoader.class.getClassLoader().getResourceAsStream(filepath + ".xls"));
			this.sheet = book.getSheet(casename);
			this.rowNum = sheet.getRows();

			Cell[] c = sheet.getRow(0);
			this.columnNum = c.length;
			//columnnName = new String[c.length];
			for (int i = 0; i < c.length; i++) {
			//	columnnName[i] = c[i].getContents().toString();
				columnMap.put(c[i].getContents().trim(),i);
			}
			this.curRowNo++;
			
			Collections.addAll(includeGropupList, incluldGroups);
			Collections.addAll(excludeGropupList, excluldGroups);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasNext() {
		/**
		 * 方法功能：是否有下一条数据 如果行数为0即空sheet或者 当前行数大于总行数 就关闭对excel的操作返回false，否则返回true
		 *
		 */
		if (this.rowNum == 0 || this.curRowNo >= this.rowNum) {
			try {
				
				book.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else{
			//循环找到下一个符合要求的行，没有返回false
			for(int i =curRowNo;i<this.rowNum;i++){
				if(checkGroups(i)){
					curRowNo=i;
					return true;
				}
			}
			return false;
		}
	}
	
	/*
	 * 校验分组信息，校验此行数据是否符合目前运行的分组
	 * group列不存在或者为空表示不进行分组，任何group上都运行
	 */
	private boolean checkGroups(int rowNumber) {
		// TODO Auto-generated method stub
/*		if(includeGropupList.size()==0 && excludeGropupList.size()==0){
			return true;
		}
		*/
		//没有group列，代表任何group都运行，受方法上的group控制
		if(columnMap.get("group")==null){
			return true;
		}
		
		
		//获取当前行的group
		Cell[] c =sheet.getRow(rowNumber);
		String cellContent=c.length>columnMap.get("group") ? c[columnMap.get("group")].getContents().toLowerCase().trim():"";
		
		return TestGroups.CheckGroups(cellContent);
		
		/*
		//如果为空，代表任何group都运行
		if(StringUtils.isBlank(cellContent)){
			return true;
		}
		String[] groupStrings=cellContent.split(",");
		List<String> groupList = new ArrayList<String>();
		Collections.addAll(groupList,groupStrings);

		// 检测是否包含在excludeGropupList里，为空，继续，包括返回false
		if (excludeGropupList.size() != 0) {
			for (String exGroup : excludeGropupList) {
				if (groupList.contains(exGroup.toLowerCase().trim())) {
					return false;
				}
			}
		}

		// 检测是否包含在includeGropupList里，为空，返回true，包括返回true；不包括返回false
		if (includeGropupList.size() == 0) {
			return true;
		} else {

			for (String inGroup : includeGropupList) {
				if (groupList.contains(inGroup.toLowerCase().trim())) {
					return true;
				}
			}
			return false;
		}
*/
	}

	@Override
	public Object[] next() {
		/*
		 * 方法功能：得到并返回下一行数据
		 * 使用for将一行的数据放入TreeMap中（TreeMap默认按照Key值升序排列，HashMap没有排序）
		 * 然后将Map装入Object[]并返回，且将curRowNo当前行下移
		 */
		Cell[] c = sheet.getRow(this.curRowNo);
		
		Map<String, String> s = new TreeMap<String, String>();
		for (Entry<String, Integer> entry: columnMap.entrySet()) {
			String temp = "";
			try {
				Cell currentCell=c[entry.getValue()];
				
				if(currentCell.getType() == CellType.DATE){
					DateCell dc = (DateCell)currentCell;
					temp = dc.getDateFormat().format(dc.getDate());
				}
				else {
					temp = currentCell.getContents().toString();
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				temp = "";
			}
			s.put(entry.getKey(), temp.trim());
		}
		
		Object r[] = new Object[3];
		r[0] = s.get(CASE_NAME);
		r[1] = s;
		r[2] = s.get(EXPECTED);
		
	    
		logger.info("测试用例---"+r[0]+"---开始执行");
		
	//	s.remove(CASE_NAME);
	//	s.remove(EXCEPTED);
		s.remove("group");
		
		this.curRowNo++;
		return r;
	}
	

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove unsupported.");
	}
}