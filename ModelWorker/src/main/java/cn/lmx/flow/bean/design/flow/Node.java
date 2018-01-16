package cn.lmx.flow.bean.design.flow;

public class Node {
	//节点ID
	private String id;
	//源节点ID
	private String firstId;
	//名称
	private String name;
	//父节点
	private Node parent;
	//子节点
	private Node child;
	/**
	 * 构造函数
	 */
	public Node() {
		parent = null;
		child = null;
	}
	public final String getId() {
		return id;
	}
	public final void setId(String id) {
		this.id = id;
	}
	public final String getFirstId() {
		return firstId;
	}
	public final void setFirstId(String firstId) {
		this.firstId = firstId;
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final Node getParent() {
		return parent;
	}
	public final void setParent(Node parent) {
		this.parent = parent;
	}
	public final Node getChild() {
		return child;
	}
	public final void setChild(Node child) {
		this.child = child;
	}
}
