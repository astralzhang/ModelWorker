package cn.lmx.flow.bean.view.json.input;

import java.util.ArrayList;
import java.util.List;

public class ImportSet {
	//文件类型
	private String type;
	//Excel文件设定信息
	private Excel excel;
	//文本文件设定信息
	private Text text;
	//空行处理设定
	private SpaceRow spaceRow;
	//导入表设定信息
	private List<Table> table;
	//SQL或存储过程
	private List<SqlProc> proc;
	/**
	 * 构造函数
	 */
	public ImportSet() {
		table = new ArrayList<Table>();
		proc = new ArrayList<SqlProc>();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Excel getExcel() {
		return excel;
	}
	public void setExcel(Excel excel) {
		this.excel = excel;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public SpaceRow getSpaceRow() {
		return spaceRow;
	}
	public void setSpaceRow(SpaceRow spaceRow) {
		this.spaceRow = spaceRow;
	}
	public List<Table> getTable() {
		return table;
	}
	public void setTable(List<Table> table) {
		this.table = table;
	}
	public List<SqlProc> getProc() {
		return proc;
	}
	public void setProc(List<SqlProc> proc) {
		this.proc = proc;
	}
}
