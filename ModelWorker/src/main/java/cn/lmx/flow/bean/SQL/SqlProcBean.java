package cn.lmx.flow.bean.SQL;

import java.util.ArrayList;
import java.util.List;

public class SqlProcBean {
	//数据库对象
	private String target;
	//处理SQL语句
	private String sql;
	//处理存储过程
	private String proc;
	//处理类型(Select, Update)
	private String type;
	//显示字段列表
	private List<FieldBean> showFields;
	//查询条件字段列表
	private List<FieldBean> conditionFields;
	//查询条件处理函数
	private String conditionFunc;
	/**
	 * 构造函数
	 */
	public SqlProcBean() {
		showFields = new ArrayList<FieldBean>();
		conditionFields = new ArrayList<FieldBean>();
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getProc() {
		return proc;
	}
	public void setProc(String proc) {
		this.proc = proc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<FieldBean> getShowFields() {
		return showFields;
	}
	public void setShowFields(List<FieldBean> showFields) {
		this.showFields = showFields;
	}
	public List<FieldBean> getConditionFields() {
		return conditionFields;
	}
	public void setConditionFields(List<FieldBean> conditionFields) {
		this.conditionFields = conditionFields;
	}
	public String getConditionFunc() {
		return conditionFunc;
	}
	public void setConditionFunc(String conditionFunc) {
		this.conditionFunc = conditionFunc;
	}
}
