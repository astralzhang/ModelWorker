package cn.lmx.flow.bean.view.json.detail;

public class FieldBean {
	//显示
	private String label;
	//编辑类型
	private String editor;
	//是否提交
	private String submit;
	//字段属性
	private PropertyBean props;
	/**
	 * 构造函数
	 */
	public FieldBean() {
		
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getSubmit() {
		return submit;
	}
	public void setSubmit(String submit) {
		this.submit = submit;
	}
	public PropertyBean getProps() {
		return props;
	}
	public void setProps(PropertyBean props) {
		this.props = props;
	}
}
