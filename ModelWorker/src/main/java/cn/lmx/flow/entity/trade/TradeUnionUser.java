package cn.lmx.flow.entity.trade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 工会操作人员
 * @author yujx
 *
 */
@Entity(name="TradeUnionUser")
@Table(name="TradeUnionUser")
public class TradeUnionUser {
	//ID
	@Id
	@Column(name="Code")
	private String code;
	//名称
	@Column(name="Name")
	private String name;
	//所属工会
	@Column(name="TradeUnionCode")
	private String tradeUnionCode;
	//是否操作员
	@Column(name="InUsers")
	private String inUsers;
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
	public TradeUnionUser() {
		
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
	public String getTradeUnionCode() {
		return tradeUnionCode;
	}
	public void setTradeUnionCode(String tradeUnionCode) {
		this.tradeUnionCode = tradeUnionCode;
	}
	public String getInUsers() {
		return inUsers;
	}
	public void setInUsers(String inUsers) {
		this.inUsers = inUsers;
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
