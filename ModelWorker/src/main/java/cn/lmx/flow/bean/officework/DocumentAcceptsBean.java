package cn.lmx.flow.bean.officework;

import java.math.BigDecimal;
import java.util.List;

/**
 * 公文采用状况
 * @author yujx
 *
 */
public class DocumentAcceptsBean {
	//ID
	private String id;
	//公文ID
	private String parentId;
	//类别编码
	private String code;
	//采用级别
	private String levelCode;
	//采用级别名称
	private String levelName;
	//采用期刊
	private String magazineCode;
	//采用期刊名称
	private String magazineName;
	//采用类型
	private String acceptType;
	//采用类型名称
	private String acceptTypeName;
	//状态
	private String status;
	//总期数
	private BigDecimal periods;
	//杂志年份
	private String magazineYear;
	//采用期次
	private BigDecimal period;
	//创建者
	private String createUser;
	//创建日期
	private String createTime;
	//修改者
	private String updateUser;
	//修改日期
	private String updateTime;
	//信息类型
	private String infoType;
	//信息类型名称
	private String infoTypeName;
	//积分
	private BigDecimal point;
	//list
	List<DocumentAcceptsBean> documentAcceptsList;
	/**
	 * 构造函数
	 */
	public DocumentAcceptsBean() {
		
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public List<DocumentAcceptsBean> getDocumentAcceptsList() {
		return documentAcceptsList;
	}
	public void setDocumentAcceptsList(List<DocumentAcceptsBean> documentAcceptsList) {
		this.documentAcceptsList = documentAcceptsList;
	}
	public String getMagazineYear() {
		return magazineYear;
	}
	public void setMagazineYear(String magazineYear) {
		this.magazineYear = magazineYear;
	}
	public BigDecimal getPeriods() {
		return periods;
	}
	public void setPeriods(BigDecimal periods) {
		this.periods = periods;
	}
	public BigDecimal getPeriod() {
		return period;
	}
	public void setPeriod(BigDecimal period) {
		this.period = period;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getInfoTypeName() {
		return infoTypeName;
	}
	public void setInfoTypeName(String infoTypeName) {
		this.infoTypeName = infoTypeName;
	}
	public BigDecimal getPoint() {
		return point;
	}
	public void setPoint(BigDecimal point) {
		this.point = point;
	}
}
