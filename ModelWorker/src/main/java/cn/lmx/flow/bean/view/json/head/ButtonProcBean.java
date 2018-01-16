package cn.lmx.flow.bean.view.json.head;

import java.util.ArrayList;
import java.util.List;

public class ButtonProcBean {
	//存储过程
	private String proc;
	//目标数据库
	private String target;
	//处理类型
	private String process;
	//处理结束后的逻辑
	private String back;
	//数据映射
	private List<ProcMappingBean> mapping;
	/**
	 * 构造函数
	 */
	public ButtonProcBean() {
		mapping = new ArrayList<ProcMappingBean>();	
	}
	public String getProc() {
		return proc;
	}
	public void setProc(String proc) {
		this.proc = proc;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getBack() {
		return back;
	}
	public void setBack(String back) {
		this.back = back;
	}
	public List<ProcMappingBean> getMapping() {
		return mapping;
	}
	public void setMapping(List<ProcMappingBean> mapping) {
		this.mapping = mapping;
	}
}
