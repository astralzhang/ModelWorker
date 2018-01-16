package cn.lmx.flow.bean.view.json.input;

import java.util.ArrayList;
import java.util.List;

public class SpaceRow {
	//处理方法
	private String method;
	//目标数据库
	private String target;
	//参数
	private List<Parameter> parameter;
	//存储过程
	private String proc;
	/**
	 * 构造函数
	 */
	public SpaceRow() {
		parameter = new ArrayList<Parameter>();
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<Parameter> getParameter() {
		return parameter;
	}
	public void setParameter(List<Parameter> parameter) {
		this.parameter = parameter;
	}
	public String getProc() {
		return proc;
	}
	public void setProc(String proc) {
		this.proc = proc;
	}
}
