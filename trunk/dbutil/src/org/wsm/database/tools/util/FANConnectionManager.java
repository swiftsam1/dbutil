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

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DataSourceConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class FANConnectionManager extends OracleBaseLoadTestConnectionManager{
    private static Logger log = Logger.getLogger(FANConnectionManager.class);

    public void setupDataSource(String dsName) {
        try {
            OracleDataSource ods = super.getBasicDataSourceSetup(dsName);

            java.util.Properties prop = new java.util.Properties();
            prop.setProperty("MinLimit", "5");
            prop.setProperty("MaxLimit", "45");
            ods.setConnectionCacheName("FanConnectionCache01");
            ods.setConnectionCachingEnabled(true);
            ods.setConnectionCacheProperties(prop);
            ods.setFastConnectionFailoverEnabled(true);
            ObjectPool connectionPool = new GenericObjectPool(null);
            ConnectionFactory connectionFactory = new DataSourceConnectionFactory(ods);
            PoolableConnectionFactory pcf = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
            PoolingDataSource pds = new PoolingDataSource(connectionPool);
            addPoolingDataSourceToBucket(dsName,pds);
            //addOracleDataSourceToBucket(dsName, ods);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection()
            throws DBConnectionFailedException, EmptyDataSourceException{
        Connection con = super.getConnection();
        if(con instanceof OracleConnection) {
            try {
                log.debug("Registering Connection cache callback for connection " + con);
                ((OracleConnection)con).registerConnectionCacheCallback(
                        new OracleConnectionCacheCallbackImpl(),
                        null,
                        OracleConnection.ALL_CONNECTION_CALLBACKS);
                log.debug("Registration Done");
            } catch (SQLException e) {
                log.error("Error registering Connection cache callback. The reason is :");
                log.error(e.getStackTrace());
            }
        }
        return con;
    }

    public Connection getConnection(String keyName)
            throws DBConnectionFailedException, EmptyDataSourceException {
        Connection con = super.getConnection(keyName);
        if(con instanceof OracleConnection) {
            try {
                log.debug("Registering Connection cache callback for connection " + con);
                ((OracleConnection)con).registerConnectionCacheCallback(
                        new OracleConnectionCacheCallbackImpl(),
                        null,
                        OracleConnection.ALL_CONNECTION_CALLBACKS);
                log.debug("Registration Done");
            } catch (SQLException e) {
                log.error("Error registering Connection cache callback. The reason is :");
                log.error(e.getStackTrace());
            }
        }
        return con;
    }


}
