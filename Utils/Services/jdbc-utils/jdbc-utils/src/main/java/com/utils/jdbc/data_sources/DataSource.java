package com.utils.jdbc.data_sources;

import java.sql.Connection;

public interface DataSource {

	Connection connect();
}
