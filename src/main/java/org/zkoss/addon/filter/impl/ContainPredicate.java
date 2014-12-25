package org.zkoss.addon.filter.impl;

import org.apache.commons.collections.Predicate;

public class ContainPredicate implements Predicate {

	private String value;
	
	public ContainPredicate(String value) {
	    this.value = value;
    }

	public boolean evaluate(Object object) {
	    return object.toString().contains(value);
    }

}
