package demo.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.addon.filter.FilterModel;
import org.zkoss.addon.filter.MemoryFilterUtils;
import org.zkoss.addon.filter.impl.FilterModelImpl;

import demo.dao.CrimeRecordDao;
import demo.model.CrimeRecord;

public class CrimeRecordService {
	
	private CrimeRecordDao dao = null;

	public CrimeRecordService() {		
		try {
	        dao = new CrimeRecordDao("src/test/webapp/WEB-INF/SacramentocrimeJanuary2006.csv");
        } catch (IOException e) {
	        e.printStackTrace();
        }
    }
	
	public List<CrimeRecord> loadData() {
		return dao.getRecords();
	}
	
	public Map<String, FilterModel<?>> getAvailFilterModels(List<?> completeList) {
		Map<String, FilterModel<?>> availFilterModels = new HashMap<String, FilterModel<?>>();

		availFilterModels.put("address", new FilterModelImpl<String>("address", "string", null));
		
		availFilterModels.put("district", new FilterModelImpl<Integer>("district", "number", 
				MemoryFilterUtils.<Integer>getDistinctValues(completeList, "district")));
		
		availFilterModels.put("description", new FilterModelImpl<String>("description", "string", 
				MemoryFilterUtils.<String>getDistinctValues(completeList, "description")));
		availFilterModels.put("latitude", new FilterModelImpl<Number>("latitude", "number", null));
		availFilterModels.put("longitude", new FilterModelImpl<Number>("longitude", "number", null));
		
		return availFilterModels;
	}
	
}
