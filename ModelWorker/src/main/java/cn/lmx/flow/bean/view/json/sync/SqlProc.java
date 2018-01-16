package cn.lmx.flow.bean.view.json.sync;

import java.util.ArrayList;
import java.util.List;

public class SqlProc {
	//目标数据库
	private String target;
	//运行时点
	private String runTime;
	//SQL或存储过程
	private String proc;
	//编码
	private String no;
	//处理方式（update、select)
	private String type;
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
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
