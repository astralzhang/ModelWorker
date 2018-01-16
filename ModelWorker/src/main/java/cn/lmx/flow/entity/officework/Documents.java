package cn.lmx.flow.entity.officework;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 公文信息
 * @author yujx
 *
 */
@Entity(name="Documents")
@Table(name="Documents")
public class Documents {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//标题
	@Column(name="Title")
	private String title;
	//上报单位
	@Column(name="TradeUnionCode")
	private String tradeUnionCode;
	//上报单位名称
	@Column(name="TradeUnionName")
	private String tradeUnionName;
	//信息类型
	@Column(name="InfoType")
	private String infoType;
	//上报日期
	@Column(name="SubmitDate")
	private String submitDate;
	//是否必报
	@Column(name="MustSubmit")
	private String mustSubmit;
	//公文描述
	@Column(name="Description")
	private String description;
	//当前处理单位
	@Column(name="CurrentTradeUnionCode")
	private String currentTradeUnionCode;
	//上报状态
	@Column(name="SubmitStatus")
	private String submitStatus;
	//采用状态
	@Column(name="AcceptStatus")
	private String acceptStatus;
	//获得积分
	@Column(name="Point")
	private BigDecimal point;
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
	public Documents() {
		
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
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(String submitDate) {
		this.submitDate = submitDate;
	}
	public String getMustSubmit() {
		return mustSubmit;
	}
	public void setMustSubmit(String mustSubmit) {
		this.mustSubmit = mustSubmit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCurrentTradeUnionCode() {
		return currentTradeUnionCode;
	}
	public void setCurrentTradeUnionCode(String currentTradeUnionCode) {
		this.currentTradeUnionCode = currentTradeUnionCode;
	}
	public String getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}
	public String getAcceptStatus() {
		return acceptStatus;
	}
	public void setAcceptStatus(String acceptStatus) {
		this.acceptStatus = acceptStatus;
	}
	public BigDecimal getPoint() {
		return point;
	}
	public void setPoint(BigDecimal point) {
		this.point = point;
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
