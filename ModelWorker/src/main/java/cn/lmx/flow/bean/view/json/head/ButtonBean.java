package cn.lmx.flow.bean.view.json.head;

import java.util.ArrayList;
import java.util.List;

public class ButtonBean {
	//按钮ID
	private String id;
	//按钮名称
	private String name;
	//位置参照按钮
	private String button;
	//位置类型
	private String position;
	//存储过程处理一栏
	private List<ButtonProcBean> proc;
	/**
	 * 构造函数
	 */
	public ButtonBean() {
		proc = new ArrayList<ButtonProcBean>();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public List<ButtonProcBean> getProc() {
		return proc;
	}
	public void setProc(List<ButtonProcBean> proc) {
		this.proc = proc;
	}
}
