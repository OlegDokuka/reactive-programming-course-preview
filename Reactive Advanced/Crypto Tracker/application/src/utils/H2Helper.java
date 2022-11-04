package utils;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;

public class H2Helper {

	public static ConnectionFactory createInMemH2() {
		H2ConnectionConfiguration conf = H2ConnectionConfiguration.builder()
		                                                          .url("mem:db;DB_CLOSE_DELAY=-1;TRACE_LEVEL_SYSTEM_OUT=2")
		                                                          .build();

		return new H2ConnectionFactory(conf);
	}
}
