package cn.lmx.flow.bean.design.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Path {
	private String from;
	private String to;
	private List<Dot> dots;
	private Text text;
	private Dot textPos;
	private Map<String, Property> props;
	/**
	 * 构造函数
	 */
	public Path() {
		props = new HashMap<String, Property>();
	}
	public final String getFrom() {
		return from;
	}
	public final void setFrom(String from) {
		this.from = from;
	}
	public final String getTo() {
		return to;
	}
	public final void setTo(String to) {
		this.to = to;
	}
	public final List<Dot> getDots() {
		return dots;
	}
	public final void setDots(List<Dot> dots) {
		this.dots = dots;
	}
	public final Text getText() {
		return text;
	}
	public final void setText(Text text) {
		this.text = text;
	}
	public final Dot getTextPos() {
		return textPos;
	}
	public final void setTextPos(Dot textPos) {
		this.textPos = textPos;
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
