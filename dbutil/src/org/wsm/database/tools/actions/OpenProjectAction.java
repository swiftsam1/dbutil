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
package org.wsm.database.tools.actions;


import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.log4j.Logger;
import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.wizard.logic.ProjectProperties;

public class OpenProjectAction extends AbstractAction {
    private static final Logger log = Logger.getLogger(OpenProjectAction.class);


    public OpenProjectAction(String name) {
        super(name);
        //this.parent = parent;
    }

    public OpenProjectAction(String name, Icon icon) {
        super(name, icon);
        //this.parent = parent;
    }

    //private Component parent;


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {

        if ("Open".equals(e.getActionCommand())) {

            JFileChooser propsFileChooser = new JFileChooser(System.getProperty("user.dir"));
            propsFileChooser.setDialogTitle("Select the Database Connection properties file(.dbp)");
            propsFileChooser.addChoosableFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    if (f.isDirectory())
                        return true;
                    String filename = f.getName();
                    return filename.endsWith(".dbp");
                }

                public String getDescription() {
                    return "*.dbp";
                }
            });

            int openVal = propsFileChooser.showOpenDialog(StartMe.getContainer());
            if (openVal == JFileChooser.APPROVE_OPTION) {
                File propertiesFile = propsFileChooser.getSelectedFile();
                StartMe.getContainer().openProject(new ProjectProperties(getProjectName(propertiesFile.getAbsolutePath()), propertiesFile.getAbsolutePath()));
            }
        }

    }
    private static String getProjectName(final String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
        return fileName.substring(0,fileName.indexOf("dbp")-1);
    }

}
