package cn.lmx.flow.bean.officework;

import java.math.BigDecimal;
/**
 * 工会扣分Bean
 * @author yujx
 *
 */
public class DeductInfoBean {
	//ID
	private String id;
	//工会编码
	private String tradeCode;
	//工会名称
	private String tradeName;
	//扣分日期
	private String deductDate;
	//扣分值
	private BigDecimal deductPoint;
	//扣分年
	private String deductYear;
	//扣分原因
	private String deductCause;
	/**
	 * 构造函数
	 */
	public DeductInfoBean() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getDeductDate() {
		return deductDate;
	}
	public void setDeductDate(String deductDate) {
		this.deductDate = deductDate;
	}
	public BigDecimal getDeductPoint() {
		return deductPoint;
	}
	public void setDeductPoint(BigDecimal deductPoint) {
		this.deductPoint = deductPoint;
	}
	public String getDeductYear() {
		return deductYear;
	}
	public void setDeductYear(String deductYear) {
		this.deductYear = deductYear;
	}
	public String getDeductCause() {
		return deductCause;
	}
	public void setDeductCause(String deductCause) {
		this.deductCause = deductCause;
	}
}
