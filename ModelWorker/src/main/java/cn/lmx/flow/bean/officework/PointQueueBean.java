package cn.lmx.flow.bean.officework;

import java.math.BigDecimal;

public class PointQueueBean {
	//工会编码
	private String tradeUnionCode;
	//工会名称
	private String tradeUnionName;
	//父级虚拟工会编码
	private String virtualUnionCode;
	//父级虚拟工会名称
	private String virtualUnionName;
	//是否只显示虚拟工会
	private String showVirtual;
	//积分
	private BigDecimal point;
	/**
	 * 构造函数
	 */
	public PointQueueBean() {
		
	}
	public String getTradeUnionCode() {
		return tradeUnionCode;
	}
	public void setTradeUnionCode(String tradeUnionCode) {
		this.tradeUnionCode = tradeUnionCode;
	}
	public String getTradeUnionName() {
		return tradeUnionName;
	}
	public void setTradeUnionName(String tradeUnionName) {
		this.tradeUnionName = tradeUnionName;
	}
	public String getVirtualUnionCode() {
		return virtualUnionCode;
	}
	public void setVirtualUnionCode(String virtualUnionCode) {
		this.virtualUnionCode = virtualUnionCode;
	}
	public String getVirtualUnionName() {
		return virtualUnionName;
	}
	public void setVirtualUnionName(String virtualUnionName) {
		this.virtualUnionName = virtualUnionName;
	}
	public String getShowVirtual() {
		return showVirtual;
	}
	public void setShowVirtual(String showVirtual) {
		this.showVirtual = showVirtual;
	}
	public BigDecimal getPoint() {
		return point;
	}
	public void setPoint(BigDecimal point) {
		this.point = point;
	}
}
