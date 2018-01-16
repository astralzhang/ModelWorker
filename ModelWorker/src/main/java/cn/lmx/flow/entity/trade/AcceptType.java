package cn.lmx.flow.entity.trade;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 采用类别
 * @author yujx
 *
 */
@Entity(name="AcceptType")
@Table(name="AcceptType")
public class AcceptType {
	//ID
	@Id
	@Column(name="Code")
	private String code;
	//名称
	@Column(name="Name")
	private String name;
	//所属期刊
	@Column(name="MagazineCode")
	private String magazineCode;
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
	public AcceptType() {
		
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
	public String getMagazineCode() {
		return magazineCode;
	}
	public void setMagazineCode(String magazineCode) {
		this.magazineCode = magazineCode;
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
