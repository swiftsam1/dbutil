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

public class DBConnectionFailedException extends SQLException {

    /**
     * Constructs an <code>SQLException</code> object;
     * the <code>reason</code> field defaults to null,
     * the <code>SQLState</code> field defaults to <code>null</code>, and
     * the <code>vendorCode</code> field defaults to 0.
     */
    public DBConnectionFailedException() {
    }

    /**
     * Constructs an <code>SQLException</code> object with a reason;
     * the <code>SQLState</code> field defaults to <code>null</code>, and
     * the <code>vendorCode</code> field defaults to 0.
     *
     * @param reason a description of the exception
     */
    public DBConnectionFailedException(String reason) {
        super(reason);
    }

    /**
     * Constructs an <code>SQLException</code> object with the given reason and
     * SQLState; the <code>vendorCode</code> field defaults to 0.
     *
     * @param reason   a description of the exception
     * @param SQLState an XOPEN or SQL 99 code identifying the exception
     */
    public DBConnectionFailedException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    /**
     * Constructs a fully-specified <code>SQLException</code> object.
     *
     * @param reason     a description of the exception
     * @param SQLState   an XOPEN or SQL 99 code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     */
    public DBConnectionFailedException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }
}
