/* FilterpopupViewModel.java

	Purpose:
		
	Description:
		
	History:
		October 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package org.zkoss.addon.filter.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.zkoss.addon.filter.Filter;
import org.zkoss.addon.filter.FilterEvent;
import org.zkoss.addon.filter.FilterExt;
import org.zkoss.bind.Binder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.lang.Classes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ext.Selectable;

public class FilterpopupViewModel {

	
	private final String EVENT_QUEUE_NAME = FilterpopupViewModel.class.getName() + "._eventQueue";
	private final String EVENT_PUBLISH_FILTER_RESULT = "publishFilteredResultEvent";
	private final String EVENT_PERFORM_FILTERING = "performFilteringEvent";
	
	private final String CONSTRAINT = "constraint";
	private final String FILTERED_BY_CONSTRAINT_MODEL = "filteredByConstraintModel";
	
	private Binder _binder;
	private ExecutorService _executor;
	
	private FilterExt _filterExt;
	private Component _showBusyAt;
	
	private boolean _dirtyConstraint = true;//true: need to setup when init
	private Object _constraint;

	private volatile Set<?> _dataSource;
	private volatile Set<?> _filteredByConstraint;
	private volatile ListModelList<?> _filteredByConstraintListModel;
	
	private Set _selected;
	
	private Future<Set<?>> _filterTask;
	private EventQueue<Event> _eventQueue;
	private boolean _selectAll;
	
	@Wire
	Div top;
	
	@Wire
	Listbox listbox;
	
	@Wire
	Div bottom;
	
	@Init
	public void init(@ContextParam(ContextType.COMPONENT) Component component, final @ContextParam(ContextType.BINDER) Binder binder) {

		_executor = Executors.newSingleThreadExecutor();
		_binder = binder;
		
		_filterExt = (FilterExt)component.getParent();
		_filterExt.addEventListener("onCreate", new EventListener<Event>() {

			public void onEvent(Event event) throws Exception {
				initComponents();
			}
		});
		_filterExt.addEventListener(FilterExt.EVENT_SYNC_MODEL, new EventListener<Event>() {

			public void onEvent(Event event) throws Exception {
				initDataSource();
				initFilteredByConstraint();
			}
		});
		_filterExt.addEventListener(FilterExt.EVENT_SYNC_FILTERED, new EventListener<Event>() {
			
			public void onEvent(Event event) throws Exception {
				initFilteredByConstraintSelection();
			}
		});
		_filterExt.addEventListener(FilterExt.EVENT_SYNC_COMPARATOR, new EventListener<Event>() {
			
			public void onEvent(Event event) throws Exception {
				initDataSource();
				initFilteredByConstraint();
			}
		});
		_filterExt.addEventListener(FilterExt.EVENT_START_FILTERING, new EventListener<Event>() {

			public void onEvent(Event event) throws Exception {
				initFilteredByConstraintIfNeeded();
				initFilteredByConstraintSelection();
			}
		});
		_filterExt.addEventListener(FilterExt.EVENT_STOP_FILTERING, new EventListener<Event>() {

			public void onEvent(Event event) throws Exception {
				EventQueues.remove(EVENT_QUEUE_NAME, "session");
			}
		});
	}
	
	@AfterCompose
	public void findShowBusyComponent(@ContextParam(ContextType.COMPONENT) Component component) {
		Selectors.wireComponents(component, this, false);
	}
	
	public void initComponents() {
		Template topTemplate = _filterExt.getTemplate("top");
		if (topTemplate != null) {
			top.getChildren().clear();
			topTemplate.create(top, null, null, null);
		}
		Template headTemplate = _filterExt.getTemplate("head");
		if (headTemplate != null) {
			headTemplate.create(listbox, null, null, null);
		}
		Template modelTemplate = _filterExt.getTemplate("model");
		if (modelTemplate != null) {
			listbox.setTemplate("model", modelTemplate);
		}
	}

	public void setSelectAll(boolean val) {
		_selectAll = val;
	}
	
	public boolean isSelectAll() {
		return _selectAll;
	}
	
	public ListModelList getFilteredByConstraintModel() {
		return _filteredByConstraintListModel;
	}
	
	private Callable<Set<?>> createFilterTask(final Object constraint) {
		return new Callable<Set<?>>() {

			public Set<?> call() throws Exception {
				Filter filter = _filterExt.getFilter();
				if (filter == null) {
					throw new NullPointerException("filter is null");
				}
				filter.setConstraint(constraint);
				
				Comparator comparator = _filterExt.getComparator();
				Set result = comparator != null ? new TreeSet(comparator) : new LinkedHashSet();
				for (Object obj : _dataSource) {
					if (filter.accept(obj)) {
						result.add(obj);
					}
				}
				return result;
			}
		};
	}
	
	private void publishFilterResult(Collection<?> filteredResult) {
		_filteredByConstraintListModel = newListModel(filteredResult);
		Set selectedData = _filterExt.getFiltered();
		if (selectedData != null && !selectedData.isEmpty()) {
			_filteredByConstraintListModel.setSelection(selectedData);
			_selectAll = selectedData.size() == _filteredByConstraintListModel.size();
			_binder.notifyChange(this, "selectAll");
		}
		_binder.notifyChange(this, FILTERED_BY_CONSTRAINT_MODEL);
	}
	
	public void setConstraint(Object constraint) {
		_constraint = constraint;
	}
	
	public Object getConstraint() {
		return _constraint;
	}
	
	@Command
	public void setFilter(@BindingParam("filter") Filter filter) {
		_filterExt.setFilter(filter);
		if (_constraint != null) {
			HashMap arg = new HashMap();
			arg.put("constraint", _constraint);
			_binder.postCommand("performFiltering", arg);
		}
	}
	
	//TODO: setComparator
//	@Command
//	public void setComparator(@BindingParam("comparator") Comparator comparator) {
//
//	}
	
	private void cancelFilterTaskIfNotDone() {
		if (_filterTask != null && !_filterTask.isDone()) {
			_filterTask.cancel(true);
			_filterTask = null;
		}
	}
	
	@Command
	public void performFiltering(@BindingParam("constraint") Object constraint) {
		_dirtyConstraint = true;
		cancelFilterTaskIfNotDone();
		
		if (constraint == null) {
			publishFilterResult(_filterExt.getModel());
			
			if (_eventQueue != null) {
				EventQueues.remove(EVENT_QUEUE_NAME, "session");
				_eventQueue = null;
			}
			return;
		}
		
		//TODO: i18N
		Clients.showBusy(_showBusyAt, "processing");
		
		_filterTask = _executor.submit(createFilterTask(constraint));
		_eventQueue = EventQueues.lookup(EVENT_QUEUE_NAME, "session", true);
		_eventQueue.subscribe(new EventListener<Event>() {

			public void onEvent(Event event) throws Exception {
				if (EVENT_PERFORM_FILTERING.equals(event.getName())) {
					boolean cancel = false;
					try {
						_filteredByConstraint =  _filterTask.get();
					} catch (CancellationException ex) {//ignore
						cancel = true;
					} finally {
						if (!cancel) {
							_eventQueue.publish(new Event(EVENT_PUBLISH_FILTER_RESULT));
						}
					}
				}
			}
		}, true);
		
		_eventQueue.subscribe(new EventListener<Event>() {

			public void onEvent(Event event) throws Exception {
				if (EVENT_PUBLISH_FILTER_RESULT.equals(event.getName())) {
					publishFilterResult(_filteredByConstraint);
					Clients.clearBusy(_showBusyAt);
				}
			}
		});
		
		_eventQueue.publish(new Event(EVENT_PERFORM_FILTERING));
	}
	
	@Command
	public void selectAll(@BindingParam("checked") Boolean check) {
		if (check) {
			((Selectable)_filteredByConstraintListModel).setSelection(_filteredByConstraint);
		} else {
			_filteredByConstraintListModel.clearSelection();
		}
	}
	
	
	@Command
	public void ok() {
		Set<?> selection = _filteredByConstraintListModel.getSelection();
		if (_filteredByConstraint != null && selection != null && !selection.isEmpty()) {
			_filterExt.setFiltered(selection, false);
			FilterEvent event = new FilterEvent(_filterExt, selection);
			Events.postEvent(event);
		}
		_filterExt.stopFiltering();
	}
	
	@Command
	public void cancel() {
		_filterExt.stopFiltering();
	}
	
	private void initDataSource() {
		Comparator<?> comparator = _filterExt.getComparator();
		_dataSource = comparator != null ? new TreeSet(_filterExt.getComparator()) : new LinkedHashSet();
		_dataSource.addAll((Collection) _filterExt.getModel());
	}
	
	private void initFilteredByConstraint() {
		setConstraint(null);
		_binder.notifyChange(this, "constraint");
		_filteredByConstraint = _dataSource;//null constraint
		
		_filteredByConstraintListModel = newListModel(_dataSource);
		_binder.notifyChange(this, FILTERED_BY_CONSTRAINT_MODEL);
		
		_dirtyConstraint = false;
	}
	
	private void initFilteredByConstraintIfNeeded() {
		if (_dirtyConstraint) {
			initFilteredByConstraint();
		}
	}
	
	private void initFilteredByConstraintSelection() {
		Set<?> filteredData = _filterExt.getFiltered();
		if (filteredData != null && _filteredByConstraintListModel != null) {
			((Selectable)_filteredByConstraintListModel).setSelection(filteredData);
			_selectAll = filteredData.size() == _filteredByConstraintListModel.size();
		} else {
			_selectAll = false;
		}
		_binder.notifyChange(this, "selectAll");
	}
	
	private ListModelList newListModel(Collection<?> data) {
		if (data == null) {
			data = Collections.EMPTY_LIST;
		}
		ListModelList model = new ListModelList(data);
		model.setMultiple(true);
		return model;
	}
}