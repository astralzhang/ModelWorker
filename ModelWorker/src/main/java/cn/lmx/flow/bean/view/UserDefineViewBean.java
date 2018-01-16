package cn.lmx.flow.bean.view;

public class UserDefineViewBean {
	//ID
	private String id;
	//编码
	private String no;
	//名称
	private String name;
	//版本
	private String version;
	//发布版本号
	private String publishVersion;
	//单据类型
	private String voucherType;
	//单据类型名称
	private String voucherTypeName;
	//状态
	private String status;
	//列表脚本
	private String listScript;
	//Head脚本
	private String headScript;
	//明细脚本
	private String detailScript;
	/**
	 * 构造函数
	 */
	public UserDefineViewBean() {
		
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
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherTypeName() {
		return voucherTypeName;
	}
	public void setVoucherTypeName(String voucherTypeName) {
		this.voucherTypeName = voucherTypeName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getListScript() {
		return listScript;
	}
	public void setListScript(String listScript) {
		this.listScript = listScript;
	}
	public String getHeadScript() {
		return headScript;
	}
	public void setHeadScript(String headScript) {
		this.headScript = headScript;
	}
	public String getDetailScript() {
		return detailScript;
	}
	public void setDetailScript(String detailScript) {
		this.detailScript = detailScript;
	}
}
