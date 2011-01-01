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

import org.wsm.database.tools.Chain;

public class ActionsHolder implements Chain {

    static {
        init();
    }
    public static void init() {
        openProjectAction = new OpenProjectAction("Open");
        newProjectAction = new NewProjectAction(NEW_PROJECT);
        runQueryAction = new RunQueryAction("Run");
        loadTestAction = new LoadTestAction("Load Test");
        newEditorAction= new NewEditorAction(NEW_EDITOR);
        initialized = true;

    }

    public static OpenProjectAction getOpenProjectAction() {
        return openProjectAction;
    }

    public static void setOpenProjectAction(OpenProjectAction openProjectAction) {
        ActionsHolder.openProjectAction = openProjectAction;
    }

    public static NewProjectAction getNewProjectAction() {
        return newProjectAction;
    }

    public static void setNewProjectAction(NewProjectAction newProjectAction) {
        ActionsHolder.newProjectAction = newProjectAction;
    }

    public static RegisterONSMonitorAction getRegisterONSMonitorAction() {
        return registerONSMonitorAction;
    }

    public static void setRegisterONSMonitorAction(RegisterONSMonitorAction registerONSMonitorAction) {
        ActionsHolder.registerONSMonitorAction = registerONSMonitorAction;
    }

    public static RunQueryAction getRunQueryAction() {
        return runQueryAction;
    }

    public static void setRunQueryAction(RunQueryAction runQueryAction) {
        ActionsHolder.runQueryAction = runQueryAction;
    }

    public static LoadTestAction getLoadTestAction() {
        return loadTestAction;
    }

    public static void setLoadTestAction(LoadTestAction loadTestAction) {
        ActionsHolder.loadTestAction = loadTestAction;
    }

    public static NewEditorAction getNewEditorAction() {
        return newEditorAction;
    }

    public static void setNewEditorAction(NewEditorAction newEditorAction) {
        ActionsHolder.newEditorAction = newEditorAction;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void setInitialized(boolean initialized) {
        ActionsHolder.initialized = initialized;
    }

    public void destroy() {
        openProjectAction = null;
        newProjectAction=null;
        registerONSMonitorAction = null;
        runQueryAction = null;
        loadTestAction =null;
        newEditorAction = null;
        initialized = false;


    }

    private static OpenProjectAction openProjectAction;
    private static NewProjectAction newProjectAction;
    private static RegisterONSMonitorAction registerONSMonitorAction;
    private static RunQueryAction runQueryAction;
    private static LoadTestAction loadTestAction;
    private static NewEditorAction newEditorAction;
    private static boolean initialized;

    public static final String NEW_PROJECT = "Project";
    public static final String NEW_EDITOR = "Editor";
}
