package cn.lmx.flow.bean.report.json;

import java.util.ArrayList;
import java.util.List;

public class ViewBean {
	//sql文
	private String sql;
	//类型
	private String type;
	//运行时点
	private String runTime;
	//目标数据库
	private String target;
	//固定行数
	private int fixRows;
	//固定列数
	private int fixCols;
	//条件
	private List<ConditionField> condition;
	//画面项目
	private List<ViewField> view;
	public ViewBean() {
		condition = new ArrayList<ConditionField>();
		view = new ArrayList<ViewField>();
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public int getFixRows() {
		return fixRows;
	}
	public void setFixRows(int fixRows) {
		this.fixRows = fixRows;
	}
	public int getFixCols() {
		return fixCols;
	}
	public void setFixCols(int fixCols) {
		this.fixCols = fixCols;
	}
	public List<ConditionField> getCondition() {
		return condition;
	}
	public void setCondition(List<ConditionField> condition) {
		this.condition = condition;
	}
	public List<ViewField> getView() {
		return view;
	}
	public void setView(List<ViewField> view) {
		this.view = view;
	}
}
