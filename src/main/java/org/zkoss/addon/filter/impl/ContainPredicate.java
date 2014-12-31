package org.zkoss.addon.filter.impl;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NullPredicate;

public class ContainPredicate implements Predicate {

	private String value;
	
	public static Predicate getInstance(String value) {
		if (value == null)
			return NullPredicate.INSTANCE;
		return new ContainPredicate(value);
	}

	public ContainPredicate(String value) {
	    this.value = value;
    }

	public boolean evaluate(Object object) {
	    return object.toString().contains(value);
    }

}
