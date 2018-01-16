package cn.lmx.flow.entity.trade;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 积分管理表
 * @author yujx
 *
 */
@Entity(name="PointManager")
@Table(name="PointManager")
public class PointManager {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//信息类型
	@Column(name="InfoType")
	private String infoType;
	//采用类别
	@Column(name="AcceptType")
	private String acceptType;
	//杂志编码
	@Column(name="MagazineCode")
	private String magazineCode;
	//积分
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
	public PointManager() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getAcceptType() {
		return acceptType;
	}
	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}
	public String getMagazineCode() {
		return magazineCode;
	}
	public void setMagazineCode(String magazineCode) {
		this.magazineCode = magazineCode;
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
