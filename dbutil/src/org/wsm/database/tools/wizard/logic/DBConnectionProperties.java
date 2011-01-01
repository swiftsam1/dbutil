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
package org.wsm.database.tools.wizard.logic;


import java.util.Properties;

import org.wsm.database.tools.DbUtilConstants;

public class DBConnectionProperties {
    public DBConnectionProperties() {
        driverType = "thin";
    }

    public DBConnectionProperties(Properties props) {
        setUserName(props.getProperty(DbUtilConstants.DBUSER));
        setPassword(props.getProperty(DbUtilConstants.DBPASSWORD));
        setHost(props.getProperty(DbUtilConstants.DBHOST));
        setPort(props.getProperty(DbUtilConstants.DBPORT));
        setDatabaseName(props.getProperty(DbUtilConstants.DBDATABASENAME));
        setDatabaseType(props.getProperty(DbUtilConstants.DBUTIL_DATABASE_TYPE));
        if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(getDatabaseType())) {
            setDriverType(props.getProperty(DbUtilConstants.DBUTIL_DRIVER_TYPE));
            if (DbUtilConstants.ORACLE_OCI_DRIVER.equals(getDriverType())) {
                setTnsEntry(props.getProperty(DbUtilConstants.DBUTIL_TNS_ENTRY));
            }
        }

    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getTnsEntry() {
        return tnsEntry;
    }

    public void setTnsEntry(String tnsEntry) {
        this.tnsEntry = tnsEntry;
    }

    /**
     * The Type of Database. Can be Oracle, mysql, Sql Server...
     */
    private String databaseType;

    /**
     * User name used to connect to the database.
     */
    private String userName;

    /**
     * Password for connecting to the database.
     */
    private String password;

    /**
     * The name of the specific database we are interested in.
     */
    private String databaseName;

    /**
     * The Host where the database server is running.
     */
    private String host;

    /**
     * The port on which the database server deamon listens.
     */
    private String port;

    /**
     * This is related to the Oracle database. Can be either Thin or OCI.
     */
    private String driverType;

    /**
     * This is related to the Oracle Database. If the driver type is OCI, The TNSEntry must be specified.
     */
    private String tnsEntry;

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("Database Type = " + databaseType);
        sb.append("; ");
        sb.append("User Name = " + userName);
        sb.append("; ");
        sb.append("Password = (cannot spit this information Sorry) ");
        sb.append("; ");
        sb.append("Host = " + host);
        sb.append("; ");
        sb.append("Port = " + port);
        sb.append("; ");
        sb.append("Database Name = " + databaseName);
        sb.append("; ");
        sb.append("Driver Type = " + driverType);
        sb.append("}");
        return sb.toString();
    }

}
