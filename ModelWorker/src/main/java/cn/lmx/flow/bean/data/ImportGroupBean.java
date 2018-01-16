package cn.lmx.flow.bean.data;
import java.util.List;
import java.util.ArrayList;
public class ImportGroupBean {
	//组编码
	private String no;
	//组名称
	private String name;
	//导入列表
	private List<ImportBean> importList;
	/**
	 * 构造函数
	 */
	public ImportGroupBean() {
		importList = new ArrayList<ImportBean>();
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
	public List<ImportBean> getImportList() {
		return importList;
	}
	public void setImportList(List<ImportBean> importList) {
		this.importList = importList;
	}
}
