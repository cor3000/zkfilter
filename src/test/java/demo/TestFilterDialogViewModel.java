package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.zkoss.addon.filter.FilterModel;
import org.zkoss.addon.filter.MemoryFilterUtils;
import org.zkoss.addon.filter.impl.BeginPredicate;
import org.zkoss.addon.filter.impl.GreaterPredicate;
import org.zkoss.addon.filter.impl.MemberPredicate;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.ListModelList;

public class TestFilterDialogViewModel {
	
	private List<Item> completeList;
	
	private ListModelList<Item> model;
	
	/**
	 * TODO: a list of filter models, one for each column
	 */
	
	private FilterModel<String> stringFilterModel;
	private FilterModel<Number> numberFilterModel;

	private Map<String, FilterModel> availFilterModels = new HashMap<String, FilterModel>();
	private Set<FilterModel> activeFilterModels = new HashSet<FilterModel>();
	
	@Init
	public void init() {
		completeList = new ArrayList<Item>();	
		completeList.add(new Item("Alice", 1));
		completeList.add(new Item("Alice", 2));
		completeList.add(new Item("Allen", 2));
		completeList.add(new Item("Apple", 3));
		completeList.add(new Item("Betty", 1));
		completeList.add(new Item("Ben", 2));
		completeList.add(new Item("Charlie", 1));
		completeList.add(new Item("Carl", 2));
		completeList.add(new Item("Dennis", 3));
		completeList.add(new Item("Eric", 4));
		
		model = new ListModelList<Item>(completeList);

		Set<String> strings = new LinkedHashSet<String>();
		strings.add("Alice");
		strings.add("Allen");
		strings.add("Apple");
		strings.add("Betty");
		strings.add("Ben");
		strings.add("Charlie");
		strings.add("Carl");
		strings.add("Dennis");
		strings.add("Eric");
		
		stringFilterModel = new FilterModel<String>("name", "string", strings);
		
		numberFilterModel = new FilterModel<Number>("number", "number", null);
		
		availFilterModels.put(stringFilterModel.getFilterId(), stringFilterModel);
		availFilterModels.put(numberFilterModel.getFilterId(), numberFilterModel);		
	}

	
    @Command
	public void applyFilter(@BindingParam("model") FilterModel model) {
    	activeFilterModels.add(model);
		
		filterData();
	}
    
    @Command
    public void clearFilter(@BindingParam("model") FilterModel model) {
    	activeFilterModels.remove(model);
    	
    	filterData();
    }


	private void filterData() {
		ArrayList<Item> filteredList = new ArrayList<Item>(completeList);
		Predicate allPredicate = MemoryFilterUtils.createCombinedFilterPredicate(activeFilterModels);
		CollectionUtils.filter(filteredList, allPredicate);
		
		model.clear();
		model.addAll(filteredList);
    }

	public ListModelList<Item> getModel() {
    	return model;
    }

	public FilterModel<String> getStringFilterModel() {
		return stringFilterModel;
	}
	
	public FilterModel<Number> getNumberFilterModel() {
		return numberFilterModel;
	}
}
