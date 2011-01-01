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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.wsm.database.tools.Chain;
import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.UIConstants;
import org.wsm.database.tools.actions.ActionsHolder;
import org.wsm.database.tools.util.Destroyer;


public class EditorPane extends JPanel implements ChangeListener, Chain {

	private static final long serialVersionUID = 1L;

	public EditorPane() {
        Destroyer.addToGlobalDesctructionChain(this);
        this.setLayout(new BorderLayout(10, 0));
        editorTabs = new JTabbedPane();
        EditorTab editorTab = new EditorTab();
        editorTabs.addTab("Query " + (editorTabs.getTabCount() + 1), editorTab);
        currentTabIndex = editorTabs.getSelectedIndex();
        editorTabs.addChangeListener(this);
        editorTabs.addChangeListener(ActionsHolder.getRunQueryAction());
        editorTabs.addChangeListener(ActionsHolder.getLoadTestAction());
        editorTabs.addMouseListener(new EditorTabsMouseAdapter());
        this.add(editorTabs, BorderLayout.CENTER);
    }

    public static void addQueryTab(String query) {
        EditorTab editorTab = new EditorTab();
        editorTabs.addTab("Query " + (editorTabs.getTabCount() + 1), editorTab);
        editorTabs.setSelectedComponent(editorTab);
        editorTab.getCommandPane().getCommandTextPane().setText(query);
    }


    public static JTabbedPane getEditorTabs() {
        return editorTabs;
    }

    public static synchronized void setEditorTabs(JTabbedPane editorTabs) {
        EditorPane.editorTabs = editorTabs;
    }

    public static synchronized int getCurrentTabIndex() {
        return currentTabIndex;
    }

    public static synchronized void setCurrentTabIndex(int currentTabIndex) {
        EditorPane.currentTabIndex = currentTabIndex;
    }

    public static synchronized EditorTab getCurrentEditorTab() {
        if (editorTabs == null || editorTabs.getComponentCount() <= 0)
            return null;
        else
            return (EditorTab) editorTabs.getSelectedComponent();
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JTabbedPane) {
            setCurrentTabIndex(((JTabbedPane) e.getSource()).getSelectedIndex());
        }
    }

    public static void showLoadTestPane() {
        loadTestPane = new LoadTestPropertiesDialog(StartMe.getContainer());
        loadTestPane.setVisible(true);
    }

    public static LoadTestPropertiesDialog getLoadTestPane() {
        return loadTestPane;
    }

    public static void setLoadTestPane(LoadTestPropertiesDialog loadTestPane) {
        EditorPane.loadTestPane = loadTestPane;
    }

    public void destroy() {
        editorTabs.removeAll();
        this.remove(editorTabs);
    }

    public static void addQueryTab() {
        addQueryTab("");
    }

    private static LoadTestPropertiesDialog loadTestPane;
    private static JTabbedPane editorTabs;
    private static int currentTabIndex = -1;

    public class EditorTabsMouseAdapter extends MouseAdapter implements ActionListener {
        public EditorTabsMouseAdapter() {
            super();
        }

        /**
         * Invoked when the mouse has been clicked on a component.
         */
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            activateEditorPopup(e);
        }

        /**
         * Invoked when the mouse enters a component.
         */
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            activateEditorPopup(e);
        }

        /**
         * Invoked when the mouse exits a component.
         */
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            activateEditorPopup(e);
        }

        /**
         * Invoked when a mouse button has been pressed on a component.
         */
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            activateEditorPopup(e);
        }

        /**
         * Invoked when a mouse button has been released on a component.
         */
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            activateEditorPopup(e);
        }

        public void activateEditorPopup(MouseEvent e) {

            if(SwingUtilities.isRightMouseButton(e)) {
                if(e.getSource() instanceof JTabbedPane) {
                    editorTabs.setSelectedIndex(editorTabs.indexAtLocation(e.getX(), e.getY()));
                    JPopupMenu epm = new JPopupMenu("Editor");

                    JMenuItem closeEditorMI = new JMenuItem("Close");
                    closeEditorMI.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
                    closeEditorMI.addActionListener(this);

                    epm.add(closeEditorMI);
                    epm.show(editorTabs,e.getX(),e.getY());
                }
            }
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {

            if("Close".equals(e.getActionCommand())) {
                editorTabs.remove(editorTabs.getSelectedComponent());
            }
        }
    }


}
