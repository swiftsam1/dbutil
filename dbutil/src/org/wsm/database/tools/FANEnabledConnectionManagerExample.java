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
package org.wsm.database.tools;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class FANEnabledConnectionManagerExample {

    public static Connection getConnection() {
        if (ods == null) {
            initializeDataSource();
        }
        Connection conn = null;
        try {
            conn = ods.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return conn;
    }

    public static void main(String[] args) {
        initializeDataSource();
        Connection conn = getConnection();
        /**
         * use the connection.
         */
        try {
            if(conn != null)
            conn.close();//This returns the connection to the cache.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void initializeDataSource() {
        if (ods == null) {
            try {
                ods = new OracleDataSource();
                ods.setUser("user");
                ods.setPassword("password");
                /**
                 * Fast Connect Failover Supports both thin driver and OCI driver.
                 * For OCI driver, just mention the service name in the TNSNames.ora file
                 * For thin driver specify the connection string.
                 * Examples:
                 * Thin Driver.
                 * jdbc:oracle:thin:@(DESCRIPTION =
                 *  (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.2.35)(PORT = 1521))
                 *  (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.2.45)(PORT = 1521))
                 *  (LOAD_BALANCE = yes)
                 *  (CONNECT_DATA = (SERVER = DEDICATED)(SERVICE_NAME = SRV10)) )
                 *
                 * OCI Driver:
                 * jdbc:oracle:oci:@SRVFAN
                 * In TNSNames.ora file
                 * SRVFAN =  (DESCRIPTION =
                 *  (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.2.35)(PORT = 1521))
                 *  (ADDRESS = (PROTOCOL = TCP)(HOST = 192.168.2.45)(PORT = 1521))
                 *  (LOAD_BALANCE = yes)
                 *  (CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = SRV10)))
                 */
                ods.setURL("ConnectionURL");
                java.util.Properties cacheProperties = new java.util.Properties();
                /**
                 * MinLimit :    The Minimum Number of connection instances the cache holds at all times. default - 0.
                 *               This value WILL NOT initialize the cache with the specified number of connections.
                 *               Use InitialLimit for the initial number of connection instances for the cache to hold.
                 * MaxLimit :    The Maximum Number of connection instances the cache can hold. Default Integer.MAX_VALUE
                 * InitialLimit: The Initial Number of connections the cache creates
                 *               when the cache is created or reinitialized. Default - 0
                 * MaxStatementsLimit : The Maximum Number of statements that a connection keeps open
                 *
                 * InactivityTimeout: The Maximum time that a Physical Connection can be idle in Connection Cache.
                 *                     Value specified is in Seconds. Default 0
                 * TimeToLiveTimeout: The maximum time in seconds that a logical connection can remain open.
                 *                    When TimeToLiveTimeout expires, the logical connection is unconditionally closed,
                 *                    the relevant statement handles are canceled,
                 *                    and the underlying physical connection is returned to the cache for reuse.
                 * AbandonedConnectionTimeout: The Maximum Time that a connection can remain unused before the
                 *                             connection is closed and returned to the cache Default -0
                 * PropertyCheckInterval: The time interval at which the cache manager inspects
                 *                        and enforces all specified cache properties. Default 900 Seconds
                 * ConnectionWaitTimeout: Specifies cache behavior when a connection is requested and
                 *                        there are already MaxLimit connections active. If ConnectionWaitTimeout
                 *                        is greater than zero (0), each connection request waits for the
                 *                        specified number of seconds, or until a connection is returned to the cache.
                 *                        If no connection is returned to the cache before the timeout elapses,
                 *                        the connection request returns null. Default: 0 (no timeout)
                 * LowerThresholdLimit: Sets the lower threshold limit on the cache.
                 *                      The default is 20% of the MaxLimit on the connection cache.
                 * ValidateConnection: Setting ValidateConnection to true causes the connection cache to
                 *                     test every connection it retrieves against the underlying database. Default- false
                 * ClosestConnectionMatch: Setting ClosestConnectionMatch to true causes the connection
                 *                         cache to retrieve the connection with the closest approximation
                 *                         to the specified connection attributes. This can be used in
                 *                         combination with AttributeWeights to specify what is considered a
                 *                         "closest match".  Default - false
                 * AttributeWeights: Sets the weights for each connectionAttribute.
                 *                   Used when ClosestConnectionMatch is set to true to determine which
                 *                   attributes are given highest priority when searching for matches.
                 *                   An attribute with a high weight is given more importance in determining a
                 *                   match than an attribute with a low weight.
                 *                   AttributeWeights contains a set of Key/Value pairs that set the weights for
                 *                   each connectionAttribute for which the user intends to request a connection.
                 *                   The Key is a connectionAttribute and the Value is the weight;
                 *                   a weight must be an integer value greater than 0. The default weight is 1.

                 *
                 */
                cacheProperties.setProperty("MinLimit", "5");
                cacheProperties.setProperty("MaxLimit", "20");
                cacheProperties.setProperty("InitialLimit", "3");
                cacheProperties.setProperty("InactivityTimeout", "1800");
                cacheProperties.setProperty("AbandonedConnectionTimeout", "900");
                cacheProperties.setProperty("MaxStatementsLimit", "10");
                cacheProperties.setProperty("PropertyCheckInterval", "60");

                ods.setConnectionCacheName("FanConnectionCache01");//Name of the connection Cache. This has to be unique
                ods.setConnectionCachingEnabled(true);
                ods.setConnectionCacheProperties(cacheProperties); //set the cache properties.
                /**
                 *
                 */
                ods.setFastConnectionFailoverEnabled(true);//Enable the Fast connection Failover.
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    /**
     * The DataSource which is used to get connections.
     */
    private static OracleDataSource ods = null;
}
