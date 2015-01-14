package demo.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import demo.model.CrimeRecord;

public class CrimeRecordDao {
	
	private List<CrimeRecord> records = null;
	
	public CrimeRecordDao(String filename) throws IOException {
		readFile(filename);	    
    }

	private void readFile(String filename) throws IOException {
	    records = new ArrayList<CrimeRecord>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		String line = br.readLine();	// skip first line
		while ((line = br.readLine()) != null) {
			String[] fields = line.split(",");
			
			String address = fields[0];
			int district = Integer.valueOf(fields[1]);
			String description = fields[2];
			double latitude = Double.valueOf(fields[3]);
			double longitude = Double.valueOf(fields[4]);
			
			records.add(new CrimeRecord(address, district, description, latitude, longitude));
		}
		br.close();
    }

	public List<CrimeRecord> getRecords() {
		return records;
	}

}
