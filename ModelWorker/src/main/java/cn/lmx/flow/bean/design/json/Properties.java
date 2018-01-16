package cn.lmx.flow.bean.design.json;

import java.util.HashMap;
import java.util.Map;

public class Properties {
	private Map<String, Property> props;
	/**
	 * 构造函数
	 */
	public Properties() {
		props = new HashMap<String, Property>();
	}
	public final void addProperty(String name, Property prop) {
		props.put(name, prop);
	}
	public final Map<String, Property> getProps() {
		return props;
	}
	public final void setProps(Map<String, Property> props) {
		this.props = props;
	}
}
