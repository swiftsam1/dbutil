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
import java.util.Hashtable;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.dbcp.datasources.PerUserPoolDataSource;
import org.apache.log4j.Logger;

public abstract class BaseLoadTestConnectionManager implements ILoadTestConnectionManager {

    private static final Logger log = Logger.getLogger(BaseLoadTestConnectionManager.class);


    public static synchronized void addConnectionPoolDataSourceToBucket(String keyName, ConnectionPoolDataSource cpds) {
        log.debug("Adding ConnectionPoolDataSource to the bucket with key " + keyName);
        if (!EmptyOrNullStringValidator.isEmpty(keyName) && cpds != null) {
            if (connectionPoolDataSourceBucket == null) {
                connectionPoolDataSourceBucket = new Hashtable<String, DataSource >();
            }
            connectionPoolDataSourceBucket.put(keyName, prepareForConnectionPooling(cpds));
            log.debug("ConnectionPoolDataSource has been added to the Bucket");
        }
    }

    public static synchronized void addPoolingDataSourceToBucket(String keyName, PoolingDataSource ds) {
        log.debug("Adding PoolingDataSource to the bucket with key " + keyName);
        if (!EmptyOrNullStringValidator.isEmpty(keyName) && ds != null) {
            if (connectionPoolDataSourceBucket == null) {
                connectionPoolDataSourceBucket = new Hashtable<String, DataSource>();
            }
            connectionPoolDataSourceBucket.put(keyName, ds);
            log.debug("Pooling DataSource has been added to the Bucket");
        }
    }
    public static synchronized void addBasicDataSourceToBucket(String keyName, BasicDataSource ds) {
        log.debug("Adding PoolingDataSource to the bucket with key " + keyName);
        if (!EmptyOrNullStringValidator.isEmpty(keyName) && ds != null) {
            if (connectionPoolDataSourceBucket == null) {
                connectionPoolDataSourceBucket = new Hashtable<String, DataSource>();
            }
            connectionPoolDataSourceBucket.put(keyName, ds);
            log.debug("Pooling DataSource has been added to the Bucket");
        }
    }

    public static synchronized void addOracleDataSourceToBucket(String keyName, DataSource ds) {
        log.debug("Adding OracleDataSource to the Bucket with key " + keyName);
        if (!EmptyOrNullStringValidator.isEmpty(keyName) && ds != null) {
            if (connectionPoolDataSourceBucket == null) {
                connectionPoolDataSourceBucket = new Hashtable<String, DataSource>();
            }
            connectionPoolDataSourceBucket.put(keyName, ds);
            log.debug("Oracle DataSource has been added to the Bucket");
        }

    }

    public static synchronized void removeConnectionPoolDataSourceFromBucket(String keyName) {
        log.debug("Removing Connection Pool DataSource from the Bucket");
        if (connectionPoolDataSourceBucket != null) {
            if (!EmptyOrNullStringValidator.isEmpty(keyName)) {
                Object dsObj = connectionPoolDataSourceBucket.get(keyName);
                if (dsObj != null) {
                    if (dsObj instanceof PerUserPoolDataSource) {
                        log.debug("DataSource type is PerUserPoolDataSource");
                        PerUserPoolDataSource pupdsTemp = (PerUserPoolDataSource) dsObj;
                        pupdsTemp.close();
                        log.debug("Closed DataSource");
                    }
                    if (dsObj instanceof PoolingDataSource) {
                        log.debug("DataSource type is PoolingDataSource");
/*                        PoolingDataSource pds = (PoolingDataSource) dsObj;

                        pds = null;
*/                        log.debug("Set the Object to null");
                    }
                    if (dsObj instanceof BasicDataSource) {
                        log.debug("DataSource type is PoolingDataSource");
                        BasicDataSource bds = (BasicDataSource) dsObj;
                        try {
                            bds.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        log.debug("Closed DataSource");
                    }
                }
            }
        }
        if (!EmptyOrNullStringValidator.isEmpty(keyName)) {
            if (connectionPoolDataSourceBucket != null) {
                connectionPoolDataSourceBucket.remove(keyName);
                log.debug("Removed the DataSource object from the Bucket.");
            }
        }
    }

    private static PerUserPoolDataSource prepareForConnectionPooling(ConnectionPoolDataSource cpds) {
        log.debug("Preparing ConnectionPoolDataSource with the default properties ");
        PerUserPoolDataSource pupds = new PerUserPoolDataSource();
        pupds.setConnectionPoolDataSource(cpds);
        pupds.setDefaultMaxActive(0);
        pupds.setDefaultMaxIdle(8);
        return pupds;
    }

    protected BasicDataSource setupBasicDataSourcePooling() {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(ConnectionManager.connUrl);
        bds.setUsername(ConnectionManager.currentDbProps.getUserName());
        bds.setPassword(ConnectionManager.currentDbProps.getPassword());
        return bds;
    }

    public synchronized Connection getConnection(String keyName) throws EmptyDataSourceException, DBConnectionFailedException {
        if (connectionPoolDataSourceBucket == null)
            throw new EmptyDataSourceException("Datasource is empty");
        Connection con = null;
        if (!EmptyOrNullStringValidator.isEmpty(keyName)) {
            Object dsObj = connectionPoolDataSourceBucket.get(keyName);
            if (dsObj == null) {
                throw new EmptyDataSourceException("Datasource for the key is not available ");
             } else {
                try {
                    con = ((DataSource)dsObj).getConnection();
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }


//            if (dsObj instanceof PerUserPoolDataSource) {
//                PerUserPoolDataSource pupdsTemp = (PerUserPoolDataSource) dsObj;
//                try {
//                    con = pupdsTemp.getConnection();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    throw new DBConnectionFailedException("Unable to create new Connection ");
//                }
//            }
//            if (dsObj instanceof PoolingDataSource) {
//                PoolingDataSource pds = (PoolingDataSource) dsObj;
//                log.debug("Getting Basic Connection");
//                try {
//                    con = pds.getConnection();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    throw new DBConnectionFailedException("Unable to create new Connection ");
//                }
//            }
//
//            if(dsObj instanceof OracleDataSource) {
//                OracleDataSource ods = (OracleDataSource)dsObj;
//                log.debug("Getting connection from oracle datasource ");
//                try {
//                    con = ods.getConnection();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    throw new DBConnectionFailedException("Unable to create connection");
//                }
//            }
        }
        return con;
    }

    public int getNumberOfConnections() {
        return numberOfConnections;
    }

    public void setNumberOfConnections(int numberOfConnections) {
        this.numberOfConnections = numberOfConnections;
    }

    private static Hashtable<String, DataSource> connectionPoolDataSourceBucket;
    private int numberOfConnections;
}
