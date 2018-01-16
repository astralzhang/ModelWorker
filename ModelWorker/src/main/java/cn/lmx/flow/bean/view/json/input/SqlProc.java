package cn.lmx.flow.bean.view.json.input;

import java.util.ArrayList;
import java.util.List;

public class SqlProc {
	//目标数据库
	private String target;
	//运行时点
	private String runTime;
	//SQL或存储过程
	private String proc;
	//参数
	private List<Parameter> parameter;
	/**
	 * 构造函数
	 */
	public SqlProc() {
		parameter = new ArrayList<Parameter>();
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getProc() {
		return proc;
	}
	public void setProc(String proc) {
		this.proc = proc;
	}
	public List<Parameter> getParameter() {
		return parameter;
	}
	public void setParameter(List<Parameter> parameter) {
		this.parameter = parameter;
	}
}
