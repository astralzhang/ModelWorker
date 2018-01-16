package cn.lmx.flow.bean.report.json;

public class UrlParameter {
	//参数ID
	private String paramId;
	//数据源字段
	private String sourceField;
	/**
	 * 构造函数
	 */
	public UrlParameter() {
		
	}
	public String getParamId() {
		return paramId;
	}
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	public String getSourceField() {
		return sourceField;
	}
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}
}
