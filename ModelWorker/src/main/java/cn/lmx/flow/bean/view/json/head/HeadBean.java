package cn.lmx.flow.bean.view.json.head;

import java.util.ArrayList;
import java.util.List;

public class HeadBean {
	//行数
	private int rows;
	//列数
	private int cols;
	//数据表
	private String table;
	//目标数据库
	private String target;
	//数据主键
	private String tableKey;
	//取得SQL
	private String sql;
	//SQL存储过程区分
	private String sqlProc;
	//默认值设定
	private DefaultValueBean defaultValue;
	//单元格
	private List<CellBean> cells;
	//合并的单元格
	private List<MarginBean> margin;
	//保存时运行的存储过程
	private List<ProcBean> saveProc;
	//自定义按钮
	private List<ButtonBean> buttons;
	/**
	 * 构造函数
	 */
	public HeadBean() {
		cells = new ArrayList<CellBean>();
		margin = new ArrayList<MarginBean>();
		saveProc = new ArrayList<ProcBean>();
		buttons = new ArrayList<ButtonBean>();
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTableKey() {
		return tableKey;
	}
	public void setTableKey(String tableKey) {
		this.tableKey = tableKey;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getSqlProc() {
		return sqlProc;
	}
	public void setSqlProc(String sqlProc) {
		this.sqlProc = sqlProc;
	}
	public DefaultValueBean getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(DefaultValueBean defaultValue) {
		this.defaultValue = defaultValue;
	}
	public List<CellBean> getCells() {
		return cells;
	}
	public void setCells(List<CellBean> cells) {
		this.cells = cells;
	}
	public List<MarginBean> getMargin() {
		return margin;
	}
	public void setMargin(List<MarginBean> margin) {
		this.margin = margin;
	}
	public List<ProcBean> getSaveProc() {
		return saveProc;
	}
	public void setSaveProc(List<ProcBean> saveProc) {
		this.saveProc = saveProc;
	}
	public List<ButtonBean> getButtons() {
		return buttons;
	}
	public void setButtons(List<ButtonBean> buttons) {
		this.buttons = buttons;
	}
}
