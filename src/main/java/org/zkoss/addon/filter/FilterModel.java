package org.zkoss.addon.filter;

import java.util.List;

import org.zkoss.zul.ListModelList;

public interface FilterModel<E> {

	public abstract String getFilterId();

	public abstract List<String> getRuleNames();

	public abstract String getType();

	public abstract ListModelList<E> getDistinctValues();

	public abstract FilterRule getRule();

}