package cn.lmx.flow.bean.view.json.head;

public class CellBean {
	//行号
	private int row;
	//列号
	private int col;
	//类型
	private String type;
	//属性
	private CellPropertyBean props;
	/**
	 * 构造函数
	 */
	public CellBean() {
		
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public CellPropertyBean getProps() {
		return props;
	}
	public void setProps(CellPropertyBean props) {
		this.props = props;
	}
}
