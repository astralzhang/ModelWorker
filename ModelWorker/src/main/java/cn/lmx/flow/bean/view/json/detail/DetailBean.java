package cn.lmx.flow.bean.view.json.detail;

public class DetailBean {
	//显示标签
	private String label;
	//数据表
	private String table;
	//处理
	private ProcessBean process;
	/**
	 * 构造函数
	 */
	public DetailBean() {
		
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public ProcessBean getProcess() {
		return process;
	}
	public void setProcess(ProcessBean process) {
		this.process = process;
	}
}
