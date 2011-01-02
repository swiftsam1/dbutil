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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.ConnectionPoolDataSource;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.apache.commons.dbcp.datasources.PerUserPoolDataSource;
import org.apache.log4j.Logger;
import org.wsm.database.tools.Chain;
import org.wsm.database.tools.DbUtilConstants;
import org.wsm.database.tools.wizard.logic.DBConnectionProperties;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class ConnectionManager implements Chain {

    private static Logger log = Logger.getLogger(ConnectionManager.class);

    public static Connection getConnection(DBConnectionProperties props)
            throws DBConnectionFailedException, ConnectionPoolDataSourceCreationException {
        if (props == null) {
            return null;
        }
        currentDbProps = props;
        driverType = props.getDriverType();
        loadDriver(props.getDatabaseType());
        connUrl = getConnectionURL(props);
        userName = props.getUserName();
        password = props.getPassword();
        databaseName = props.getDatabaseName();
        determineDataSource(props);
        //determineConnectionManager(props);
        Connection con;
        con = getConnection();
        if (con == null) {
            throw new DBConnectionFailedException("Failed to establish connection");
        }
        return con;
    }

    public static boolean loadProperties(DBConnectionProperties props)
            throws DBConnectionFailedException, ConnectionPoolDataSourceCreationException {
        if (props == null) {
            return false;
        }
        currentDbProps = props;
        driverType = props.getDriverType();
        loadDriver(props.getDatabaseType());
        connUrl = getConnectionURL(props);
        userName = props.getUserName();
        password = props.getPassword();
        databaseName = props.getDatabaseName();
        determineDataSource(props);
        //determineConnectionManager(props);
        Connection con;
        con = getConnection();
        boolean returnVal;
        if (con == null) {
            returnVal = false;
        } else {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } 
            returnVal = true;
        }

        return returnVal;
    }

    private static Driver loadDriver(final String dbType) throws DBConnectionFailedException {
        Driver temp = null;
        if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(dbType)) {
            try {
                temp = new oracle.jdbc.driver.OracleDriver();
                DriverManager.registerDriver(temp);
            } catch (SQLException e) {
                throw new DBConnectionFailedException("Unable to register" + dbType + "Driver");
            }
        } else if (DbUtilConstants.DATABASE_TYPE_MYSQL.equals(dbType)) {
            try {
                temp = new com.mysql.jdbc.Driver();
                DriverManager.registerDriver(temp);
            } catch (SQLException e) {
                throw new DBConnectionFailedException("Unable to register" + dbType + "Driver");
            }
        }
        return temp;
    }

    private static String getConnectionURL(final DBConnectionProperties properties) {
        StringBuffer sb = new StringBuffer();
        sb.append("jdbc:").append(properties.getDatabaseType()).append(":");
        if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(properties.getDatabaseType())) {
            if (DbUtilConstants.ORACLE_OCI_DRIVER.equalsIgnoreCase(properties.getDriverType())) {
                sb.append("oci:@").append(properties.getTnsEntry());
            } else if (DbUtilConstants.ORACLE_THIN_DRIVER.equals(properties.getDriverType())) {
                sb.append("thin:@");
                sb.append(properties.getHost());
                sb.append(":");
                sb.append(properties.getPort());
                sb.append(":");
                sb.append(properties.getDatabaseName());
            }
        } else if (DbUtilConstants.DATABASE_TYPE_MYSQL.equals(properties.getDatabaseType())) {
            sb.append("//").append(properties.getHost()).append(":").append(properties.getPort()).append("/").append(properties.getDatabaseName());
        }
        return sb.toString();
    }

    public static boolean isConnectionPropertiesValid(DBConnectionProperties properties) throws DBConnectionFailedException {
        if (properties == null) {
            return false;
        }
        Driver driver = loadDriver(properties.getDatabaseType());
        String connectionURL = getConnectionURL(properties);
        Connection con = null;
        try {
            con = DriverManager.getConnection(connectionURL, properties.getUserName(), properties.getPassword());
            return con != null;
        } catch (SQLException e) {
            throw new DBConnectionFailedException("Exception occured while getting connection " + e.getCause());
        } finally {
            try {
                if(con != null) {con.close();}
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void determineDataSource(DBConnectionProperties props)
            throws ConnectionPoolDataSourceCreationException {
        File f = new File(DbUtilConstants.DBUTIL_HOME);
        File f2 = new File(f, "dataSourceLogs.txt");
        PrintWriter pw = null;
        try {
            if (!f.exists()) {
                if (f.canWrite()) {
                    f2.createNewFile();
                }
            }
            if (f2.exists())
                pw = new PrintWriter(new FileOutputStream(f2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ConnectionPoolDataSource cpds = null;


        if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(props.getDatabaseType())) {
            try {
                OracleConnectionPoolDataSource tempOcpds = new OracleConnectionPoolDataSource();
                tempOcpds.setUser(userName);
                tempOcpds.setPassword(password);
                tempOcpds.setURL(connUrl);
                cpds = tempOcpds;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (DbUtilConstants.DATABASE_TYPE_MYSQL.equals(props.getDatabaseType())) {
            MysqlConnectionPoolDataSource tempCpds = new MysqlConnectionPoolDataSource();
            tempCpds.setUser(userName);
            tempCpds.setPassword(password);
            tempCpds.setURL(connUrl);
            cpds = tempCpds;
        }
        if (pw != null) {
            try {
                if (cpds != null) {
                    cpds.setLogWriter(pw);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (getPupds() != null) {
            getPupds().close();
        }
        setPupds(null);
        if (getPupds() == null)
            setPupds(new PerUserPoolDataSource());
        if (cpds != null)
            getPupds().setConnectionPoolDataSource(cpds);
        else
            throw new ConnectionPoolDataSourceCreationException("Unable to create Data Source");

    }

    public static Connection getConnection() throws DBConnectionFailedException {
        Connection conn;
        try {
            conn = getPupds().getConnection();
            return conn;
        } catch (SQLException e) {
           throw new DBConnectionFailedException(e.getMessage());
        }
    }

    public synchronized static PerUserPoolDataSource getPupds() {
        return pupds;
    }

    public synchronized static void setPupds(PerUserPoolDataSource pupds) {
        ConnectionManager.pupds = pupds;
    }

    /*public static void determineConnectionManager(DBConnectionProperties props) {
       if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(props.getDatabaseType())) {
           if (DbUtilConstants.ORACLE_OCI_DRIVER.equalsIgnoreCase(driverType) ||
                   DbUtilConstants.ORACLE_THIN_DRIVER.equalsIgnoreCase(driverType)) {
               baseManager = DbUtilConstants.DATABASE_TYPE_ORACLE;
           }
       } else if (DbUtilConstants.DATABASE_TYPE_MYSQL.equals(props.getDatabaseType())) {
            baseManager = DbUtilConstants.DATABASE_TYPE_MYSQL;
       }
   } */

    public static String getConnUrl() {
        return connUrl;
    }

    public static void setConnUrl(String connUrl) {
        ConnectionManager.connUrl = connUrl;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        ConnectionManager.userName = userName;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        ConnectionManager.password = password;
    }

    public void destroy() {
        connUrl = null;
        userName = null;
        password = null;
        host = null;
        port = null;
        currentDbProps = null;
        driverType = null;
        getPupds().close();
    }


    static String connUrl;
    static String userName;
    static String password;
    static String host;
    static String port;
    static String databaseName;
    static String driverType;
    //protected static ConnectionPoolDataSource cpds;
    protected static PerUserPoolDataSource pupds;
    //private static String baseManager;
    public static DBConnectionProperties currentDbProps;

}
