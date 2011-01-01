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

import java.io.File;

public class ProjectProperties {


    public ProjectProperties() {
    }


    public ProjectProperties(String projectName, String projectPath) {
        this.projectName = projectName;
        this.projectPath = projectPath;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getJustProjectDir() {
        return getJustDirPath(projectPath);
    }


    public static String getJustDirPath(final String projectName) {
        if(projectName.indexOf(".dbp")>0) {
            return projectName.substring(0, projectName.lastIndexOf(File.separator));
        } else{
            return projectName;
        }
    }

    private String projectName;
    private String projectPath;


    public String toString() {
        return "ProjectProperties{" +
                "projectName='" + projectName + '\'' +
                ", projectPath='" + projectPath + '\'' +
                '}';
    }
}
