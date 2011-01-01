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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.wsm.database.tools.util.RandomStringGenerator;

public class EditorTab extends JPanel implements PropertyChangeListener {

    public EditorTab() {
        this.setLayout(new BorderLayout());
        editorSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        commandPane = new EditorCommandPane();
        editorSplitPane.setTopComponent(commandPane);
        editorSplitPane.setResizeWeight(.5);
        editorSplitPane.setContinuousLayout(true);
        editorSplitPane.setDividerSize(8);
        this.add(editorSplitPane, BorderLayout.CENTER);
        this.addPropertyChangeListener(this);
        RandomStringGenerator rsg = new RandomStringGenerator();
        randomPropertyName = rsg.generateRandomString();
    }

    public JSplitPane getEditorSplitPane() {
        return editorSplitPane;
    }

    public void setEditorSplitPane(JSplitPane editorSplitPane) {
        this.editorSplitPane = editorSplitPane;
    }

    public EditorCommandPane getCommandPane() {
        return commandPane;
    }

    public void setCommandPane(EditorCommandPane commandPane) {
        this.commandPane = commandPane;
    }

    public QueryResultPane getResultsPane() {
        return resultsPane;
    }

    public void setResultsPane(QueryResultPane resultsPane) {
        firePropertyChange(randomPropertyName, this.resultsPane, resultsPane);
        this.resultsPane = resultsPane;
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */

    public void propertyChange(PropertyChangeEvent evt) {
        if (randomPropertyName.equals(evt.getPropertyName())) {
            editorSplitPane.setBottomComponent((QueryResultPane) evt.getNewValue());
            editorSplitPane.repaint();
        }
    }

    private EditorCommandPane commandPane;
    private QueryResultPane resultsPane;
    private JSplitPane editorSplitPane;
    private String randomPropertyName;
}
