package demo.vm;

import demo.model.CrimeRecord;

public class UiCrimeRecord {
	
	private CrimeRecord record;
	private boolean open;

	public UiCrimeRecord(CrimeRecord record) {
		this.record = record;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public CrimeRecord getRecord() {
		return record;
	}
	
	public String getMarkerText() {
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

}
