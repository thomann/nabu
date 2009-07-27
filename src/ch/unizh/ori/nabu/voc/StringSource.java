package ch.unizh.ori.nabu.voc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StringSource extends Source {
	
	private String theVoc;

	public StringSource(String theVoc) {
		super();
		this.theVoc = theVoc;
	}

	public List readLections(URL base) throws Exception {
		List fieldstreams = new ArrayList();
		EmptyLineSource.read(new BufferedReader(new StringReader(theVoc)), fieldstreams, this);
		return fieldstreams;
	}

}
