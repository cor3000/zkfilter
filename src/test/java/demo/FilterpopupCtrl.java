package demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import org.zkoss.addon.filter.FilterEvent;
import org.zkoss.addon.filter.Filterpopup;
import org.zkoss.addon.filter.impl.FilterImpl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;

public class FilterpopupCtrl extends SelectorComposer<Component> {

	@Wire
	Filterpopup popup;
	
	@Wire
	Button btn;
	
	@Wire
	Button clearBtn;
	
	@Wire
	Grid grid;
	
	private ArrayList<String> list;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		list = Names.get();
		
		popup.setModel(list);
		popup.setFilter(FilterImpl.ContainsStringFilter);
		grid.setModel(new ListModelList(list));
	}

	@Listen("onClick=#btn")
	public void openFilterpopup() {
		popup.open(btn, "after_end");
	}
	
	@Listen("onClick=#clearBtn")
	public void clearFiltered() throws IOException {
		grid.setModel(new ListModelList(Names.get()));
		popup.setModel(list);
		popup.setFiltered(null);
		clearBtn.setDisabled(true);
	}
	
	@Listen("onFilter=#popup")
	public void onFilter(FilterEvent evt) {
		grid.setModel(new ListModelList(evt.getFiltered()));
		clearBtn.setDisabled(false);
	}
	
	@Listen("onCheck=#ascending")
	public void sortAscending() {
		popup.setComparator(new Comparator<String>() {

			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		});
	}
	
	@Listen("onCheck=#descending")
	public void sortDescending() {
		popup.setComparator(new Comparator<String>() {

			public int compare(String s1, String s2) {
				return -s1.compareTo(s2);
			}
		});
	}
	
	@Listen("onCheck=#contains")
	public void sortContainsString() {
		popup.setFilter(FilterImpl.ContainsStringFilter);
	}
	
	@Listen("onCheck=#beginsWith")
	public void sortBeginsWith() {
		popup.setFilter(FilterImpl.StartsWithStringFilter);
	}
}
