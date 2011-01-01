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
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.editor.ui.EditorPane;
import org.wsm.database.tools.editor.ui.QueryResultPane;
import org.wsm.database.tools.query.CatalogManager;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;
import org.wsm.database.tools.util.StateVerificationUtil;

public class RunQueryAction extends AbstractAction implements DocumentListener, ChangeListener {

    /**
     * Defines an <code>Action</code> object with the specified
     * description string and a default icon.
     */
    public RunQueryAction(String name) {
        super(name);
        this.setEnabled(!StateVerificationUtil.isCurrentEditorEmpty());
    }

    /**
     * Defines an <code>Action</code> object with the specified
     * description string and a the specified icon.
     */
    public RunQueryAction(String name, Icon icon) {
        super(name, icon);
        this.setEnabled(!StateVerificationUtil.isCurrentEditorEmpty());
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if ("Run".equals(e.getActionCommand())) {
            if (EditorPane.getCurrentEditorTab() != null) {
//                SwingUtilities.invokeLater(
//                      new Runnable() {
//                        public void run() {
                String command = EditorPane.getCurrentEditorTab().getCommandPane().getCommandTextPane().getText();
                if (EmptyOrNullStringValidator.isEmpty(command)) {
                    JOptionPane.showMessageDialog(StartMe.getContainer(),
                            "Please enter the qurey to be profiled",
                            "Required Fields",
                            JOptionPane.OK_OPTION);
                    return;
                }
                CatalogManager cm = new CatalogManager();
                QueryResultPane erp = new QueryResultPane(command);
                EditorPane.getCurrentEditorTab().setResultsPane(erp);
                //erp.startProc();
//                             }
//                       }
//                );

            }
        }

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
