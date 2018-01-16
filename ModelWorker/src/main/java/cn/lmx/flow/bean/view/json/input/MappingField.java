package cn.lmx.flow.bean.view.json.input;

import java.util.ArrayList;
import java.util.List;

public class MappingField {
	//目标字段
	private String targetField;
	//来源
	private String source;
	//函数
	private String func;
	//映射类型（header：表头、title：明细标题、column：明细列）
	private String mapping;
	//源字段
	private String sourceField;
	//是否关键字段
	private String keyField;
	//是否覆盖（数据更新是否为更新的对象）
	private String replace;
	//SQL执行数据库
	private String target;
	//参数
	private List<Parameter> parameter;
	/**
	 * 构造函数
	 */
	public MappingField() {
		parameter = new ArrayList<Parameter>();
	}
	public String getTargetField() {
		return targetField;
	}
	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
	}
	public String getMapping() {
		return mapping;
	}
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	public String getSourceField() {
		return sourceField;
	}
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
	public String getKeyField() {
		return keyField;
	}
	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}
	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<Parameter> getParameter() {
		return parameter;
	}
	public void setParameter(List<Parameter> parameter) {
		this.parameter = parameter;
	}
}
