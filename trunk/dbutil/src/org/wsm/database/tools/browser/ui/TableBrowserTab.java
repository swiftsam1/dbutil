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
package org.wsm.database.tools.browser.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.UIConstants;
import org.wsm.database.tools.browser.logic.ColumnInfo;
import org.wsm.database.tools.browser.logic.DatabaseBrowserTreeCellRenderer;
import org.wsm.database.tools.browser.logic.DatabaseInfo;
import org.wsm.database.tools.browser.logic.TableInfo;
import org.wsm.database.tools.editor.ui.EditorPane;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;
import org.wsm.database.tools.util.QueryBuilderUtil;

public class TableBrowserTab extends JPanel {

    public TableBrowserTab() {
        super();
        this.setLayout(new BorderLayout());
        DatabaseInfo info = (DatabaseInfo)StartMe.getContainer().getDatabaseInfo();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(info.getName());

        LinkedList tabInfoList = info.getTablesInfo();
        Iterator it = tabInfoList.iterator();
        while (it.hasNext()) {
            TableInfo ti = (TableInfo) it.next();
            DefaultMutableTreeNode table = new DefaultMutableTreeNode(ti);
            LinkedList columns = ti.getColumnInfo();
            Iterator colIter = columns.iterator();
            while (colIter.hasNext()) {
                table.add(new DefaultMutableTreeNode(colIter.next()));
            }
            root.add(table);
        }
        tableTree = new JTree(root);
        tableTree.setCellRenderer(new DatabaseBrowserTreeCellRenderer());
        tableTree.addMouseListener(new TableBrowserMouseAdapter());
        //tabTree.setVisibleRowCount(30);
        JScrollPane tableTab = new JScrollPane(tableTree);
        tableTab.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableTab.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableTab.setAutoscrolls(true);
        //tableTab.setPreferredSize(new Dimension(150,800));
        this.add(tableTab, BorderLayout.CENTER);
    }

    public JTree getTableTree() {
        return tableTree;
    }

    public void setTableTree(JTree tableTree) {
        this.tableTree = tableTree;
    }

    private JTree tableTree;



    public class TableBrowserMouseAdapter extends MouseAdapter implements ActionListener {
        public TableBrowserMouseAdapter() {
            super();
        }

        /**
         * Invoked when the mouse has been clicked on a component.
         */
        public void mouseClicked(MouseEvent e) {
            workWithPopup(e);
        }

        /**
         * Invoked when the mouse enters a component.
         */
        public void mouseEntered(MouseEvent e) {
            workWithPopup(e);
        }

        /**
         * Invoked when the mouse exits a component.
         */
        public void mouseExited(MouseEvent e) {
            workWithPopup(e);
        }

        /**
         * Invoked when a mouse button has been pressed on a component.
         */
        public void mousePressed(MouseEvent e) {
            workWithPopup(e);
        }

        /**
         * Invoked when a mouse button has been released on a component.
         */
        public void mouseReleased(MouseEvent e) {
        }

        public void workWithPopup(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                if (e.getSource() instanceof JTree) {
                    JTree temp = (JTree) e.getSource();
                    temp.setSelectionPath(temp.getPathForLocation(e.getX(), e.getY()));
                    TreePath[] selectedPaths = temp.getSelectionPaths();
                    boolean tableSelected = false;
                    if (selectedPaths != null) {
                        if (selectedPaths.length == 1) {
                            TreePath selectedPath = selectedPaths[0];
                            DefaultMutableTreeNode valSelected = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                            if (valSelected.getUserObject() instanceof TableInfo) {
                                tableSelected = true;
                            }
                        }
                    }
                    if (tableSelected) {
                        JPopupMenu popupMenu = new JPopupMenu();
                        JMenuItem sdmi = new JMenuItem(SELECT_DATA);
                        sdmi.addActionListener(this);
                        sdmi.setFont(UIConstants.MENU_FONT_VERDANA_PLAIN_12);
                        popupMenu.add(sdmi);
                        popupMenu.show(temp, e.getX(), e.getY());
                    }
                }

            }
        }


        public void actionPerformed(ActionEvent e) {
            if (SELECT_DATA.equals(e.getActionCommand())) {
                TreePath[] selectedPaths = getTableTree().getSelectionPaths();
                String selectedTable = "";
                String[] columns = null;
                if (selectedPaths != null) {
                    if (selectedPaths.length == 1) {
                        TreePath selectedPath = selectedPaths[0];
                        DefaultMutableTreeNode valSelected = (DefaultMutableTreeNode) selectedPath.getLastPathComponent();
                        if (valSelected.getUserObject() instanceof TableInfo) {
                            selectedTable = ((TableInfo) valSelected.getUserObject()).getName();
                            Enumeration en = valSelected.children();
                            Vector tempColumns = new Vector(1);
                            while (en.hasMoreElements()) {
                                DefaultMutableTreeNode colNode = (DefaultMutableTreeNode) en.nextElement();
                                ColumnInfo columnData = (ColumnInfo) colNode.getUserObject();
                                tempColumns.add(columnData.getName());
                            }
                            columns = new String[tempColumns.size()];
                            System.arraycopy(tempColumns.toArray(), 0, columns, 0, tempColumns.size());
                        }
                    }
                    if(!EmptyOrNullStringValidator.isEmpty(selectedTable))
                    EditorPane.addQueryTab(QueryBuilderUtil.buildQuery(selectedTable, columns));
                }
            }
        }

        public static final String SELECT_DATA = "Select Data";
    }

    public class TablePopupMenu extends JPopupMenu {

    }
}
