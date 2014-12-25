package org.zkoss.addon.filter.impl;

import org.apache.commons.collections.Predicate;

public class GreaterPredicate implements Predicate {
	
	private Number value;

	public GreaterPredicate(Number value) {
		this.value = value;
    }

	public boolean evaluate(Object object) {
		if (!(object instanceof Number))
			return false;
		
		Number number = (Number) object;
		return (number.doubleValue() > value.doubleValue());
	}

}
