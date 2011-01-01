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
package org.wsm.database.tools.toolbars;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;
import org.wsm.database.tools.actions.ActionsHolder;

public class ToolbarManager extends JToolBar {

    private static final Logger log = Logger.getLogger(ToolbarManager.class);

    public ToolbarManager() {
        super("toolbar",JToolBar.HORIZONTAL);
        log.debug("Initializing Toolbar");
        //this.add(ActionsHolder.getNewProjectAction());
        newPm = new JPopupMenu("New");
        newPm.add(new JMenuItem(ActionsHolder.getNewProjectAction()));
        newPm.add(new JMenuItem(ActionsHolder.getNewEditorAction()));
        newButton = new JButton("New");
        //newButton.add(newPm);
        newButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             */
            public void actionPerformed(ActionEvent e) {
                newPm.show((JComponent)e.getSource(),0, 0+ newButton.getHeight());

            }
        });
        this.add(ActionsHolder.getOpenProjectAction());
        runQueryButton = new JButton(ActionsHolder.getRunQueryAction());
        loadTestButton = new JButton(ActionsHolder.getLoadTestAction());
        loadTestButton.addMouseListener(ActionsHolder.getLoadTestAction());
        this.add(newButton);
        this.addSeparator();
        this.add(runQueryButton);
        this.add(loadTestButton);
        this.setFloatable(false);
        this.setRollover(true);
        this.setBorderPainted(false);
    }

    public JToolBar getToolbarHolder() {
        return toolbarHolder;
    }

    public void setToolbarHolder(JToolBar toolbarHolder) {
        this.toolbarHolder = toolbarHolder;
    }

    public static JButton getLoadTestButton() {
        return loadTestButton;
    }

    public static void setLoadTestButton(JButton loadTestButton) {
        ToolbarManager.loadTestButton = loadTestButton;
    }

    public static JButton getRunQueryButton() {
        return runQueryButton;
    }

    public static void setRunQueryButton(JButton runQueryButton) {
        ToolbarManager.runQueryButton = runQueryButton;
    }

    JPopupMenu newPm;
    JButton newButton;
    private static JButton loadTestButton, runQueryButton;
    private JToolBar toolbarHolder;
}
