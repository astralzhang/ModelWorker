package cn.lmx.flow.entity.officework;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 公文采用状况
 * @author yujx
 *
 */
@Entity(name="DocumentAccepts")
@Table(name="DocumentAccepts")
public class DocumentAccepts {
	//ID
	@Id
	@Column(name="ID")
	private String id;
	//公文ID
	@Column(name="ParentId")
	private String parentId;
	//类别编码
	@Column(name="Code")
	private String code;
	//采用级别
	@Column(name="LevelCode")
	private String levelCode;
	//采用级别名称
	@Column(name="LevelName")
	private String levelName;
	//采用期刊
	@Column(name="MagazineCode")
	private String magazineCode;
	//采用期刊名称
	@Column(name="MagazineName")
	private String magazineName;
	//采用类型
	@Column(name="AcceptType")
	private String acceptType;
	//采用类型名称
	@Column(name="AcceptTypeName")
	private String acceptTypeName;
	//采用日期
	@Column(name="AcceptDate")
	private String acceptDate;
	//采用期刊年
	@Column(name="MagazineYear")
	private String magazineYear;
	//采用期次
	@Column(name="Period")
	private BigDecimal period;
	//状态
	@Column(name="Status")
	private String status;
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
	public DocumentAccepts() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getMagazineCode() {
		return magazineCode;
	}
	public void setMagazineCode(String magazineCode) {
		this.magazineCode = magazineCode;
	}
	public String getMagazineName() {
		return magazineName;
	}
	public void setMagazineName(String magazineName) {
		this.magazineName = magazineName;
	}
	public String getAcceptType() {
		return acceptType;
	}
	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}
	public String getAcceptTypeName() {
		return acceptTypeName;
	}
	public void setAcceptTypeName(String acceptTypeName) {
		this.acceptTypeName = acceptTypeName;
	}
	public String getMagazineYear() {
		return magazineYear;
	}
	public void setMagazineYear(String magazineYear) {
		this.magazineYear = magazineYear;
	}
	public BigDecimal getPeriod() {
		return period;
	}
	public void setPeriod(BigDecimal period) {
		this.period = period;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAcceptDate() {
		return acceptDate;
	}
	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
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
