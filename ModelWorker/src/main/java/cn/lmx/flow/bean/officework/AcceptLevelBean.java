package cn.lmx.flow.bean.officework;

import java.util.ArrayList;
import java.util.List;

public class AcceptLevelBean {
	//编码
	private String code;
	//名称
	private String name;
	//杂志数
	private int count;
	//杂志列表
	private List<MagazineBean> magazineList;
	/**
	 * 构造函数
	 */
	public AcceptLevelBean() {
		magazineList = new ArrayList<MagazineBean>();
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<MagazineBean> getMagazineList() {
		return magazineList;
	}
	public void setMagazineList(List<MagazineBean> magazineList) {
		this.magazineList = magazineList;
	}
}
