package org.zkoss.addon.filter.impl;

import org.apache.commons.collections.Predicate;

public class BeginPredicate implements Predicate {
	
	private String prefix;

	public BeginPredicate(String prefix) {
		if (prefix == null) prefix = "";
	    this.prefix = prefix;
    }

	public boolean evaluate(Object object) {
		return object.toString().startsWith(prefix);
	}

}
