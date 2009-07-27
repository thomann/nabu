/*
 * Created on 07.05.2004
 */
package ch.unizh.ori.nabu.input.db;

import javax.sql.DataSource;

/**
 * @author pht
 */
public class DummyDBSource extends DBSource {
	
	private DataSource src;

	protected DataSource getDataSource() throws Exception {
		if (src == null) {
			src = new DummyDataSource();
		}

		return src;
	}

}
