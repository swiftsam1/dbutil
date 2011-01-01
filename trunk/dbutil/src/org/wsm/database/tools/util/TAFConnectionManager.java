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

public class TAFConnectionManager extends OracleBaseLoadTestConnectionManager{

    public Connection getConnection(String keyName) throws DBConnectionFailedException, EmptyDataSourceException {
        try {
            Connection conn = super.getConnection(keyName);            
            TAFConnectionRegistrationUtil.RegisterFailOver(conn);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DBConnectionFailedException("Failed to get connection");
        }
    }

}
