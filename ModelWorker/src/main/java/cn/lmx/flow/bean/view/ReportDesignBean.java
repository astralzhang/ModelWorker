package cn.lmx.flow.bean.view;

public class ReportDesignBean {
	private String id;
	//报表编码
	private String no;
	//报表名称
	private String name;
	//所属模块
	private String moduleNo;
	//画面脚本
	private String viewScript;
	//模板处理脚本
	private String templateScript;
	//状态
	private String status;
	//版本
	private String version;
	//发布版本
	private String publishVersion;
	/**
	 * 构造函数
	 */
	public ReportDesignBean() {
		
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
	public String getModuleNo() {
		return moduleNo;
	}
	public void setModuleNo(String moduleNo) {
		this.moduleNo = moduleNo;
	}
	public String getViewScript() {
		return viewScript;
	}
	public void setViewScript(String viewScript) {
		this.viewScript = viewScript;
	}
	public String getTemplateScript() {
		return templateScript;
	}
	public void setTemplateScript(String templateScript) {
		this.templateScript = templateScript;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPublishVersion() {
		return publishVersion;
	}
	public void setPublishVersion(String publishVersion) {
		this.publishVersion = publishVersion;
	}
}
