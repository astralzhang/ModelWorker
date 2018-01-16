package cn.lmx.flow.bean.design.flow;

public class ConditionNode extends Node {
	//节点描述
	private String desc;
	//条件
	private String condition;
	//Yes子节点
	private Node yesChild;
	//No子节点
	private Node noChild;
	/**
	 * 构造函数
	 */
	public ConditionNode() {
		yesChild = null;
		noChild = null;
	}
	public final String getDesc() {
		return desc;
	}
	public final void setDesc(String desc) {
		this.desc = desc;
	}
	public final String getCondition() {
		return condition;
	}
	public final void setCondition(String condition) {
		this.condition = condition;
	}
	public final Node getYesChild() {
		return yesChild;
	}
	public final void setYesChild(Node yesChild) {
		this.yesChild = yesChild;
	}
	public final Node getNoChild() {
		return noChild;
	}
	public final void setNoChild(Node noChild) {
		this.noChild = noChild;
	}
}
