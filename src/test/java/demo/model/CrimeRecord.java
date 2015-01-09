package demo.model;

public class CrimeRecord {
	private String address;
	private int district;
	private String description;
	private double latitude;
	private double longitude;
	
	public CrimeRecord(String address, int district, String description, double latitude, double longitude) {
	    this.address = address;
	    this.district = district;
	    this.description = description;
	    this.latitude = latitude;
	    this.longitude = longitude;
    }

	public String getAddress() {
		return address;
	}
	
	public int getDistrict() {
		return district;
	}
	
	public String getDescription() {
		return description;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}

	@Override
    public String toString() {
	    return "CrimeRecord [address=" + address + ", district=" + district
	            + ", description=" + description + ", latitude=" + latitude
	            + ", longitude=" + longitude + "]";
    }
	
}
