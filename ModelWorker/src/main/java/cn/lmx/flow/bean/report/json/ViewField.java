package cn.lmx.flow.bean.report.json;

public class ViewField {
	//显示名称
	private String label;
	//字段名称
	private String field;
	//是否链接
	private String link;
	//画面编码
	private String viewNo;
	//ID字段
	private String idField;
	/**
	 * 构造函数
	 */
	public ViewField() {
		
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getViewNo() {
		return viewNo;
	}
	public void setViewNo(String viewNo) {
		this.viewNo = viewNo;
	}
	public String getIdField() {
		return idField;
	}
	public void setIdField(String idField) {
		this.idField = idField;
	}
}
