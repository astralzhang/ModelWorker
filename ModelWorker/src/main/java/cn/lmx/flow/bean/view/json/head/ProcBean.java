package cn.lmx.flow.bean.view.json.head;

public class ProcBean {
	//目标数据库
	private String target;
	//运行时点
	private String runTime;
	//参数类型
	private String paramType;
	//明细表
	private String table;
	//存储过程
	private String proc;
	/**
	 * 构造函数
	 */
	public ProcBean() {
		
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
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getProc() {
		return proc;
	}
	public void setProc(String proc) {
		this.proc = proc;
	}
}
