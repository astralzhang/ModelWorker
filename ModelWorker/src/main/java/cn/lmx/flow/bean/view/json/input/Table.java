package cn.lmx.flow.bean.view.json.input;

import java.util.ArrayList;
import java.util.List;

public class Table {
	//目标数据库
	private String target;
	//表名
	private String table;
	//字段映射
	private List<MappingField> mapping;
	/**
	 * 构造函数
	 */
	public Table() {
		mapping = new ArrayList<MappingField>();
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public List<MappingField> getMapping() {
		return mapping;
	}
	public void setMapping(List<MappingField> mapping) {
		this.mapping = mapping;
	}
}
