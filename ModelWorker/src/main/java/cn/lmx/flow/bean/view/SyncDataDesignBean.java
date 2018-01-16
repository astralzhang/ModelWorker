package cn.lmx.flow.bean.view;

public class SyncDataDesignBean {
	private String id;
	//编码
	private String no;
	//名称
	private String name;
	//模式
	private String model;
	//脚本
	private String script;
	//设计版本
	private String designVersion;
	//发布版本
	private String publishVersion;
	//状态
	private String status;
	/**
	 * 构造函数
	 */
	public SyncDataDesignBean() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getDesignVersion() {
		return designVersion;
	}
	public void setDesignVersion(String designVersion) {
		this.designVersion = designVersion;
	}
	public String getPublishVersion() {
		return publishVersion;
	}
	public void setPublishVersion(String publishVersion) {
		this.publishVersion = publishVersion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
