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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.editor.ui.EditorPane;
import org.wsm.database.tools.util.StateVerificationUtil;

public class LoadTestAction extends AbstractAction implements MouseListener, ChangeListener, DocumentListener {

    /**
     * Defines an <code>Action</code> object with a default
     * description string and default icon.
     */
    public LoadTestAction() {
        this.setEnabled(!StateVerificationUtil.isCurrentEditorEmpty());

    }

    /**
     * Defines an <code>Action</code> object with the specified
     * description string and a default icon.
     * @param name Name of LoadTest button
     */
    public LoadTestAction(String name) {
        super(name);
        this.setEnabled(!StateVerificationUtil.isCurrentEditorEmpty());

    }

    /**
     * Defines an <code>Action</code> object with the specified
     * description string and a the specified icon.
     * @param name name
     * @param icon icon
     */
    public LoadTestAction(String name, Icon icon) {
        super(name, icon);
        this.setEnabled(!StateVerificationUtil.isCurrentEditorEmpty());

    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if("Load Test".equals(e.getActionCommand())) {
            EditorPane.showLoadTestPane();
        }
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {
        if(isEnabled()) {
            StartMe.getContainer().setStatusMessage("Click to perform LoadTest for the query");
        } else {
            StartMe.getContainer().setStatusMessage("Please enter the Query to perform Load Test");
        }
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {
        StartMe.getContainer().setStatusMessageLbl(" ");
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    public void changedUpdate(DocumentEvent e) {
        changeStatusOfButton();
    }

    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    public void insertUpdate(DocumentEvent e) {
        changeStatusOfButton();
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    public void removeUpdate(DocumentEvent e) {
        changeStatusOfButton();
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    public void stateChanged(ChangeEvent e) {
        changeStatusOfButton();
    }

    private void changeStatusOfButton() {
        this.setEnabled(!StateVerificationUtil.isCurrentEditorEmpty());
    }
}
