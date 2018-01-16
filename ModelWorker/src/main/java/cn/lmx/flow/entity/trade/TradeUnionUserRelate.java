package cn.lmx.flow.entity.trade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 可操作工会
 * @author yujx
 *
 */
@Entity(name="TradeUnionUserRelate")
@Table(name="TradeUnionUserRelate")
public class TradeUnionUserRelate {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//名称
	@Column(name="UserNo")
	private String userNo;
	//所属工会
	@Column(name="TradeUnionCode")
	private String tradeUnionCode;
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
	public TradeUnionUserRelate() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getTradeUnionCode() {
		return tradeUnionCode;
	}
	public void setTradeUnionCode(String tradeUnionCode) {
		this.tradeUnionCode = tradeUnionCode;
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
