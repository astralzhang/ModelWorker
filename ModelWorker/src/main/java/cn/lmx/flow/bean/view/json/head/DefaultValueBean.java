package cn.lmx.flow.bean.view.json.head;

import java.util.ArrayList;
import java.util.List;

public class DefaultValueBean {
	//目标数据库
	private String target;
	//SQL文/存储过程
	private String sql;
	//字段映射
	private List<DefaultValueMappingBean> mapping;
	/**
	 * 构造函数
	 */
	public DefaultValueBean() {
		mapping = new ArrayList<DefaultValueMappingBean>();
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
	public List<DefaultValueMappingBean> getMapping() {
		return mapping;
	}
	public void setMapping(List<DefaultValueMappingBean> mapping) {
		this.mapping = mapping;
	}
}
