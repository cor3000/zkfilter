package org.zkoss.addon.filter.inmemory;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NullPredicate;

public class EndPredicate implements Predicate {
	
	private String suffix;
	
	public static Predicate getInstance(String suffix) {
		if (suffix == null)
			return NullPredicate.INSTANCE;
		return new EndPredicate(suffix);
	}
	
	public EndPredicate(String suffix) {
	    this.suffix = suffix;
    }

	public boolean evaluate(Object object) {
		return object.toString().endsWith(suffix);
	}

}
