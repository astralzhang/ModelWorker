package cn.lmx.flow.bean.design.json;

import java.util.HashMap;
import java.util.Map;

public class Task {
	private String type;
	private Text text;
	private PosAndSize attr;
	private Map<String, Property> props;
	/**
	 * 构造函数
	 */
	public Task() {
		props = new HashMap<String, Property>();
	}
	public final String getType() {
		return type;
	}
	public final void setType(String type) {
		this.type = type;
	}
	public final Text getText() {
		return text;
	}
	public final void setText(Text text) {
		this.text = text;
	}
	public final PosAndSize getAttr() {
		return attr;
	}
	public final void setAttr(PosAndSize attr) {
		this.attr = attr;
	}
	public final Map<String, Property> getProps() {
		return props;
	}
	public final void setProps(Map<String, Property> props) {
		this.props = props;
	}
	public final void addProperty(String name, Property prop) {
		props.put(name, prop);
	}
}
