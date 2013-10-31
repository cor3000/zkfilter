/* ProfileViewModel.java

	Purpose:
		
	Description:
		
	History:
		May 30, 2013 11:42:43 AM , Created by Sam

Copyright (C) 2013 Potix Corporation. All Rights Reserved.

{{IS_RIGHT
}}IS_RIGHT
 */
package demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.zkoss.addon.filter.Filter;
import org.zkoss.addon.filter.Filterpopup;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;


public class ProfileViewModel {
	
	boolean _filtered;
	List<Profile> _data;
	
	AfterDateFilter _afterDateFilter;
	BeforeDateFilter _beforeDateFilter;
	
	@Wire
	Filterpopup dateFilter;
	
	@Init
	public void init() {
		
		_data = Profiles.provideData();
		
		_afterDateFilter = new AfterDateFilter();
		_beforeDateFilter = new BeforeDateFilter();
	}
	
	public boolean isFiltered() {
		return _filtered;
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	@NotifyChange("profiles")
	public void clearFilter() {
		_data = Profiles.provideData();
		//TODO: bind filtered attribute
		dateFilter.setFiltered(null);
	}
	
	@Command
	@NotifyChange({"profiles", "filtered"})//reset filterpopup's model and enable clear filter button
	public void setProfiles(@BindingParam("model") Collection model) {
		_data = new ArrayList(model);
		_filtered = true;
	}
	
	public List<Profile> getProfiles() {
		return _data;
	}
	
	public Comparator<Profile> getProfileDateComparator() {
		return new Comparator<Profile>() {

			public int compare(Profile o1, Profile o2) {
				return o1.getBirth().compareTo(o2.getBirth());
			}
		};		
	}
	
	public Filter getAfterDateFilter() {
		return _afterDateFilter;
	}
	
	public Filter getBeforeDateFilter() {
		return _beforeDateFilter;
	}
	
	@Command
	public void open(@BindingParam("filter") Filterpopup filterpopup, @BindingParam("ref") Component ref) {
		filterpopup.open(ref, "after_end");
	}
	
	@Command
	@NotifyChange("profiles")
	public void setBirth(@BindingParam("profile") Profile profile, @BindingParam("birth") Date birth) {
		profile.setBirth(birth);
	}
	
	@Command
	@NotifyChange("profiles")
	public void setMarried(@BindingParam("profile") Profile profile, @BindingParam("married") Boolean married) {
		profile.setMarried(married);
	}
	
	@Command
	public void setSkills(@BindingParam("profile") Profile profile, @BindingParam("skills") Collection<Skill> skills) {
		profile.setSkill(new ArrayList<Skill>(skills));
	}
	
	public Skill[] getAllSkills() {
		return Skill.values();
	}

	class AfterDateFilter implements Filter<Profile, Date> {
		Date _constraint;
		
		public boolean accept(Profile profile) {
			
			return profile.getBirth() != null && profile.getBirth().after(getConstraint());
		}
	
		public void setConstraint(Date constraint) {
			_constraint = constraint;
		}
	
		public Date getConstraint() {
			return _constraint;
		}
	}
	
	class BeforeDateFilter implements Filter<Profile, Date> {
		Date _constraint;
		
		public boolean accept(Profile profile) {
			
			return profile.getBirth() != null && profile.getBirth().before(getConstraint());
		}
	
		public void setConstraint(Date constraint) {
			_constraint = constraint;
		}
	
		public Date getConstraint() {
			return _constraint;
		}
	}
}