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

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.wsm.database.tools.wizard.ui.ProjectWizard;

public class NewProjectAction extends AbstractAction {

        public NewProjectAction() {
        }

        public NewProjectAction(String name) {
            super(name);
        }

        public NewProjectAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {

            if (ActionsHolder.NEW_PROJECT.equals(e.getActionCommand())) {
                ProjectWizard pw = new ProjectWizard();
                pw.setVisible(true);
            }

        }

    }
