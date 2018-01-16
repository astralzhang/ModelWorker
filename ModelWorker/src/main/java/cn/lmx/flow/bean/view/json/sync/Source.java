package cn.lmx.flow.bean.view.json.sync;

public class Source {
	//源编码
	private String no;
	//sql执行目标
	private String target;
	//SQL文或存储过程
	private String sql;
	//是否主数据源
	private String master;
	/**
	 * 构造函数
	 */
	public Source() {
		
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
}
