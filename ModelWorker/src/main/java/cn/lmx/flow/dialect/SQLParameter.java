package cn.lmx.flow.dialect;

public class SQLParameter {
	private String type;
	private Object value;
	/**
	 * 构造函数
	 */
	public SQLParameter() {
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
