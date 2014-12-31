package org.zkoss.addon.filter.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.addon.filter.FilterRule;
import org.zkoss.addon.filter.FilterModel;
import org.zkoss.zul.ListModelList;

public class FilterModelImpl<E> implements FilterModel<E> {
	
	static Map<String, List<String>> AVAIL_RULES;
	
	static {
		AVAIL_RULES = new HashMap<String, List<String>>();
		
		// Numeric filter rules
		List<String> ruleNames = Arrays.asList("equal", "notequal", "greater", "notless","less", "notgreater");
		AVAIL_RULES.put("number", ruleNames);
		
		// String filter rules
		ruleNames = Arrays.asList("equal", "notequal", "begin", "end", "notcontain", "contain");
		AVAIL_RULES.put("string", ruleNames);
	}
	
	private String filterId;
	private String type;
	private ListModelList<E> distinctValuesModel = null;
	private List<String> ruleNames;
	private FilterRule rule = new FilterRule("");

	public FilterModelImpl(String filterId, String type, Set<E> distinctValues) {
		this.filterId = filterId;
		this.type = type;
		this.ruleNames = AVAIL_RULES.get(this.type);
		initDistinctValues(distinctValues);
	}

	private void initDistinctValues(Set<E> distinctValues) {
		if (distinctValues != null) {
			distinctValuesModel = new ListModelList<E>(distinctValues);
			distinctValuesModel.setMultiple(true);
			for (E element : distinctValuesModel) {
				distinctValuesModel.addToSelection(element);
			}			
		}
	}
	
	@Override
	public String getFilterId() {
		return filterId;
	}

	@Override
	public List<String> getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public ListModelList<E> getDistinctValues() {
		return distinctValuesModel;
	}

	@Override
	public FilterRule getRule() {
		return rule;
	}
	
	@Override
    public String toString() {
	    return "FilterModel [type=" + type + ", distinctValues="
	            + distinctValuesModel.getInnerList() + "]";
    }

}
