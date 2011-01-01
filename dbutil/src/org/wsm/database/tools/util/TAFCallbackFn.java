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

import oracle.jdbc.OracleOCIFailover;

import org.apache.log4j.Logger;

public class TAFCallbackFn implements OracleOCIFailover {

    private static Logger log = Logger.getLogger(TAFCallbackFn.class);


    public TAFCallbackFn() {
        log.debug("Created an object now");
    }

    public int callbackFn(Connection connection, Object o, int i, int i1) {

        String foType[] = {
            "SESSION",
            "SELECT",
            "NONE"
        };
        String foEvent[] = {
            "BEGIN",
            "END",
            "ABORT",
            "REAUTHORISE",
            "ERROR",
            "RETRY",
            "UNKNOWN"
        };

        try {
            log.info("The Connection for which the failover Occured is :" + connection.getMetaData().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("FAILOVER TYPE is :  " + foType[i-1]);
        log.info("FAILOVER EVENT is : " + foEvent[i1-1]);

        boolean failOver = false;

        switch (i1) {

            case FO_BEGIN:
                log.info("Failover event is begin ");
                break;

            case FO_END:
                log.info("Failover event in end");

                // Failover has ended. Reset the flag
                failOver = false;
                return 0;

            case FO_ABORT:
                log.info("Failover is aborted");
                break;

            case FO_REAUTH:
                log.info("Failover needs reauthorization");
                break;

            case FO_ERROR:
                if (!failOver) {
                    log.info("Failover error occured and so the failover is tested again");
                    // Set the flag.
                    failOver = true;
                }
                // Retry by returning the value FO_RETRY.
                return FO_RETRY;

            default:
                log.info("Default is returned");
        }
        return 0;
    }

}
