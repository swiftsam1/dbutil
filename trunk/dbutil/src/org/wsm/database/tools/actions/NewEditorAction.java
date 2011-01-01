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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.wsm.database.tools.DBUtilContainer;
import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.editor.ui.EditorPane;

public class NewEditorAction extends AbstractAction implements PropertyChangeListener {
    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public NewEditorAction() {
        this.initialize();
    }

    /**
     * Defines an <code>Action</code> object with the specified
     * description string and a default icon.
     */
    public NewEditorAction(String name) {
        super(name);
        this.initialize();
    }

    /**
     * Defines an <code>Action</code> object with the specified
     * description string and a the specified icon.
     */
    public NewEditorAction(String name, Icon icon) {
        super(name, icon);
        this.initialize();
    }

    public void initialize() {
        StartMe.getContainer().addPropertyChangeListener(this);
        this.setEnabled(amISupposedToBeEnabled());
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if(ActionsHolder.NEW_EDITOR.equals(e.getActionCommand())) {
            EditorPane.addQueryTab();
        }
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */

    public void propertyChange(PropertyChangeEvent evt) {
        if(DBUtilContainer.PROJECT_STATUS.equals(evt.getPropertyName())) {
            this.setEnabled(amISupposedToBeEnabled());
        }
    }

    private boolean amISupposedToBeEnabled() {
        return (StartMe.getContainer() != null)&& StartMe.getContainer().isProjectActive();
    }
}
