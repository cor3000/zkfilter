package org.zkoss.addon.filter;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.beanutils.BeanPredicate;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.zkoss.addon.filter.impl.BeginPredicate;
import org.zkoss.addon.filter.impl.GreaterPredicate;
import org.zkoss.addon.filter.impl.MemberPredicate;
import org.zkoss.zul.ListModelList;

public class MemoryFilterUtils {
	
	public static Predicate createCombinedFilterPredicate(Set<FilterModel> activeFilterModels) {
	    ArrayList<BeanPredicate> predicateList = new ArrayList<BeanPredicate>();
		for (FilterModel<?> filterModel : activeFilterModels) {
			FilterRule filterRule = filterModel.getRule();
			if (filterRule != null) {
				filterRule.setValue(filterModel.getRule().getValue());
			
    			//filter by selected predicate/s
    			addBeanPredicate(predicateList, filterModel, getPredicateForRule(filterModel.getType(), filterRule));    			
			}
			//filter by selected values
			ListModelList distinctValues = filterModel.getDistinctValues();
			if (distinctValues != null) {				
				addBeanPredicate(predicateList, filterModel, getPredicateDistinctValues(distinctValues.getSelection()));
			}
	
		}
		
		Predicate allPredicate = PredicateUtils.allPredicate(predicateList);
	    return allPredicate;
    }


	private static void addBeanPredicate(ArrayList<BeanPredicate> predicateList,
            FilterModel<?> filterModel, Predicate predicateForRule) {
	    if (predicateForRule != null) {
	    	BeanPredicate beanPredicate = new BeanPredicate(filterModel.getFilterId(), predicateForRule);
	    	predicateList.add(beanPredicate);    				
	    }
    }

	private static Predicate getPredicateDistinctValues(Set distinctValues) {
		if (distinctValues != null && !distinctValues.isEmpty()) {
			return new MemberPredicate(distinctValues);
		}
	    return null;
    }

	private static Predicate getPredicateForRule(String type, FilterRule rule) {
		String ruleName = rule.getName();
		String ruleValue = (String) rule.getValue();
		if("string".equals(type)) {
			if ("begin".equals(ruleName)) {
    			return new BeginPredicate(ruleValue);
    		}
//    		if ("end".equals(ruleName)) {
//    			predicates.put("name", new EndPredicate(ruleValue));
//    		}
//    		if ("notcontain".equals(ruleName)) {			
//    			predicates.put("name", new NotPredicate(new ContainPredicate(ruleValue)));
//    		}
//    		if ("contain".equals(ruleName)) {
//    			predicates.put("name", new ContainPredicate(ruleValue));
//    		}
		} else if("number".equals(type)) {
			Integer value = Integer.parseInt(ruleValue);
			
			// number filters
			if ("equal".equals(ruleName)) {
				return new EqualPredicate(value);
			}
//			if ("notequal".equals(ruleName)) {
//				predicates.put("number", new NotPredicate(new EqualPredicate(value)));
//			}
			if ("greater".equals(ruleName)) {
				return new GreaterPredicate(value);
			}
//			if ("notless".equals(ruleName)) {
//				predicates.put("number", new NotPredicate(new LessPredicate(value)));
//			}
//			if ("less".equals(ruleName)) {
//				predicates.put("number", new LessPredicate(value));
//			}
//			if ("notgreater".equals(ruleName)) {
//				predicates.put("number", new NotPredicate(new GreaterPredicate(value)));
//			}

		}
		return null;
    }

}
