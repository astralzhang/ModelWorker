package cn.lmx.flow.bean.view.json.input;

public class Parameter {
	//目标字段
	private String targetField;
	//来源
	private String source;
	//来源字段
	private String sourceField;
	//映射方法（header：表头、title：明细标题、column：明细列）
	private String mapping;
	/**
	 * 构造函数
	 */
	public Parameter() {
		
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
	public String getSourceField() {
		return sourceField;
	}
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
	public String getMapping() {
		return mapping;
	}
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
}
