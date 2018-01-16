package cn.lmx.flow.bean.view.json.input;

public class Text {
	//文件编码
	private String encode;
	//标题行号
	private int title;
	//数据起始行号
	private int startRow;
	//项目区分符号
	private String separator;
	/**
	 * 构造函数
	 */
	public Text() {
		
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
	public int getTitle() {
		return title;
	}
	public void setTitle(int title) {
		this.title = title;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
}
