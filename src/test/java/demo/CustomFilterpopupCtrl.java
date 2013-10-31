package demo;

import java.util.ArrayList;
import java.util.Set;

import org.zkoss.addon.filter.FilterEvent;
import org.zkoss.addon.filter.Filterpopup;
import org.zkoss.addon.filter.impl.FilterImpl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;

public class CustomFilterpopupCtrl extends SelectorComposer<Component> {

	@Wire
	Filterpopup filterpopup;
	
	@Wire
	Button btn;
	
	@Wire
	Grid grid;
	
	@WireVariable
	Desktop desktop;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		ArrayList list = Names.get();
		
		filterpopup.setModel(list);
		filterpopup.setFilter(FilterImpl.ContainsStringFilter);
		grid.setModel(new ListModelList(list));
		
		desktop.setAttribute("ContainsStringFilter", FilterImpl.ContainsStringFilter);
		desktop.setAttribute("StartWithStringFilter", FilterImpl.StartsWithStringFilter);
		
	}

	@Listen("onClick=#btn")
	public void openFilterpopup() {
		filterpopup.open(btn, "after_end");
	}
	
	@Listen("onFilter=#filterpopup")
	public void onFilter(FilterEvent evt) {
		Set filteredData = evt.getFiltered();
		grid.setModel(new ListModelList(evt.getFiltered()));
	}
}
