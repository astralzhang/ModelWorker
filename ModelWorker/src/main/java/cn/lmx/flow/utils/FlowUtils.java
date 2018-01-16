package cn.lmx.flow.utils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.lmx.flow.bean.design.flow.AuditNode;
import cn.lmx.flow.bean.design.flow.ConditionNode;
import cn.lmx.flow.bean.design.flow.EndNode;
import cn.lmx.flow.bean.design.flow.Node;
import cn.lmx.flow.bean.design.json.Flow;
import cn.lmx.flow.bean.design.json.Path;
import cn.lmx.flow.bean.design.json.Property;
import cn.lmx.flow.bean.design.json.Task;

public class FlowUtils {
	/**
	 * 创建节点列表
	 * @param flow
	 * @return
	 * @throws Exception
	 */
	public static List<Node> createFlowTree(Flow flow) throws Exception {
		Node startNode = new Node();
		EndNode endNode = new EndNode();
		Map<String, Task> taskMap = flow.getStates();
		if (taskMap == null || taskMap.size() <= 0) {
			throw new Exception("没有节点！");
		}
		Iterator<Entry<String, Task>> it = taskMap.entrySet().iterator();
		int iStarts = 0;
		int iEnds = 0;
		List<Node> list = new ArrayList<Node>();
		while (it.hasNext()) {
			Entry<String, Task> entry = (Entry<String, Task>)it.next();
			Task task = entry.getValue();
			if ("start".equals(task.getType())) {
				//起始节点
				startNode.setId(entry.getKey());
				startNode.setName(task.getText().getText());
				startNode.setFirstId(task.getProps().get("id") != null ? task.getProps().get("id").getValue() : entry.getKey());
				if (startNode.getFirstId() == null || "".equals(startNode.getFirstId())) {
					startNode.setFirstId(entry.getKey());
				}
				list.add(0, startNode);
				iStarts++;
				continue;
			}
			if ("end".equals(task.getType())) {
				//结束节点
				endNode.setId(entry.getKey());
				endNode.setName(task.getText().getText());
				endNode.setFirstId(task.getProps().get("id") != null ? task.getProps().get("id").getValue() : entry.getKey());
				if (endNode.getFirstId() == null || "".equals(endNode.getFirstId())) {
					endNode.setFirstId(entry.getKey());
				}
				list.add(endNode);
				iEnds++;
				continue;
			}
			if ("task".equals(task.getType())) {
				//审核节点
				AuditNode auditNode = new AuditNode();
				auditNode.setId(entry.getKey());
				auditNode.setName(task.getText().getText());
				auditNode.setFirstId(task.getProps().get("id") != null ? task.getProps().get("id").getValue() : entry.getKey());
				if (auditNode.getFirstId() == null || "".equals(auditNode.getFirstId())) {
					auditNode.setFirstId(entry.getKey());
				}
				Map<String, Property> props = task.getProps();
				auditNode.setDesc(props.get("desc") != null ? props.get("desc").getValue() : "");
				auditNode.setAuditUserCondition(props.get("processor") != null ? props.get("processor").getValue() : "");
				auditNode.setAuditProcessSql(props.get("sql") != null ? URLDecoder.decode(props.get("sql").getValue(), "UTF-8") : "");
				list.add(auditNode);
				continue;
			}
			if ("condition".equals(task.getType())) {
				//条件节点
				ConditionNode conditionNode = new ConditionNode();
				conditionNode.setId(entry.getKey());
				conditionNode.setName(task.getText().getText());
				conditionNode.setFirstId(task.getProps().get("id") != null ? task.getProps().get("id").getValue() : entry.getKey());
				if (conditionNode.getFirstId() == null || "".equals(conditionNode.getFirstId())) {
					conditionNode.setFirstId(entry.getKey());
				}
				Map<String, Property> props = task.getProps();
				conditionNode.setDesc(props.get("desc") != null ? props.get("desc").getValue() : "");
				conditionNode.setCondition(props.get("condition") != null ? props.get("condition").getValue() : "");
				list.add(conditionNode);
				continue;
			}
		}
		if (iStarts > 1) {
			throw new Exception("不能存在多个起始节点！");
		}
		if (iEnds > 1) {
			throw new Exception("不能存在多个结束节点！");
		}
		Map<String, Path> pathMap = flow.getPaths();
		Iterator<Entry<String, Path>> pathIt = pathMap.entrySet().iterator();
		while (pathIt.hasNext()) {
			Entry<String, Path> entry = (Entry<String, Path>)pathIt.next();
			Path path = entry.getValue();
			String fromId = path.getFrom();
			String toId = path.getTo();
			Node fromNode = null;
			Node toNode = null;
			for (int i = 0; i < list.size(); i++) {
				Node node = list.get(i);
				if (fromId.equals(node.getId())) {
					fromNode = node;
					if (toNode != null) {
						break;
					}
					continue;
				}
				if (toId.equals(node.getId())) {
					toNode = node;
					if (fromNode != null) {
						break;
					}
					continue;
				}
			}
			if (fromNode == null) {
				throw new Exception("节点[" + fromId + "]不存在！");
			}
			if (toNode == null) {
				throw new Exception("节点[" + toId + "]不存在！");
			}
			if (!(toNode instanceof EndNode)) {
				if (toNode.getParent() != null) {
					throw new Exception("节点[" + toNode.getFirstId() + "]不可以存在多个父节点！");
				}
			}
			if (fromNode.getId().equals(startNode.getId())) {
				//start节点
				if (startNode.getChild() != null) {
					throw new Exception("起始节点下不可以存在多个子节点！");
				}
				startNode.setChild(toNode);
			}
			if (toNode instanceof EndNode) {
				//设定父节点
				((EndNode)toNode).addParent(fromNode);
			} else {
				toNode.setParent(fromNode);
			}
			if (fromNode instanceof EndNode) {
				//End节点
				throw new Exception("结束节点后不可以存在子节点！");
			}
			if (fromNode instanceof AuditNode) {
				//审核节点
				AuditNode auditNode = (AuditNode)fromNode;
				if (auditNode.getChild() != null) {
					throw new Exception("节点[" + auditNode.getFirstId() + "]不可以存在多个子节点！");
				}
				auditNode.setChild(toNode);
				continue;
			}
			if (fromNode instanceof ConditionNode) {
				//条件节点
				ConditionNode conditionNode = (ConditionNode)fromNode;
				String type = path.getProps().get("yesorno") != null ? path.getProps().get("yesorno").getValue() : "N";
				if ("N".equals(type)) {
					//NO
					if (conditionNode.getNoChild() != null) {
						throw new Exception("条件节点[" + conditionNode.getFirstId() + "]不可以存在多个No连接线！");
					}
					conditionNode.setNoChild(toNode);
					continue;
				} else if ("Y".equals(type)) {
					//Yes
					if (conditionNode.getYesChild() != null) {
						throw new Exception("条件节点[" + conditionNode.getFirstId() + "]不可以存在多个Yes连接线！");
					}
					conditionNode.setYesChild(toNode);
					continue;
				} else {
					throw new Exception("条件节点[" + conditionNode.getFirstId() + "]的连接线设定不正确！");
				}
			}
		}
		//check所有节点的连接状态
		boolean isConStart = false;
		for (int i = 0; i < list.size(); i++) {
			Node node = list.get(i);
			if (node.getId().equals(startNode.getId())) {
				//start节点
				if (node.getChild() == null) {
					throw new Exception("开始节点必须存在子节点！");
				}
				continue;
			}
			if (node instanceof EndNode) {
				//end节点
				if (((EndNode) node).getParents() == null || ((EndNode)node).getParents().size() <= 0) {
					throw new Exception("必须有节点指向结束节点！");
				}
				List<Node> parents = ((EndNode) node).getParents();
				for (int j = 0; j < parents.size(); j++) {
					Node parentNode = parents.get(j);
					if (parentNode.getId().equals(startNode.getId())) {
						throw new Exception("起始节点和结束节点之间必须存在处理节点！");
					}
				}
				continue;
			}
			if (node instanceof AuditNode) {
				//审核节点
				if (node.getParent() == null) {
					throw new Exception("审核节点[" + node.getFirstId() + "]必须存在父节点！");
				}
				if (node.getParent().getId().equals(startNode.getId())) {
					isConStart = true;
				}
				if (((AuditNode) node).getChild() == null) {
					throw new Exception("审核节点[" + node.getFirstId() + "]必须存在子节点!");
				}
				continue;
			}
			if (node instanceof ConditionNode) {
				//条件节点
				if (node.getParent() == null) {
					throw new Exception("条件节点[" + node.getFirstId() + "]必须存在父节点！");
				}
				if (node.getParent().getId().equals(startNode.getId())) {
					isConStart = true;
				}
				if(((ConditionNode) node).getYesChild() == null) {
					throw new Exception("条件节点[" + node.getFirstId() + "]必须指定条件满足时的处理节点！");
				}
				if (((ConditionNode) node).getNoChild() == null) {
					throw new Exception("条件节点[" + node.getFirstId() + "]必须指定条件不满足时的处理节点！");
				}
			}
		}
		if (isConStart == false) {
			throw new Exception("开始节点必须存在一个子节点！");
		}
		return list;
	}
	/**
	 * 遍历节点树，check是否所有路径均可到达End节点
	 * @param list
	 * @throws Exception
	 */
	public static  Node checkFlowTree(Node node) throws Exception {
		if (node instanceof EndNode) {
			//End节点
			return null;
		}
		if (node instanceof AuditNode) {
			//审核节点
			Node childNode = ((AuditNode) node).getChild();
			if (childNode != null) {
				return checkFlowTree(childNode);
			} else {
				return node;
			}
		}
		if (node instanceof ConditionNode) {
			//条件节点
			Node yesChildNode = ((ConditionNode) node).getYesChild();
			if (yesChildNode != null) {
				if (checkFlowTree(yesChildNode) == null) {
					Node noChildNode = ((ConditionNode) node).getNoChild();
					if (noChildNode != null) {
						return checkFlowTree(noChildNode);
					} else {
						return node;
					}
				}
			} else {
				return node;
			}			
		}
		Node childNode = node.getChild();
		if (childNode != null) {
			return checkFlowTree(childNode);
		} else {
			return node;
		}
	}
}
