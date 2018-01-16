package cn.lmx.flow.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
public class ExcelUtils {
	/**
	 * 根据单元格名称取得单元格值
	 * @param sheet
	 * @param cellName
	 * @return
	 * @throws Exception
	 */
	public static Object getCellDataByName(Sheet sheet, String cellName) throws Exception {
		Map<String, Integer> rowColNoMap = parseCellName(cellName);
		int rowNo = rowColNoMap.get("row");
		int colNo = rowColNoMap.get("col");
		return getCellData(sheet, rowNo, colNo);
	}
	/**
	 * 根据行列号取得单元格值
	 * @param sheet
	 * @param rowNo
	 * @param colNo
	 * @return
	 */
	public static Object getCellData(Sheet sheet, int rowNo, int colNo) {
		Row row = sheet.getRow(rowNo - 1);
		if (row == null) {
			return null;
		}
		Cell cell = row.getCell(colNo - 1);
		if (cell == null) {
			return null;
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_FORMULA:
			return cell.getStringCellValue();
		default:
			return null;
		}
	}
	/**
	 * 根据Excel单元格名称取得单元格的行列号
	 * @param cellName
	 * @return
	 */
	public static Map<String, Integer> parseCellName(String cellName) {
		if (cellName == null || "".equals(cellName)) {
			return null;
		}
		cellName = cellName.toUpperCase();
		String strPart = "";
		int iPos = -1;
		for (int i = 0; i < cellName.length(); i++) {
			char ch = cellName.charAt(i);
			if (ch >= 'A' && ch <= 'Z') {
				strPart += ch;
				continue;
			}
			iPos = i;
			break;
		}
		String numPart = cellName.substring(iPos);
		if (strPart.length() <= 0) {
			return null;
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		//行号
		try {
			int iRowNo = Integer.parseInt(numPart);
			map.put("row", iRowNo);
		} catch(NumberFormatException e) {
			return null;
		}
		//列号计算
		int iJinz = 0;
		int iColNo = 0;
		for (int i = strPart.length() - 1; i >= 0; i--) {
			char ch = strPart.charAt(i);
			int iNum = ch - 'A' + 1;
			for (int j = 0; j < iJinz; j++) {
				iNum *= 26;
			}
			iColNo += iNum;
			iJinz++;
		}
		map.put("col", iColNo);
		return map;
	}
	/**
	 * 根据标题取得列号
	 * @param sheet
	 * @param startRowNo
	 * @param endRowNo
	 * @param title
	 * @return
	 */
	public static int getColNoByTitle(Sheet sheet, int startRowNo, int endRowNo, String title) {
		if (title == null || "".equals(title)) {
			return -1;
		}
		for (int i = startRowNo - 1; i <= endRowNo - 1; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Object value = getCellData(sheet, i + 1, j + 1);
				if (value == null) {
					continue;
				}
				if (title.equals(value)) {
					return j + 1;
				}
			}
		}
		return -1;
	}
	/**
	 * 脚本执行
	 * @param script
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static Object runScript(String script, Map<String, Object> param) throws Exception {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		if (param != null && param.size() > 0) {
			Iterator<Entry<String, Object>> it = param.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = it.next();
				engine.put(entry.getKey(), entry.getValue());
			}
		}
		String functionName = new StringBuffer("")
				.append("checkfunction_")
				.append(UUID.randomUUID().toString().replaceAll("-", "")).toString();
		String sFunction = new StringBuffer("")
				.append("function ")
				.append(functionName)
				.append("(){")
				.append(script)
				.append("}").toString();
		engine.eval(sFunction);
		if (engine instanceof Invocable) {
			Invocable invocable = (Invocable)engine;
			Object value = invocable.invokeFunction(functionName);
			return value;
		}
		return null;
	}
	public static void main(String[] arg) {
		Map<String, Integer> map = ExcelUtils.parseCellName("BB2");
		System.out.println("row=" + map.get("row") + ";col=" + map.get("col"));
		int iPos = "111${ddd}".indexOf("${");
		System.out.println(iPos);
		String x = "111${ddd}";
		System.out.println(x.replaceAll("\\$\\{", "sss"));
	}
}
