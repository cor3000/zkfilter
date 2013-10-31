/* FilterImpl.java

	Purpose:
		
	Description:
		
	History:
		October 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package org.zkoss.addon.filter.impl;

import org.zkoss.addon.filter.Filter;

public final class FilterImpl {
	private FilterImpl() {};
	
	public static abstract class StringFilter implements Filter<String, String>{
		protected String _constraint;
		
		public void setConstraint(String constraint) {
			_constraint = constraint;
		}

		public String getConstraint() {
			return _constraint;
		}	
	}
	
	public static Filter<String, String> ContainsStringFilter = new StringFilter() {

		public boolean accept(String src) {
			String constraint = getConstraint();
			return constraint != null && src.indexOf(_constraint) >= 0;
		}
		
	};
	
	public static Filter<String, String> StartsWithStringFilter = new StringFilter() {

		public boolean accept(String src) {
			String constraint = getConstraint();
			return constraint != null && src.startsWith(_constraint);
		}
		
	};
	
	public static Filter<String, String> EndsWithStringFilter = new StringFilter() {

		public boolean accept(String src) {
			String constraint = getConstraint();
			return constraint != null && src.endsWith(_constraint);
		}
		
	};
}
