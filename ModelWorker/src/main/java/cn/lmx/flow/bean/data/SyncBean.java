package cn.lmx.flow.bean.data;

public class SyncBean {
	//导入编码
	private String no;
	//导入名称
	private String name;
	//导入模式
	private String model;
	//所属组
	private String groupNo;
	//脚本
	private String script;
	//版本
	private String version;
	/**
	 * 构造函数
	 */
	public SyncBean() {
		
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
