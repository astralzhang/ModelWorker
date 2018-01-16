package cn.lmx.flow.bean.design.json;

import java.util.HashMap;
import java.util.Map;

public class Flow {
	private Map<String, Task> states;
	private Map<String, Path> paths;
	private Properties props;
	/**
	 * 构造函数
	 */
	public Flow() {
		states = new HashMap<String, Task>();
		paths = new HashMap<String, Path>();
	}
	public final Map<String, Task> getStates() {
		return states;
	}
	public final void setStates(Map<String, Task> states) {
		this.states = states;
	}
	public final void addState(String name, Task task) {
		states.put(name, task);
	}
	public final Map<String, Path> getPaths() {
		return paths;
	}
	public final void setPaths(Map<String, Path> paths) {
		this.paths = paths;
	}
	public final void addPath(String name, Path path) {
		paths.put(name, path);
	}
	public final Properties getProps() {
		return props;
	}
	public final void setProps(Properties props) {
		this.props = props;
	}
}
