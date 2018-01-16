package cn.lmx.flow.bean.report.json;

import java.util.ArrayList;
import java.util.List;

public class Linker {
	//链接字段
	private String field;
	//链接参数
	private List<UrlParameter> param;
	/**
	 * 构造函数
	 */
	public Linker() {
		param = new ArrayList<UrlParameter>();
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public List<UrlParameter> getParam() {
		return param;
	}
	public void setParam(List<UrlParameter> param) {
		this.param = param;
	}
}
