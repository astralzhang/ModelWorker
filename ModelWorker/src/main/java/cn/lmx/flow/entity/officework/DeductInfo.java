package cn.lmx.flow.entity.officework;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 扣分处理
 * @author yujx
 *
 */
@Entity(name="DeductInfo")
@Table(name="DeductInfo")
public class DeductInfo {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//工会编码
	@Column(name="TradeCode")
	private String tradeCode;
	//扣分日期
	@Column(name="DeductDate")
	private String deductDate;
	//扣分值
	@Column(name="DeductPoint")
	private BigDecimal deductPoint;
	//扣分年
	@Column(name="DeductYear")
	private String deductYear;
	//扣分原因
	@Column(name="DeductCause")
	private String deductCause;
	//创建者
	@Column(name="CreateUser")
	private String createUser;
	//创建日期
	@Column(name="CreateTime")
	private String createTime;
	//修改者
	@Column(name="UpdateUser")
	private String updateUser;
	//修改日期
	@Column(name="UpdateTime")
	private String updateTime;
	/**
	 * 构造函数
	 */
	public DeductInfo() {
		
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
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
