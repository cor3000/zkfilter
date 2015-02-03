package org.zkoss.addon.filter.inmemory;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.NullPredicate;

public class BeginPredicate implements Predicate {
	
	private String prefix;

	public static Predicate getInstance(String prefix) {
		if (prefix == null)
			return NullPredicate.INSTANCE;
		return new BeginPredicate(prefix);
	}

	public BeginPredicate(String prefix) {
	    this.prefix = prefix;
    }

	public boolean evaluate(Object object) {
		return object.toString().startsWith(prefix);
	}

}
