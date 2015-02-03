package demo.vm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.addon.filter.FilterModel;
import org.zkoss.addon.filter.impl.FilterModelImpl;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.ListModelList;

import demo.model.CrimeRecord;
import demo.service.CrimeRecordService;

public class MemoryFilteringViewModel {
	
	private static final int LIMIT = 200;

	private CrimeRecordService service = new CrimeRecordService();

	private List<CrimeRecord> completeList;
	private ListModelList<UiCrimeRecord> crimeRecords = new ListModelList<UiCrimeRecord>();

	private Map<String, FilterModel<?>> availFilterModels = new HashMap<String, FilterModel<?>>();
	
	private String captionLabel;

	public String getCaptionLabel() {		
		return captionLabel;
	}

	private Set<FilterModel<?>> activeFilterModels = new HashSet<FilterModel<?>>();
		
	@Init
	public void init() {
		// prepare model for unfiltered data
		completeList = service.loadData();		
		availFilterModels = service.getAvailFilterModels(completeList);
		
		filterData();
	}
	
    @Command("applyFilter")
    @NotifyChange({"crimeRecords", "captionLabel"})
	public void applyFilter(@BindingParam("model") FilterModelImpl<?> model) {
    	activeFilterModels.add(model);
		
		filterData();
	}
    
    @Command("clearFilter")
    @NotifyChange({"crimeRecords", "captionLabel"})
    public void clearFilter(@BindingParam("model") FilterModel<?> model) {
    	activeFilterModels.remove(model);
    	
    	filterData();
    }
    
    @Command("showCrimeData")    
    public void showCrimeData(@BindingParam("record") UiCrimeRecord record) {
    	record.setOpen(!record.isOpen());
    	BindUtils.postNotifyChange(null, null, record, "open");
    }

	private void filterData() {				
		crimeRecords.clear();		
		List<CrimeRecord> findRecords = service.findRecords(activeFilterModels, LIMIT);
		for (CrimeRecord crimeRecord : findRecords) {
			crimeRecords.add(new UiCrimeRecord(crimeRecord));	        
        }			
		updateCaption();
    }

	private void updateCaption() {
		captionLabel = (crimeRecords.size() < LIMIT) 
			? crimeRecords.size() + " crime records"
			: "Showing only the first " + LIMIT + " records";
    }

	public ListModelList<UiCrimeRecord> getCrimeRecords() {
    	return crimeRecords;
    }

	public Map<String, FilterModel<?>> getAvailFilterModels() {
		return availFilterModels;
	}

}
