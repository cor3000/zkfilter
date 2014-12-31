package org.zkoss.addon.filter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanPredicate;
import org.apache.commons.beanutils.BeanToPropertyValueTransformer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.apache.commons.collections.functors.NotPredicate;
import org.zkoss.addon.filter.impl.BeginPredicate;
import org.zkoss.addon.filter.impl.ContainPredicate;
import org.zkoss.addon.filter.impl.EndPredicate;
import org.zkoss.addon.filter.impl.GreaterPredicate;
import org.zkoss.addon.filter.impl.LessPredicate;
import org.zkoss.addon.filter.impl.MemberPredicate;
import org.zkoss.addon.filter.impl.NumberEqualPredicate;
import org.zkoss.zul.ListModelList;

public class MemoryFilterUtils {
	
	@SuppressWarnings("unchecked")
    public static <E extends Comparable<E>> Set<E> getDistinctValues(List<?> beanList, String propertyName) {
		return new TreeSet<E>(
			CollectionUtils.collect(beanList, new BeanToPropertyValueTransformer(propertyName)));
	}
	
	public static Predicate createCombinedFilterPredicate(Set<FilterModel<?>> activeFilterModels) {
	    ArrayList<BeanPredicate> predicateList = new ArrayList<BeanPredicate>();
		for (FilterModel<?> filterModel : activeFilterModels) {
			FilterRule filterRule = filterModel.getRule();
			if (filterRule != null) {
				filterRule.setValue(filterModel.getRule().getValue());
			
    			//filter by selected predicate/s
    			addBeanPredicate(predicateList, filterModel, getPredicateForRule(filterModel.getType(), filterRule));    			
			}
			//filter by selected values
			ListModelList<?> distinctValues = filterModel.getDistinctValues();
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

	private static Predicate getPredicateDistinctValues(Set<?> distinctValues) {
		if (distinctValues != null && !distinctValues.isEmpty()) {
			return new MemberPredicate(distinctValues);
		}
	    return null;
    }

	private static Predicate getPredicateForRule(String type, FilterRule rule) {
		String ruleName = rule.getName();
		String ruleValue = (String) rule.getValue();
		
		// string filters
		if("string".equals(type)) {
			if ("equal".equals(ruleName)) {
				return EqualPredicate.getInstance(ruleValue);
			}
			if ("notequal".equals(ruleName)) {
				return NotPredicate.getInstance(EqualPredicate.getInstance(ruleValue));
			}
			
			if ("begin".equals(ruleName)) {
    			return BeginPredicate.getInstance(ruleValue);
    		}
    		if ("end".equals(ruleName)) {
    			return EndPredicate.getInstance(ruleValue);
    		}
    		if ("notcontain".equals(ruleName)) {			
    			return NotPredicate.getInstance(ContainPredicate.getInstance(ruleValue));
    		}
    		if ("contain".equals(ruleName)) {
    			return new ContainPredicate(ruleValue);
    		}
		} 
		// number filters
		else if("number".equals(type)) {
			Number value = null;
			if (ruleValue != null) value = new BigDecimal(ruleValue);
			
			if ("equal".equals(ruleName)) {
				return NumberEqualPredicate.getInstance(value);
			}
			if ("notequal".equals(ruleName)) {
				return NotPredicate.getInstance(EqualPredicate.getInstance(value));
			}
			if ("greater".equals(ruleName)) {
				return GreaterPredicate.getInstance(value);
			}
			if ("notless".equals(ruleName)) {
				return NotPredicate.getInstance(LessPredicate.getInstance(value));
			}
			if ("less".equals(ruleName)) {
				return LessPredicate.getInstance(value);
			}
			if ("notgreater".equals(ruleName)) {
				return NotPredicate.getInstance(GreaterPredicate.getInstance(value));
			}

		}
		return null;
    }

}
