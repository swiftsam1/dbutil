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

import java.sql.SQLException;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleConnectionCacheCallback;

import org.apache.log4j.Logger;

public class OracleConnectionCacheCallbackImpl implements OracleConnectionCacheCallback {
    Logger log = Logger.getLogger(OracleConnectionCacheCallbackImpl.class);
    public boolean handleAbandonedConnection(OracleConnection oracleConnection, Object o) {
        log.debug("Abandoned connection event is being called on connection " + oracleConnection.toString());
        return false;
    }

    public void releaseConnection(OracleConnection oracleConnection, Object o) {
        try {
            log.info("Releasing connection event is triggered for connection " + oracleConnection.getConnectionAttributes().toString());
            if(o!=null) {
                log.debug("Object associate as parameter is " + o.getClass());
            }
            log.debug("Reason for releasing conneciton is " + oracleConnection.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
