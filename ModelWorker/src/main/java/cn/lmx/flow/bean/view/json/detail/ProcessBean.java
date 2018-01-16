package cn.lmx.flow.bean.view.json.detail;

import java.util.ArrayList;
import java.util.List;

public class ProcessBean {
	//处理类型
	private String type;
	//处理SQL
	private String sql;
	//目标数据库
	private String target;
	//字段列表
	private List<FieldBean> fields;
	/**
	 * 构造函数
	 */
	public ProcessBean() {
		fields = new ArrayList<FieldBean>();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<FieldBean> getFields() {
		return fields;
	}
	public void setFields(List<FieldBean> fields) {
		this.fields = fields;
	}
}
