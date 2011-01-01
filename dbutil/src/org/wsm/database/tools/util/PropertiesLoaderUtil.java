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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.wsm.database.tools.wizard.logic.DBConnectionProperties;

public class PropertiesLoaderUtil {

    public static Properties loadProperties(File f) throws FileNotLoadbleException {
        Properties prts = new Properties();
        try {
            FileInputStream fis = new FileInputStream(f);
            prts.load(fis);
        } catch (FileNotFoundException e1) {
            throw new FileNotLoadbleException("File Doesnot Exist");
        } catch (IOException ioe) {
            throw new FileNotLoadbleException("Error loading file");
        }
        return prts;
    }

    public static boolean isProjectValid(File f) throws FileNotLoadbleException, DBConnectionFailedException {
        DBConnectionProperties dbProps= new DBConnectionProperties(loadProperties(f));
        return ConnectionManager.isConnectionPropertiesValid(dbProps);
    }
}
