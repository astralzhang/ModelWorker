package cn.lmx.flow.bean.view.json.sync;

import java.util.ArrayList;
import java.util.List;

public class SyncSet {
	//数据源
	private List<Source> source;
	//导入表设定信息
	private List<Table> table;
	//SQL或存储过程
	private List<SqlProc> proc;
	/**
	 * 构造函数
	 */
	public SyncSet() {
		source = new ArrayList<Source>();
		table = new ArrayList<Table>();
		proc = new ArrayList<SqlProc>();
	}
	public List<Source> getSource() {
		return source;
	}
	public void setSource(List<Source> source) {
		this.source = source;
	}
	public List<Table> getTable() {
		return table;
	}
	public void setTable(List<Table> table) {
		this.table = table;
	}
	public List<SqlProc> getProc() {
		return proc;
	}
	public void setProc(List<SqlProc> proc) {
		this.proc = proc;
	}
}
