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
package org.wsm.database.tools.browser.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;

import org.wsm.database.tools.DbUtilConstants;
import org.wsm.database.tools.util.ConnectionManager;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;

public class DatabaseInformationUtil {

    public static IDatabaseInfo getDatabaseInfo() {
        DatabaseInfo info = new DatabaseInfo();
        info.setName(ConnectionManager.currentDbProps.getDatabaseName());
        info.setTablesInfo(getTabelInfo());
        return info;
    }

    public static LinkedList<ITableInfo> getTabelInfo() {
        if (DbUtilConstants.DATABASE_TYPE_MYSQL.equals(ConnectionManager.currentDbProps.getDatabaseType()))
            return getMysqlTableStructure();
        if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(ConnectionManager.currentDbProps.getDatabaseType()))
            return getOracleTableStructure();
        return null;
    }


    private static LinkedList<ITableInfo> getMysqlTableStructure() {
        LinkedList<ITableInfo> tabList = new LinkedList<ITableInfo>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            stmt = con.createStatement();
            StringBuffer sb = new StringBuffer();
            sb.append("SHOW TABLES");
            rs = stmt.executeQuery(sb.toString());

            while (rs.next()) {
                TableInfo info = new TableInfo();
                info.setName(rs.getString(1));
                info.setColumnInfo(getMysqlColumnInfo(info.getName()));
                tabList.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tabList;
    }

    private static LinkedList<ITableInfo> getOracleTableStructure() {
        LinkedList<ITableInfo> tabList = new LinkedList<ITableInfo>();
        Hashtable<String, ITableInfo> tabHt = new Hashtable<String, ITableInfo>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            stmt = con.createStatement();
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT TABLE_NAME TNAME, COLUMN_NAME COLNAME ")
                    .append("FROM ALL_TAB_COLS ")
                    .append("WHERE OWNER ='")
                    .append(ConnectionManager.currentDbProps.getUserName())
                    .append("' ORDER BY TNAME, COLNAME ");
            rs = stmt.executeQuery(sb.toString());

            while (rs.next()) {
                String tabName = rs.getString("TNAME");
                boolean tiExists = false;
                if (!EmptyOrNullStringValidator.isEmpty(tabName)) {
                    TableInfo ti = (TableInfo) tabHt.get(tabName);
                    if (ti == null) {
                        ti = new TableInfo();
                        ti.setName(tabName);
                        ti.setColumnInfo(new LinkedList<IColumnInfo>());
                    } else {
                        tiExists = true;
                    }
                    ColumnInfo ci = new ColumnInfo();
                    ci.setName(rs.getString("COLNAME"));
                    ti.getColumnInfo().add(ci);
                    if (!tiExists) {
                        tabHt.put(tabName, ti);
                    }
                }
            }
            tabList = new LinkedList<ITableInfo>(Collections.list(tabHt.elements()));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return tabList;

    }

    /*public static LinkedList getColumnsInfo(String tabName) {

        if(DbUtilConstants.DATABASE_TYPE_MYSQL.equals(ConnectionManager.currentDbProps.getDatabaseType()))
            return getMysqlColumnInfo(tabName);
        if(DbUtilConstants.DATABASE_TYPE_ORACLE.equals(ConnectionManager.currentDbProps.getDatabaseType()))
                    return getOracleColumnInfo(tabName);
        return null;
    } */

    private static LinkedList<IColumnInfo> getMysqlColumnInfo(String tabName) {
        LinkedList<IColumnInfo> colList = new LinkedList<IColumnInfo>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            stmt = con.createStatement();
            StringBuffer sb = new StringBuffer();
            sb.append("DESC ").append(tabName);
            rs = stmt.executeQuery(sb.toString());

            while (rs.next()) {
                ColumnInfo info = new ColumnInfo();
                info.setName(rs.getString("Field"));
                info.setType(rs.getString("Type"));
                info.setNullable(rs.getBoolean("Null"));
                info.setKey(rs.getBoolean("Key"));
                info.setDefaultValue(rs.getString("Default"));
                colList.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return colList;
    }

    private static LinkedList<IColumnInfo> getOracleColumnInfo(String tabName) {
        LinkedList<IColumnInfo> colList = new LinkedList<IColumnInfo>();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = ConnectionManager.getConnection();
            stmt = con.createStatement();
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT COLUMN_NAME CNAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = '")
                    .append(tabName)
                    .append("' ");
            rs = stmt.executeQuery(sb.toString());

            while (rs.next()) {
                ColumnInfo info = new ColumnInfo();
                info.setName(rs.getString("CNAME"));
                colList.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return colList;
    }


}
