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


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.wsm.database.tools.util.EmptyOrNullStringValidator;

public class GenericTableDataModel extends AbstractTableModel {


    public GenericTableDataModel() {
        super();
        columns = new String[]{};
        data = new Object[][]{};
        originalData = new Object[][]{};
        initializeEditableRowColumns();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    public int getColumnCount() {
        return this.getColumns().length;
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount() {
        return getData().length;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     * @return the Object.class
     */
    public Class getColumnClass(int columnIndex) {
        if (getValueAt(0, columnIndex) != null)
            return getValueAt(0, columnIndex).getClass();
        else
            return Object.class;
    }


    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void revertToOriginalValue() {
        for (int i = 0; i < getRowCount(); i++) {
            if (isRowEditable(i)) {
                revertToOriginalValue(i);
            }
        }
    }

    public void revertToOriginalValue(int rowIndex) {
        for (int i = 0; i < getColumnCount(); i++) {
            revertToOriginalValue(rowIndex, i);
        }
    }

    public void revertToOriginalValue(int rowIndex, int columnIndex) {
        if (valueChanged(rowIndex, columnIndex)) {
            this.setValueAt(getOriginalValueAt(rowIndex, columnIndex), rowIndex, columnIndex);
        }
    }

    public boolean hasAtleastOneCellValueChangedForRow(int rowIndex) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (valueChanged(rowIndex, i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a default name for the column using spreadsheet conventions:
     * A, B, C, ... Z, AA, AB, etc.  If <code>column</code> cannot be found,
     * returns an empty string.
     *
     * @param column the column being queried
     * @return a string containing the default name of <code>column</code>
     */
    public String getColumnName(int column) {
        return getColumns()[column];
    }

    /**
     * Returns a column given its name.
     * Implementation is naive so this should be overridden if
     * this method is to be called often. This method is not
     * in the <code>TableModel</code> interface and is not used by the
     * <code>JTable</code>.
     *
     * @param columnName string containing name of column to be located
     * @return the column with <code>columnName</code>, or -1 if not found
     */
    public int findColumn(String columnName) {
        return super.findColumn(columnName);
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
        fireTableStructureChanged();
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public Object[][] getOriginalData() {
        return originalData;
    }

    public Object getOriginalValueAt(int rowIndex, int columnIndex) {
        return originalData[rowIndex][columnIndex];
    }

    public void setOriginalData(final Object[][] originalData) {
        this.originalData = originalData;
    }

    public void addOriginalRow(Object[] originalRowData) {
        Object[][] dest = new Object[getOriginalData().length + 1][getColumnCount()];
        System.arraycopy(getOriginalData(), 0, dest, 0, getOriginalData().length);
        dest[dest.length - 1] = originalRowData;
        this.setOriginalData(dest);

    }

    public void addRow(Object[] rowData) {
        Object[][] dest = new Object[getData().length + 1][getColumnCount()];
        System.arraycopy(getData(), 0, dest, 0, getData().length);
        dest[dest.length - 1] = rowData;
        this.setData(dest);
        fireTableRowsInserted(getData().length - 1, getData().length);
        //fireTableStructureChanged();
    }    

    public void deleteRow(Object[] rowData) {
        if (rowData == null) {
            return;
        }
        for (int i = 0; i < getData().length; i++) {
            if (getData()[i] == rowData) {
                Object[][] temp = new Object[getData().length - 1][getColumnCount()];
                System.arraycopy(getData(), 0, temp, 0, i + 1);
                System.arraycopy(getData(), i + 1, temp, i, (getData().length - (i + 1)));
                setData(temp);
                fireTableRowsDeleted(i, i);
                fireTableStructureChanged();
            }
        }
    }

    /**
     * Returns false.  This is the default implementation for all cells.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return false
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (isAllRowsEditable()) return true;
        Integer row = new Integer(rowIndex), column = new Integer(columnIndex);
        if (valueChanged(rowIndex, columnIndex)) {
            return true;
        }
        if (editableRowColumns == null) {
            return false;
        } else {
            if (editableRowColumns.containsKey(row)) {
                Vector cols = (Vector) editableRowColumns.get(row);
                return cols.contains(column);
            } else {
                return false;
            }
        }

/*
        if (editableRows != null) {
            Iterator iter = editableRows.iterator();
            while (iter.hasNext()) {
                Integer rowNum = ((Integer) iter.next());
                if (rowNum.intValue() == rowIndex) {
                    return true;
                }
            }
        }
        return false;
*/
    }

    public void setRowEditable(int row) {

        if (isRowEditable(row)) {
            editableRowColumns.remove(new Integer(row));
            //editableRows.remove(new Integer(row));
        } else if (hasRowEntry(row)) {
            setCellEditable(row, true, false);
        } else {
            if (editableRowColumns == null) {
                //if (editableRows == null) {
                initializeEditableRowColumns();
            }
            setCellEditable(row, true, false);
        }
        fireTableRowsUpdated(row, row);
    }

    public void setCellEditable(int rowIndex, int columnIndex, boolean deleteIfExists) {
        if (editableRowColumns == null) {
            initializeEditableRowColumns();
        }
        Integer column = new Integer(columnIndex);
        Integer row = new Integer(rowIndex);
        if (hasRowEntry(rowIndex)) {
            Vector cols = (Vector) editableRowColumns.get(row);
            if (!cols.contains(column)) {
                cols.add(column);
            } else {
                if (deleteIfExists) cols.remove(column);
            }
        } else {
            Vector cols = new Vector();
            cols.add(column);
            editableRowColumns.put(row, cols);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void setCellEditable(int rowIndex, int columnIndex) {
        setCellEditable(rowIndex, columnIndex, true);
    }

    public boolean hasRowEntry(int rowIndex) {
        if (editableRowColumns == null) {
            return false;
        } else {
            return editableRowColumns.containsKey(new Integer(rowIndex));
        }
    }

    private void initializeEditableRowColumns() {
        if (editableRowColumns == null) {
            editableRowColumns = (Map) Collections.synchronizedMap(new HashMap());
        }
    }

    public void setCellEditable(int rowIndex, boolean allCols) {
        setCellEditable(rowIndex, allCols, true);
/*
        for(int i=0; i< getColumnCount(); i++) {
            setCellEditable(rowIndex, i);
        }
*/
    }

    public void setCellEditable(int rowIndex, boolean allCols, boolean deleteIfExists) {
        for (int i = 0; i < getColumnCount(); i++) {
            setCellEditable(rowIndex, i, deleteIfExists);
        }
    }

    public boolean isRowEditable(int rowIndex) {
        if (isAllRowsEditable()) return true;
        if (editableRowColumns != null) {
            for (int i = 0; i < getColumnCount(); i++) {
                if (!isCellEditable(rowIndex, i)) return false;
            }
        } else {
            return false;
        }
        return true;
/*
        if (editableRows == null) return false;
        else {
            boolean editable = true;
            for (int i = 1; i <= columns.length; i++) {
                editable = editable && isCellEditable(row, i);
            }
            return editable;
        }
*/
    }

/*
    public Vector getEditableRows() {
        return editableRows;
    }

    public void setEditableRows(Vector editableRows) {
        this.editableRows = editableRows;
    }

*/

    public Map getEditableRowColumns() {
        return editableRowColumns;
    }

    public void setEditableRowColumns(Map editableRowColumns) {
        this.editableRowColumns = editableRowColumns;
    }

    public boolean isAllRowsEditable() {
        return allRowsEditable;
    }

    public void setAllRowsEditable() {
        allRowsEditable = !allRowsEditable;
        fireTableRowsUpdated(0, getRowCount());
    }

    public boolean valueChanged(int rowIndex, int columnIndex) {
        if (rowIndex > getRowCount() || rowIndex < 0 || columnIndex < 0 || columnIndex > getColumnCount()) {
            return false;
        }
        final Object currentValue = getValueAt(rowIndex, columnIndex);
        final Object originalValue = getOriginalValueAt(rowIndex, columnIndex);

        if (currentValue != null && originalValue != null) {
            return !(currentValue.equals(originalValue));
        }
        if (currentValue != null && originalValue == null) {
            if (currentValue instanceof String) {
                return !EmptyOrNullStringValidator.isEmpty((String) currentValue);
            }
        }
        if (currentValue == null && originalValue != null) {
            if (originalValue instanceof String) {
                return !EmptyOrNullStringValidator.isEmpty((String) originalValue);
            }
        }
        return false;
    }

    //private Vector editableRows;

    /**
     * The key would the row index and the value will be a vector of column index.
     */
    private Map editableRowColumns;
    private boolean allRowsEditable;

    private String[] columns;

    /**
     * Current data that is being represented by the Table.
     */
    private Object[][] data;

    /**
     * The original data that is retrieved by the query.
     */
    private Object[][] originalData;
}
