package org.zkoss.addon.filter.inmemory;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NullPredicate;

public class GreaterPredicate implements Predicate {
	
	private Number value;

	public static Predicate getInstance(Number value) {
		if (value == null)
			return NullPredicate.INSTANCE;
		return new GreaterPredicate(value);
	}

	public GreaterPredicate(Number value) {
		this.value = value;
    }
	
	// Double also uses comparison operator in its implementation of compare()
	public boolean evaluate(Object object) {
		if (!(object instanceof Number))
			return false;
		
		Number number = (Number) object;
		return (number.doubleValue() > value.doubleValue());
	}


}
