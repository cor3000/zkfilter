package demo.vm;

import java.util.ArrayList;
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
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.gmaps.Gmaps;
import org.zkoss.gmaps.Gmarker;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;

import demo.model.CrimeRecord;
import demo.service.CrimeRecordService;

public class MemoryFilteringViewModel {
	
	@Wire
	private Gmaps map;

	private static final int LIMIT = 1000;

	private CrimeRecordService service = new CrimeRecordService();

	private List<CrimeRecord> completeList;
	private ListModelList<CrimeRecord> crimeRecords = new ListModelList<CrimeRecord>();

	private Map<String, FilterModel<?>> availFilterModels = new HashMap<String, FilterModel<?>>();
	
	private String captionLabel;
	private int fromIndex;
	private int toIndex;
	private int totalRecords;

	public String getCaptionLabel() {
		return captionLabel;
	}

	private Set<FilterModel<?>> activeFilterModels = new HashSet<FilterModel<?>>();
	
	@AfterCompose
	public void doAfterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, true);
	}
	
	@Init
	public void init() {
		// prepare model for unfiltered data
		totalRecords = service.loadData().size();		
		updateRange(0);
		
		availFilterModels = service.getAvailFilterModels(completeList);
	}
	
    @Command("applyFilter")
    @NotifyChange("crimeRecords")
	public void applyFilter(@BindingParam("model") FilterModelImpl<?> model) {
    	activeFilterModels.add(model);
		
		filterData();
	}
    
    @Command("clearFilter")
    public void clearFilter(@BindingParam("model") FilterModel<?> model) {
    	activeFilterModels.remove(model);
    	
    	filterData();
    }
    
    @Command("first")
    @NotifyChange({"model", "captionLabel"})
    public void gotoFirstPage() {	
		updateRange(0);
    }
    
    @Command("prev")
    @NotifyChange({"crimeRecords", "captionLabel"})
    public void gotoPrevPage() {
    	updateRange(fromIndex - LIMIT);
    }

    @Command("next")
    @NotifyChange({"crimeRecords", "captionLabel"})
    public void gotoNextPage() {
    	updateRange(fromIndex + LIMIT);
    }
    
    @Command("last")
    @NotifyChange({"crimeRecords", "captionLabel"})
    public void gotoLastPage() {
		updateRange(((totalRecords-1) / LIMIT) * LIMIT);
    }
    
    @Command("showCrimeData")
    public void showCrimeData(@BindingParam("marker") Gmarker marker) {
    	if (marker != null)
    		marker.setOpen(!marker.isOpen());
    }
    
    @Command("locateCrimeData")
    public void locateCrimeData(@BindingParam("record") Set<CrimeRecord> selectedRecords) {
    	if (selectedRecords.isEmpty()) return;
    	
    	CrimeRecord record = selectedRecords.iterator().next();
    	int index = crimeRecords.indexOf(record);
    	
    	Gmarker marker = (Gmarker) map.getFellowIfAny("marker_" + index);
    	if (marker != null) marker.setOpen(true);
    }
    
    public String formatCrimeData(CrimeRecord record) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("<ul>");
    	sb.append("  <li><b>Address: </b>" + record.getAddress() + "</li>");
    	sb.append("  <li><b>District: </b>" + record.getDistrict() + "</li>");
    	sb.append("  <li><b>Description: </b>" + record.getDescription() + "</li>");
    	sb.append("  <li><b>Latitude: </b>" + record.getLatitude() + "</li>");
    	sb.append("  <li><b>Longitude: </b>" + record.getLongitude() + "</li>");
    	sb.append("</ul>");

    	return sb.toString();
    }

	private void filterData() {		
		List<CrimeRecord> filteredList = new ArrayList<CrimeRecord>(completeList);
		Predicate combinedPredicate = MemoryFilterUtils.createCombinedFilterPredicate(activeFilterModels);
		CollectionUtils.filter(filteredList, combinedPredicate);
		
		crimeRecords.clear();
		crimeRecords.addAll(filteredList);
		
		if (map != null) map.invalidate();
    }

	private void updateRange(int fromIndex) {
		if (fromIndex >= this.totalRecords)
			return;
		
		this.fromIndex = Math.max(0, fromIndex);
		this.toIndex = Math.min(this.fromIndex + LIMIT, this.totalRecords);
		
		captionLabel = 
			String.format("Crime Records (%d - %d / %d)", 
			(this.fromIndex+1), this.toIndex, this.totalRecords);
		
		completeList = service.loadData(this.fromIndex, this.toIndex);
		
		filterData();
	}

	public ListModelList<CrimeRecord> getCrimeRecords() {
    	return crimeRecords;
    }

	public Map<String, FilterModel<?>> getAvailFilterModels() {
		return availFilterModels;
	}

}
