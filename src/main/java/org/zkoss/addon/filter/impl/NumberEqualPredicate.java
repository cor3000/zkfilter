package org.zkoss.addon.filter.impl;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NullPredicate;

public class NumberEqualPredicate implements Predicate {
	
	private Number value;
	
	public static Predicate getInstance(Number value) {
		if (value == null)
			return NullPredicate.INSTANCE;
		return new NumberEqualPredicate(value);
	}

	public NumberEqualPredicate(Number value) {
	    this.value = value;
    }
	
	private static double EPSILON = 1e-10;

	@Override
	public boolean evaluate(Object object) {
		if (!(object instanceof Number))
			return false;
		
		Number number = (Number) object;	
		return Math.abs(number.doubleValue() - value.doubleValue()) < EPSILON;
	}

}
