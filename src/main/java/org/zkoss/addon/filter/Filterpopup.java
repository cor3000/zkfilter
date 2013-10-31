/* Filterpopup.java

	Purpose:
		
	Description:
		
	History:
		October 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package org.zkoss.addon.filter;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Popup;

public class Filterpopup extends Popup implements IdSpace, FilterExt {

	
	private static final String DEFAULT_TEMPLATE = "~./zul/filter.zul";
	
	private Component _cave;
	private Filter<?, ?> _filter;
	
	private Collection<?> _model;
	private Set<?> _filteredData;

	private Comparator<?> _comparator;
	
	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		
		if (_cave != null) {
			_cave.setParent(null);
		}
		_cave = Executions.createComponents(DEFAULT_TEMPLATE, this, null);
	}
	
	/**
	 * Sets the Filter
	 */
	public void setFilter(Filter<?, ?> filter) {
		_filter = filter;
	}
	

//	public void setFilter(Filter<?, ?> filter, boolean fireEvent) {
//		if (!Objects.equals(_filter, filter)) {
//			_filter = filter;
//			if (fireEvent) {
//				Events.postEvent(new Event(FilterExt.EVENT_SYNC_, this));
//			}
//		}
//	}
	
	public Filter<?, ?> getFilter() {
		return _filter;
	}
	
	public void setModel(Collection<?> data) {
		setModel(data, true);
	}
	
	public void setModel(Collection<?> data, boolean fireEvent) {
		if (!Objects.equals(_model, data)) {
			_model = data;
		}
		if (fireEvent) {//model may be the same collection, but content changed
			Events.postEvent(new Event(FilterExt.EVENT_SYNC_MODEL, this));
		}
	}
		
	public Collection<?> getModel() {
		return _model;
	}
	
	public void setComparator(Comparator<?> comparator) {
		setComparator(comparator, true);
	}
	
	public void setComparator(Comparator<?> comparator, boolean fireEvent) {
		if (!Objects.equals(_comparator, comparator)) {
			_comparator = comparator;
			if (fireEvent) {
				Events.postEvent(new Event(FilterExt.EVENT_SYNC_COMPARATOR, this));
			}
		}
	}
	
	public Comparator<?> getComparator() {
		return _comparator;
	}
	
	public void setFiltered(Set<?> filtered) {
		setFiltered(filtered, true);
	}
	
	public void setFiltered(Set<?> filtered, boolean fireEvent) {
		if (!Objects.equals(_filteredData, filtered)) {
			_filteredData = filtered;
			if (fireEvent) {
				Events.postEvent(new Event(FilterExt.EVENT_SYNC_FILTERED, this));
			}
		}
	}
	
	public Set<?> getFiltered() {
		return _filteredData;
	}
	
	@Override
	public void open(Component ref, String position) {
		super.open(ref, position);
		Events.postEvent(new Event(FilterExt.EVENT_START_FILTERING, Filterpopup.this));
	}

	public void close() {
		super.close();
		Events.postEvent(new Event(FilterExt.EVENT_STOP_FILTERING, this));
	}
	
	public void stopFiltering() {
		this.close();
	}
}