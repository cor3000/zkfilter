package demo;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.zkoss.zk.ui.WebApps;

import au.com.bytecode.opencsv.CSVReader;

public class Names {

	private static ArrayList<String> names;
	
	public static ArrayList<String> get() throws IOException {
		if (names != null) {
			return names;
		}
		
		String path = WebApps.getCurrent().getRealPath("female-names.txt");
		CSVReader reader = new CSVReader(new FileReader(path));
		
		names = new ArrayList<String>();
		String [] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	    	String name = nextLine[0];
	    	if (name != null && !name.startsWith("#")) {
	    		names.add(name);
	    	}
	    }
		return names;
	}
}
