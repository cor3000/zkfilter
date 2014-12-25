package org.zkoss.addon.filter.impl;

import org.apache.commons.collections.Predicate;

public class EndPredicate implements Predicate {
	
	private String suffix;
	
	public EndPredicate(String suffix) {
	    this.suffix = suffix;
    }

	public boolean evaluate(Object object) {
		return object.toString().endsWith(suffix);
	}

}
