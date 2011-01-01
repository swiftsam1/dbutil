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
package org.wsm.database.tools.editor.logic;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.wsm.database.tools.util.SwingWorker;

public class ProfilerThread extends SwingWorker {

    private static final Logger log = Logger.getLogger(ProfilerThread.class);

    private int threadId;
    private Profiler profiler;
    private Connection con;
    private LoadTestResult ltr;


    public ProfilerThread(int threadId, Profiler profiler, Connection con, QueryTiming qt) {
        log.debug("Initializing new profiler thread");
        this.threadId = threadId;
        this.profiler = profiler;
        this.con = con;
        ltr = new LoadTestResult();
        ltr.setQt(qt);

    }

    /**
     *
     */
    public Object construct() {
        while (true) {
            try {
                if (profiler.canStartThreads()) {
                    //Thread.currentThread().getThreadGroup().notifyAll();
                    log.debug("Started Thread " + threadId);
                    //Increment the Concurrent Thread Count
                    profiler.changeThreadCount(true);
                    //Get the current Timestamp in Milliseconds - StartTime
                    //Execute Query
                    loadTest();
                    //Get the ThreadCount
                    //Get the current Timestamp in Milliseconds - EndTime
                    //Decrement the Concurrent Thread Count
                    profiler.changeThreadCount(false);
                    //Print Results
                    return null;
                } else {
                    //log.debug("Tried to execute but not ready yet");
                    Thread.sleep(1000);
                    //Thread.currentThread().getThreadGroup().notifyAll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     *
     */
    private void loadTest() {
        Statement stmt = null;
        try {

            ltr.getQt().setTimeBeforeCreatingStatement(System.currentTimeMillis());
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            ltr.getQt().setTimeAfterCreatingStatement(System.currentTimeMillis());
            ltr.getQt().setTimeBeforeExecutingQuery(System.currentTimeMillis());
            if(stmt != null) {
            	stmt.execute(profiler.getQuery());
            }
            ltr.getQt().setTimeAfterExecutingQuery(System.currentTimeMillis());
            ltr.setThreadNumber(threadId);
            ltr.setQuery(profiler.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void finished() {
        profiler.addLoadTestAnalysisToResults(ltr);
    }

}
