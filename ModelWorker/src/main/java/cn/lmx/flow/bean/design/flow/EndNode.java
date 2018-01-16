package cn.lmx.flow.bean.design.flow;

import java.util.ArrayList;
import java.util.List;

public class EndNode extends Node {
	//父节点列表
	private List<Node> parents;
	/**
	 * 构造函数
	 */
	public EndNode() {
		parents = new ArrayList<Node>();
	}
	public final List<Node> getParents() {
		return parents;
	}
	public final void setParent(List<Node> parents) {
		this.parents = parents;
	}
	public final void addParent(Node node) {
		parents.add(node);
	}
}
