/*******************************************************************************
 * Copyright 2010 Sudhir Movva
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.wsm.database.tools.util;

import java.sql.Connection;

import org.apache.commons.dbcp.BasicDataSource;

public class MysqlBaseLoadTestConnectionManager extends
		BaseLoadTestConnectionManager {
	public Connection getConnection() throws DBConnectionFailedException,
			EmptyDataSourceException {
		return getConnection(dsKeyName);
	}

	public BasicDataSource getBasicDataSourceSetup() {
		BasicDataSource bds = super.setupBasicDataSourcePooling();
		bds.setMaxActive(150);
		bds.setMaxIdle(8);
		bds.setMaxWait(3000);
		return bds;
	}

	public void setupDataSource(String dsName) {
		dsKeyName = dsName;
		BasicDataSource bds = getBasicDataSourceSetup();
		addBasicDataSourceToBucket(dsName, bds);
	}

	protected String dsKeyName;

}
