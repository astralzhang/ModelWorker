package cn.lmx.flow.bean.view.json.head;

public class MarginBean {
	//起始单元格
	private CellBean start;
	//结束单元格
	private CellBean end;
	/**
	 * 构造函数
	 */
	public MarginBean() {
		
	}
	public CellBean getStart() {
		return start;
	}
	public void setStart(CellBean start) {
		this.start = start;
	}
	public CellBean getEnd() {
		return end;
	}
	public void setEnd(CellBean end) {
		this.end = end;
	}
}
