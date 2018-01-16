package cn.lmx.flow.bean.flow;

import java.util.ArrayList;
import java.util.List;

import cn.lmx.flow.bean.SQL.SqlProcBean;

public class DatabaseBean {
	private List<SqlProcBean> list;
	/**
	 * 构造函数
	 */
	public DatabaseBean() {
		list = new ArrayList<SqlProcBean>();
	}
	public List<SqlProcBean> getList() {
		return list;
	}
	public void setList(List<SqlProcBean> list) {
		this.list = list;
	}
	public void add(SqlProcBean bean) {
		if (list == null) {
			list = new ArrayList<SqlProcBean>();
		}
		list.add(bean);
	}
}
