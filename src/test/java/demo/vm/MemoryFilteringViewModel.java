package demo.vm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.zkoss.addon.filter.FilterModel;
import org.zkoss.addon.filter.MemoryFilterUtils;
import org.zkoss.addon.filter.impl.FilterModelImpl;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModelList;

import demo.model.Item;

public class MemoryFilteringViewModel {
	
	private List<Item> completeList;
	
	private ListModelList<Item> model;
	
	// XXX: inject service here!
	// private DataService ds = new InMemoryDataServer();
	/**
	 * TODO: a list of filter models, one for each column
	 * should be generated based on bean class
	 */
	
	private FilterModel<String> stringFilterModel;
	private FilterModel<Number> numberFilterModel;

	private Map<String, FilterModel<?>> availFilterModels = new HashMap<String, FilterModel<?>>();
	private Set<FilterModel<?>> activeFilterModels = new HashSet<FilterModel<?>>();
	
	@Init
	public void init() {
		// TODO: Load data from service
		// prepare model for unfiltered data
		completeList = Arrays.asList(
			new Item("Alice", 1),
			new Item("Alice", 2),
			new Item("Allen", 2),
			new Item("Apple", 3),
			new Item("Betty", 1),
			new Item("Ben", 2),
			new Item("Charlie", 1),
			new Item("Carl", 2),
			new Item("Dennis", 3),
			new Item("Eric", 4));		
		model = new ListModelList<Item>(completeList);

		// 
		Set<String> strings = null;
        try {
	        strings = MemoryFilterUtils.<String>getDistinctValues(completeList, "name");
        } catch (Exception e) {
	        e.printStackTrace();
        }
		
        // TODO: have another service method to return possible filter models 
		stringFilterModel = new FilterModelImpl<String>("name", "string", strings);
		numberFilterModel = new FilterModelImpl<Number>("number", "number", null);
		
		availFilterModels.put(stringFilterModel.getFilterId(), stringFilterModel);
		availFilterModels.put(numberFilterModel.getFilterId(), numberFilterModel);		
	}
	
    @Command
	public void applyFilter(@BindingParam("model") FilterModelImpl<?> model) {
    	activeFilterModels.add(model);
		
		filterData();
	}
    
    @Command
    public void clearFilter(@BindingParam("model") FilterModel<?> model) {
    	activeFilterModels.remove(model);
    	
    	filterData();
    }


	private void filterData() {		
		ArrayList<Item> filteredList = new ArrayList<Item>(completeList);
		Predicate combinedPredicate = MemoryFilterUtils.createCombinedFilterPredicate(activeFilterModels);
		CollectionUtils.filter(filteredList, combinedPredicate);
		
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
