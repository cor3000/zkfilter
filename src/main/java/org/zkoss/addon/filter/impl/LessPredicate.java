package org.zkoss.addon.filter.impl;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NullPredicate;

public class LessPredicate implements Predicate {

	private Number value;

	public static Predicate getInstance(Number value) {
		if (value == null)
			return NullPredicate.INSTANCE;
		return new LessPredicate(value);
	}

	public LessPredicate(Number value) {
	    this.value = value;
    }

	public boolean evaluate(Object object) {
		if (!(object instanceof Number))
			return false;
		
		Number number = (Number) object;
		return (number.doubleValue() < value.doubleValue());
	}

}
