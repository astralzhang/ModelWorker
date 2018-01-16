package cn.lmx.flow.bean.view.json.sync;

public class Parameter {
	//目标字段
	private String targetField;
	//来源编码
	private String sourceNo;
	//来源字段
	private String sourceField;
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
	public String getSourceNo() {
		return sourceNo;
	}
	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}
	public String getSourceField() {
		return sourceField;
	}
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
}
