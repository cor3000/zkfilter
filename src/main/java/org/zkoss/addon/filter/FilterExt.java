package org.zkoss.addon.filter;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import org.zkoss.zk.ui.Component;

public interface FilterExt extends Component {
	
	public static String EVENT_SYNC_MODEL = "onSyncModel";
	public static String EVENT_SYNC_FILTERED = "onSyncFiltered";
	public static String EVENT_SYNC_COMPARATOR = "onSyncComparator";
	public static String EVENT_START_FILTERING = "onStartFiltering";
	public static String EVENT_STOP_FILTERING = "onStopFiltering";

//	public void startFiltering();
	
	public void stopFiltering();
	
	public void setFilter(Filter<?, ?> filter);
	
	public Filter<?, ?> getFilter();
	
	public void setModel(Collection<?> data, boolean fireEvent);
	
	public Collection<?> getModel();
	
	public void setComparator(Comparator<?> comparator, boolean fireEvent);
	
	public Comparator<?> getComparator();
	
	public void setFiltered(Set<?> filtered, boolean fireEvent);
	
	public Set<?> getFiltered();
}