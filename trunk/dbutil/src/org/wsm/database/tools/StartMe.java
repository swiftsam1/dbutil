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

import java.awt.Color;
import java.io.File;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;

public class StartMe {
    private static final Logger log = Logger.getLogger(DBUtilContainer.class);


    public static void main(String[] args) {
        String envUtilHome = System.getProperty("DBUTIL_HOME");
        String fileName = envUtilHome + File.separator + "lib" + File.separator + "log4j.properties";
        PropertyConfigurator.configure(fileName);

        if (EmptyOrNullStringValidator.isEmpty(envUtilHome)) {
            log.fatal("DBUTIL_HOME env variable is not set. Usage - java -DDBUTIL_HOME=path -cp classpath main class");
            throw new Error("DBUTIL_HOME is required");
        }
        log.debug("\n\n\n");
        log.debug("---------Logging for a new Execution-----------");
        log.debug("Using DBUTIL_HOME " + envUtilHome);

        container = new DBUtilContainer(envUtilHome);
        container.initialize();
        //DBUtilContainer.setDbUtilHome(envUtilHome);
        container.getContentPane().setBackground(Color.GRAY);
        container.setVisible(true);
        container.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static DBUtilContainer getContainer() {
        return container;
    }

    public static void setContainer(DBUtilContainer container) {
        StartMe.container = container;
    }

    private static DBUtilContainer container;

}
