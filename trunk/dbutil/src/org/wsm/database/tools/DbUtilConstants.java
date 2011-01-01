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

import java.io.File;

public interface DbUtilConstants {

    public static final String DBUSER = "dbUser";

    public static final String DBPASSWORD = "dbPassword";

    public static final String DBHOST = "dbHost";

    public static final String DBPORT = "dbPort";

    public static final String DBDATABASENAME = "dbDatabaseName";

    public static String DBUTIL_HOME = "";

    public static final String DBUTIL_DRIVER_TYPE = "dbDriverType";
    
    public static final String DBUTIL_TNS_ENTRY = "dbTnsEntry";

    public static final String DBUTIL_DATABASE_TYPE = "dbType";

    public static final String ORACLE_OCI_DRIVER = "Oracle OCI";

    public static final String ORACLE_THIN_DRIVER = "Oracle Thin";

    public static final String SELECT_ONE = "Select One...";

    public static final String DATABASE_TYPE_ORACLE = "Oracle";

    public static final String DATABASE_TYPE_MYSQL = "MySQL";

    public static final String ICON_IMAGES_LOCATION = StartMe.getContainer().getDbUtilHome() + File.separator + "config" + File.separator + "images" + File.separator + "icons" + File.separator;
    public static final String RECENT_FILES_LOCATION = StartMe.getContainer().getDbUtilHome() + File.separator + "config" + File.separator + "history" + File.separator;



}
