package cn.lmx.flow.bean.view.json.head;

public class ProcMappingBean {
	//源字段
	private String sourceField;
	//目标类型
	private String targetType;
	//目标字段
	private String targetField;
	/**
	 * 构造含数
	 */
	public ProcMappingBean() {
		
	}
	public String getSourceField() {
		return sourceField;
	}
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getTargetField() {
		return targetField;
	}
	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}
}
