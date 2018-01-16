package cn.lmx.flow.bean.data;
import java.util.List;
import java.util.ArrayList;
public class SyncGroupBean {
	//组编码
	private String no;
	//组名称
	private String name;
	//导入列表
	private List<SyncBean> syncList;
	/**
	 * 构造函数
	 */
	public SyncGroupBean() {
		syncList = new ArrayList<SyncBean>();
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SyncBean> getSyncList() {
		return syncList;
	}
	public void setSyncList(List<SyncBean> syncList) {
		this.syncList = syncList;
	}
}
