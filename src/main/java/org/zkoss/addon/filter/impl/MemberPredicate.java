package org.zkoss.addon.filter.impl;

import java.util.Set;

import org.apache.commons.collections.Predicate;

public class MemberPredicate implements Predicate {
	
	private Set<?> matching;

	public MemberPredicate(Set<?> matching) {
	    this.matching = matching;
    }

	public boolean evaluate(Object object) {
		return matching.contains(object);
	}

}
