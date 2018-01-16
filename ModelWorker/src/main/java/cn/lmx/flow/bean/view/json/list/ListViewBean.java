package cn.lmx.flow.bean.view.json.list;

import java.util.List;

import cn.lmx.flow.bean.view.json.common.ConditionBean;

public class ListViewBean {
	//sql类型
	private String type;
	//sql文
	private String sql;
	//目标数据库
	private String target;
	//执行时点
	private String runTime;
	//条件
	private List<ConditionBean> condition;
	//列表视图
	private List<ListFieldBean> view;
	/**
	 * 构造函数
	 */
	public ListViewBean() {
		
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getTarget() {
		return target;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<ConditionBean> getCondition() {
		return condition;
	}
	public void setCondition(List<ConditionBean> condition) {
		this.condition = condition;
	}
	public List<ListFieldBean> getView() {
		return view;
	}
	public void setView(List<ListFieldBean> view) {
		this.view = view;
	}
}
