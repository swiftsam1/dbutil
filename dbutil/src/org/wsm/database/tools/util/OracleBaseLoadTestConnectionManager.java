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
import java.sql.SQLException;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;

public class OracleBaseLoadTestConnectionManager extends BaseLoadTestConnectionManager {

    public Connection getConnection()
            throws DBConnectionFailedException, EmptyDataSourceException {
        return getConnection(dsKeyName);
    }


    public OracleConnectionPoolDataSource getBasicConnectionPoolDataSourceSetup(String dsName) {
        dsKeyName = dsName;
        OracleConnectionPoolDataSource ocpds = null;

        try {
            ocpds = new OracleConnectionPoolDataSource();
            ocpds.setUser(ConnectionManager.currentDbProps.getUserName());
            ocpds.setPassword(ConnectionManager.currentDbProps.getPassword());
            ocpds.setURL(ConnectionManager.connUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ocpds;
    }
    public OracleDataSource getBasicDataSourceSetup(String dsName) {
        dsKeyName = dsName;
        OracleDataSource ods = null;
        try {
            ods = new OracleDataSource();
            ods.setUser(ConnectionManager.currentDbProps.getUserName());
            ods.setPassword(ConnectionManager.currentDbProps.getPassword());
            ods.setURL(ConnectionManager.connUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ods;
    }

    public void setupDataSource(String dsName) {
        OracleConnectionPoolDataSource ocpds = getBasicConnectionPoolDataSourceSetup(dsName);
        addConnectionPoolDataSourceToBucket(dsName, ocpds);
    }

    protected String dsKeyName;

}
