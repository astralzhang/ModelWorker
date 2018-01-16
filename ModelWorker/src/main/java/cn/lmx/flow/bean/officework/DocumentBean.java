package cn.lmx.flow.bean.officework;

public class DocumentBean {
	//ID
	private String id;
	//标题
	private String title;
	//正文
	private String description;
	//公文类型
	private String infoType;
	/**
	 * 构造函数
	 */
	public DocumentBean() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
}
