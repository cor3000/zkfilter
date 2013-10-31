/* FilterEvent.java

	Purpose:
		
	Description:
		
	History:
		October 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package org.zkoss.addon.filter;

import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;

public class FilterEvent<E> extends Event {

	public final static String NAME = "onFilter";
	
	public FilterEvent (Component target, Set<E> filteredData) {
		super(NAME, target, filteredData);
	}
	
	public Set<E> getFiltered() {
		return (Set<E>)getData();
	}
}
