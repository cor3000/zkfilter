package demo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.zkoss.addon.filter.FilterModel;
import org.zkoss.addon.filter.impl.FilterModelImpl;
import org.zkoss.addon.filter.inmemory.MemoryFilterUtils;

import demo.dao.CrimeRecordDao;
import demo.model.CrimeRecord;

public class CrimeRecordService {

	private CrimeRecordDao dao = null;

	public CrimeRecordService() {
		dao = new CrimeRecordDao("src/test/webapp/WEB-INF/SacramentocrimeJanuary2006.csv");
	}

	public Map<String, FilterModel<?>> getAvailableFilterModels() {
		Map<String, FilterModel<?>> availableFilterModels = new LinkedHashMap<String, FilterModel<?>>();

		// could be cached
		List<?> completeList = dao.getRecords();
		Set<Integer> distinctDistricts = 
			MemoryFilterUtils.<Integer> getDistinctValues(completeList, "district");
		Set<String> distinctDescriptions = MemoryFilterUtils
			.<String> getDistinctValues(completeList, "description");

		// consider dynamic creation based on reflection
		availableFilterModels
			.put("address", new FilterModelImpl<String>("address", "string", null));
		availableFilterModels
			.put("district", new FilterModelImpl<Integer>("district", "number", distinctDistricts));
		availableFilterModels
			.put("description", new FilterModelImpl<String>("description", "string", distinctDescriptions));
		availableFilterModels
			.put("latitude", new FilterModelImpl<Number>("latitude", "number", null));
		availableFilterModels
			.put("longitude", new FilterModelImpl<Number>("longitude", "number", null));

		return availableFilterModels;
	}

	public List<CrimeRecord> findRecords(Set<FilterModel<?>> filterModels, int limit) {
		// instead of memory filtering, e.g. create a dynamic database query, or
		// call your dao
		List<CrimeRecord> completeList = dao.getRecords();
		List<CrimeRecord> filteredList = new ArrayList<CrimeRecord>(completeList);
		Predicate combinedPredicate = MemoryFilterUtils.createCombinedFilterPredicate(filterModels);
		CollectionUtils.filter(filteredList, combinedPredicate);
		return filteredList.subList(0, Math.min(filteredList.size(), limit));
	}

}
