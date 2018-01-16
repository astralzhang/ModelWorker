package cn.lmx.flow.entity.trade;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 采用期刊
 * @author yujx
 *
 */
@Entity(name="Magazines")
@Table(name="Magazines")
public class Magazines {
	//ID
	@Id
	@Column(name="Code")
	private String code;
	//名称
	@Column(name="Name")
	private String name;
	//级别
	@Column(name="LevelCode")
	private String levelCode;
	//总期数
	@Column(name="Periods")
	private BigDecimal periods;
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
	public Magazines() {
		
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
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public BigDecimal getPeriods() {
		return periods;
	}
	public void setPeriods(BigDecimal periods) {
		this.periods = periods;
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
