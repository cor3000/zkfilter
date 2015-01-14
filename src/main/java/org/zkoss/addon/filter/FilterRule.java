package org.zkoss.addon.filter;

public class FilterRule {
	private String name;	
	private Object value;

	public FilterRule(String name) {
	    this.name = name;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
    public String toString() {
	    return "FilterRule [name=" + name + ", value=" + value + "]";
    }

}
