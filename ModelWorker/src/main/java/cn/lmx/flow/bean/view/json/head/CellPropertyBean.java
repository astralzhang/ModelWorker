package cn.lmx.flow.bean.view.json.head;

public class CellPropertyBean {
	//字段ID
	private String field;
	//开窗no字段
	private String noField;
	//开窗name字段
	private String nameField;
	//默认值
	private String value;
	//是否只读
	private String readonly;
	//是否保存
	private String save;
	//是否保存名称字段（开窗）
	private String saveName;
	//隐藏字段
	private String hidden;
	//check
	private String validate;
	//事件处理函数
	private String eventFunc;
	//SQL文
	private String sql;
	//控件属性
	private String controlProp;
	/**
	 * 构造函数
	 */
	public CellPropertyBean() {
		
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getNoField() {
		return noField;
	}
	public void setNoField(String noField) {
		this.noField = noField;
	}
	public String getNameField() {
		return nameField;
	}
	public void setNameField(String nameField) {
		this.nameField = nameField;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReadonly() {
		return readonly;
	}
	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}
	public String getSave() {
		return save;
	}
	public void setSave(String save) {
		this.save = save;
	}
	public String getSaveName() {
		return saveName;
	}
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	public String getHidden() {
		return hidden;
	}
	public void setHidden(String hidden) {
		this.hidden = hidden;
	}
	public String getValidate() {
		return validate;
	}
	public void setValidate(String validate) {
		this.validate = validate;
	}
	public String getEventFunc() {
		return eventFunc;
	}
	public void setEventFunc(String eventFunc) {
		this.eventFunc = eventFunc;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getControlProp() {
		return controlProp;
	}
	public void setControlProp(String controlProp) {
		this.controlProp = controlProp;
	}
}
