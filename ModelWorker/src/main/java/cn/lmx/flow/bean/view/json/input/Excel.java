package cn.lmx.flow.bean.view.json.input;

public class Excel {
	//表头起始行
	private int headStartRowNo;
	//表头结束行
	private int headEndRowNo;
	//标题起始行
	private int titleStartRowNo;
	//标题结束行
	private int titleEndRowNo;
	//明细起始行
	private int detailStartRowNo;
	/**
	 * 构造函数
	 */
	public Excel() {
		
	}
	public int getHeadStartRowNo() {
		return headStartRowNo;
	}
	public void setHeadStartRowNo(int headStartRowNo) {
		this.headStartRowNo = headStartRowNo;
	}
	public int getHeadEndRowNo() {
		return headEndRowNo;
	}
	public void setHeadEndRowNo(int headEndRowNo) {
		this.headEndRowNo = headEndRowNo;
	}
	public int getTitleStartRowNo() {
		return titleStartRowNo;
	}
	public void setTitleStartRowNo(int titleStartRowNo) {
		this.titleStartRowNo = titleStartRowNo;
	}
	public int getTitleEndRowNo() {
		return titleEndRowNo;
	}
	public void setTitleEndRowNo(int titleEndRowNo) {
		this.titleEndRowNo = titleEndRowNo;
	}
	public int getDetailStartRowNo() {
		return detailStartRowNo;
	}
	public void setDetailStartRowNo(int detailStartRowNo) {
		this.detailStartRowNo = detailStartRowNo;
	}
}
