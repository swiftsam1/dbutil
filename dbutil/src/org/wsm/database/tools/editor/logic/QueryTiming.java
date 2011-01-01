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


public class QueryTiming {


    public long getTimeBeforeConnection() {
        return timeBeforeConnection;
    }

    public void setTimeBeforeConnection(long timeBeforeConnection) {
        this.timeBeforeConnection = timeBeforeConnection;
    }

    public long getTimeAfterConnection() {
        return timeAfterConnection;
    }

    public void setTimeAfterConnection(long timeAfterConnection) {
        this.timeAfterConnection = timeAfterConnection;
    }

    public long getTimeBeforeExecutingQuery() {
        return timeBeforeExecutingQuery;
    }

    public void setTimeBeforeExecutingQuery(long timeBeforeExecutingQuery) {
        this.timeBeforeExecutingQuery = timeBeforeExecutingQuery;
    }

    public long getTimeAfterExecutingQuery() {
        return timeAfterExecutingQuery;
    }

    public void setTimeAfterExecutingQuery(long timeAfterExecutingQuery) {
        this.timeAfterExecutingQuery = timeAfterExecutingQuery;
    }

    public long getTimeBeforeCreatingStatement() {
        return timeBeforeCreatingStatement;
    }

    public void setTimeBeforeCreatingStatement(long timeBeforeCreatingStatement) {
        this.timeBeforeCreatingStatement = timeBeforeCreatingStatement;
    }

    public long getTimeAfterCreatingStatement() {
        return timeAfterCreatingStatement;
    }

    public void setTimeAfterCreatingStatement(long timeAfterCreatingStatement) {
        this.timeAfterCreatingStatement = timeAfterCreatingStatement;
    }

    public long getQueryExecutionTime() {
        return (timeAfterExecutingQuery-timeBeforeExecutingQuery);
    }

    public long getConnectionCreationTime() {
        return(timeAfterConnection - timeBeforeConnection);
    }

    /**
     * The System time before getting Connection.
     */
    private long timeBeforeConnection;

    /**
     * The System time after getting Connection
     */
    private long timeAfterConnection;

    /**
     * The System time before executing Query.
     */
    private long timeBeforeExecutingQuery;

    /**
     * The System time after executing query.
     */
    private long timeAfterExecutingQuery;

    /**
     * Time before creating Statement
     */
    private long timeBeforeCreatingStatement;

    /**
     * Time after creating statement
     */
    private long timeAfterCreatingStatement;

}
