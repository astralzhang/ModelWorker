package cn.lmx.flow.entity.trade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 工会信息
 * @author yujx
 *
 */
@Entity(name="TradeUnionInfo")
@Table(name="TradeUnionInfo")
public class TradeUnionInfo {
	//ID
	@Id
	@Column(name="Code")
	private String code;
	//名称
	@Column(name="Name")
	private String name;
	//上级工会编码
	@Column(name="ParentCode")
	private String parentCode;
	//工会级别
	@Column(name="TradeLevel")
	private String tradeLevel;
	//工会类别
	@Column(name="TradeType")
	private String tradeType;
	//虚拟上级工会编码
	@Column(name="VirtualParentCode")
	private String virtualParentCode;
	//显示顺序
	@Column(name="ShowOrder")
	private String showOrder;
	//是否只显示虚拟工会
	@Column(name="ShowVirtual")
	private String showVirtual;
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
	public TradeUnionInfo() {
		
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getTradeLevel() {
		return tradeLevel;
	}
	public void setTradeLevel(String tradeLevel) {
		this.tradeLevel = tradeLevel;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getVirtualParentCode() {
		return virtualParentCode;
	}
	public void setVirtualParentCode(String virtualParentCode) {
		this.virtualParentCode = virtualParentCode;
	}
	public String getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}
	public String getShowVirtual() {
		return showVirtual;
	}
	public void setShowVirtual(String showVirtual) {
		this.showVirtual = showVirtual;
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
