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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import org.wsm.database.tools.common.ui.table.renderer.BaseColumnRenderer;
import org.wsm.database.tools.common.ui.table.renderer.BooleanRenderer;
import org.wsm.database.tools.common.ui.table.renderer.DateRenderer;
import org.wsm.database.tools.common.ui.table.renderer.DoubleRenderer;
import org.wsm.database.tools.common.ui.table.renderer.NumberRenderer;


public class ResultsPane extends JPanel implements MouseListener, CellEditorListener {

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public ResultsPane() {
        gtdm = new GenericTableDataModel();
        TableSorter ts = new TableSorter(gtdm);
        this.setLayout(new BorderLayout());
        results = new JTable();
        results.getTableHeader().setReorderingAllowed(false);
        results.setModel(ts);
        ts.setTableHeader(results.getTableHeader());

        results.setDefaultRenderer(Object.class, new BaseColumnRenderer());
        results.setDefaultRenderer(String.class, new BaseColumnRenderer());
        results.setDefaultRenderer(Integer.class, new NumberRenderer());
        results.setDefaultRenderer(Float.class, new DoubleRenderer());
        results.setDefaultRenderer(Double.class, new DoubleRenderer());
        results.setDefaultRenderer(Boolean.class, new BooleanRenderer());
        results.setDefaultRenderer(Date.class, new DateRenderer());
        results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        results.setCellSelectionEnabled(false);
        results.setColumnSelectionAllowed(false);
        results.setRowSelectionAllowed(true);
        results.setAutoscrolls(true);
        results.setShowGrid(true);
        results.setRowHeight(results.getRowHeight() + 5);
        results.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        results.addMouseListener(this);
        //graphPane = new GraphPane(profiler);
        JScrollPane scrollResults = new JScrollPane(results);
        scrollResults.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollResults.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollResults, BorderLayout.CENTER);
    }

    public void setResults(JTable results) {
        this.results = results;
    }


    public GenericTableDataModel getGtdm() {
        return gtdm;
    }

    public void setGtdm(GenericTableDataModel gtdm) {
        this.gtdm = gtdm;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e) {
        handleMouseEvent(e);
    }

    /**
     * Invoked when the mouse enters a component.
     */
    public void mouseEntered(MouseEvent e) {
        handleMouseEvent(e);
    }

    /**
     * Invoked when the mouse exits a component.
     */
    public void mouseExited(MouseEvent e) {
        handleMouseEvent(e);
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     */
    public void mousePressed(MouseEvent e) {
        handleMouseEvent(e);
    }

    /**
     * Invoked when a mouse button has been released on a component.
     */
    public void mouseReleased(MouseEvent e) {
        handleMouseEvent(e);
    }

    public void handleMouseEvent(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            final int rowIndex = results.rowAtPoint(e.getPoint());
            final int columnIndex = results.columnAtPoint(e.getPoint());
            results.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
            JPopupMenu popup = new JPopupMenu();

            /**
             * set the all rows editable menu
             */
            JCheckBoxMenuItem editAllRows = new JCheckBoxMenuItem(new EditAllRowsAction(EDIT_ALL_ROWS));
            editAllRows.setSelected(gtdm.isAllRowsEditable());

            JCheckBoxMenuItem editRow = new JCheckBoxMenuItem(new EditRowAction(EDIT_ROW));
            editRow.setSelected(gtdm.isRowEditable(results.getSelectedRow()));
            editRow.setEnabled(!gtdm.isAllRowsEditable());

            JCheckBoxMenuItem editColumn = new JCheckBoxMenuItem(new EditColumnAction(EDIT_COLUMN, e.getPoint()));
            editColumn.setSelected(gtdm.isCellEditable(results.getSelectedRow(), columnIndex));
            editColumn.setEnabled(!gtdm.isAllRowsEditable());
            //JMenuItem editRow = new JMenuItem(new EditRowAction("Edit Row"));

            JMenuItem revertAllChanges = new JMenuItem(new RevertAllChangesAction(REVERT_CHANGE_FOR_ALL_ROWS));
            revertAllChanges.setEnabled(gtdm.isAllRowsEditable());

            JMenuItem revertRowChanges = new JMenuItem(new RevertChangesForRowAction(REVERT_CHANGE_FOR_ROW, e.getPoint()));
            revertRowChanges.setEnabled(gtdm.isRowEditable(rowIndex)
                    && gtdm.hasAtleastOneCellValueChangedForRow(rowIndex));

            JMenuItem revertCellChanges = new JMenuItem(new RevertChangesForCellAction(REVERT_CHANGE_FOR_CELL, e.getPoint()));
            revertCellChanges.setEnabled(
                    gtdm.isCellEditable(rowIndex, columnIndex) &&
                            gtdm.valueChanged(rowIndex, columnIndex)
            );

            popup.add(editAllRows);
            popup.add(editRow);
            popup.add(editColumn);
            popup.addSeparator();
            popup.add(revertAllChanges);
            popup.add(revertRowChanges);
            popup.add(revertCellChanges);
            popup.show(results, e.getX(), e.getY());
        }
    }

    /**
     * This tells the listeners the editor has canceled editing
     */
    public void editingCanceled(ChangeEvent e) {
        System.out.println("Inside editing cancelled");
        gtdm.fireTableDataChanged();
    }

    /**
     * This tells the listeners the editor has ended editing
     */
    public void editingStopped(ChangeEvent e) {
        System.out.println("Inside editing stopped");
        gtdm.fireTableDataChanged();
    }

    private GenericTableDataModel gtdm;
    private JTable results;

    public class EditRowAction extends AbstractAction {
        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public EditRowAction() {
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         */
        public EditRowAction(String name) {
            super(name);
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         */
        public EditRowAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (EDIT_ROW.equals(e.getActionCommand())) {
                gtdm.setRowEditable(results.getSelectedRow());
            }
        }

    }

    public class EditColumnAction extends AbstractAction {
        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public EditColumnAction(Point p) {
            this.mousePoint = p;
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         */
        public EditColumnAction(String name, Point p) {
            super(name);
            this.mousePoint = p;
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         */
        public EditColumnAction(String name, Icon icon, Point p) {
            super(name, icon);
            this.mousePoint = p;
        }


        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (EDIT_COLUMN.equals(e.getActionCommand())) {
                gtdm.setCellEditable(results.getSelectedRow(), results.columnAtPoint(mousePoint));
            }
        }

        private Point mousePoint;
    }

    public class EditAllRowsAction extends AbstractAction {

        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public EditAllRowsAction() {
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         */
        public EditAllRowsAction(String name) {
            super(name);
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         */
        public EditAllRowsAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (EDIT_ALL_ROWS.equals(e.getActionCommand())) {
                gtdm.setAllRowsEditable();
            }
        }
    }

    public class RevertChangesForCellAction extends AbstractAction {

        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public RevertChangesForCellAction(Point p) {
            this.mousePoint = p;
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         */
        public RevertChangesForCellAction(String name, Point p) {
            super(name);
            this.mousePoint = p;
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         */
        public RevertChangesForCellAction(String name, Icon icon, Point p) {
            super(name, icon);
            this.mousePoint = p;
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (REVERT_CHANGE_FOR_CELL.equals(e.getActionCommand())) {
                int rowIndex = results.rowAtPoint(this.mousePoint);
                int colIndex = results.columnAtPoint(this.mousePoint);
                gtdm.revertToOriginalValue(rowIndex, colIndex);
            }
        }

        private Point mousePoint;
    }

    public class RevertChangesForRowAction extends AbstractAction {

        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public RevertChangesForRowAction(Point p) {
            this.mousePoint = p;
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         */
        public RevertChangesForRowAction(String name, Point p) {
            super(name);
            this.mousePoint = p;
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         */
        public RevertChangesForRowAction(String name, Icon icon, Point p) {
            super(name, icon);
            this.mousePoint = p;
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (REVERT_CHANGE_FOR_ROW.equals(e.getActionCommand())) {
                int rowIndex = results.rowAtPoint(this.mousePoint);
                gtdm.revertToOriginalValue(rowIndex);
            }
        }

        private Point mousePoint;
    }

    public class RevertAllChangesAction extends AbstractAction {

        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public RevertAllChangesAction() {
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         */
        public RevertAllChangesAction(String name) {
            super(name);
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         */
        public RevertAllChangesAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (REVERT_CHANGE_FOR_ALL_ROWS.equals(e.getActionCommand())) {
                gtdm.revertToOriginalValue();
            }
        }
    }


    protected static final String EDIT_ROW = "Edit Row";
    protected static final String EDIT_COLUMN = "Edit Column";
    protected static final String EDIT_ALL_ROWS = "Edit All Rows";

    protected static final String REVERT_CHANGE_FOR_CELL = "Revert Column Changes";
    protected static final String REVERT_CHANGE_FOR_ROW = "Revert Row Changes";
    protected static final String REVERT_CHANGE_FOR_ALL_ROWS = "Revert All Changes";
}
