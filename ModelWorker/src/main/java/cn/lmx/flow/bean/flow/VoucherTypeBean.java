package cn.lmx.flow.bean.flow;

public class VoucherTypeBean {
	//编号
	private String no;
	//名称
	private String name;
	//描述
	private String description;
	/**
	 * 构造函数
	 */
	public VoucherTypeBean() {
		
	}
	public final String getNo() {
		return no;
	}
	public final void setNo(String no) {
		this.no = no;
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final String getDescription() {
		return description;
	}
	public final void setDescription(String description) {
		this.description = description;
	}
}
