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
package org.wsm.database.tools.editor.ui;



import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.wsm.database.tools.actions.ActionsHolder;

public class EditorCommandPane extends JPanel {

    private static final long serialVersionUID = 1L;
	public EditorCommandPane() {
        this.setLayout(new BorderLayout());
        commandTextPane = new JTextPane();
        commandTextPane.setDragEnabled(true);
        //commandTextPane.addKeyListener(ActionsHolder.getRunQueryAction());
        commandTextPane.getDocument().addDocumentListener(ActionsHolder.getRunQueryAction());
        commandTextPane.getDocument().addDocumentListener(ActionsHolder.getLoadTestAction());
        commandTextScrollPane = new JScrollPane(commandTextPane);
        this.add(commandTextScrollPane);
    }



    public JTextPane getCommandTextPane() {
        return commandTextPane;
    }

    public void setCommandTextPane(JTextPane commandTextPane) {
        this.commandTextPane = commandTextPane;
    }

    public JScrollPane getCommandTextScrollPane() {
        return commandTextScrollPane;
    }

    public void setCommandTextScrollPane(JScrollPane commandTextScrollPane) {
        this.commandTextScrollPane = commandTextScrollPane;
    }

    private JTextPane commandTextPane;
    private JScrollPane commandTextScrollPane;
}