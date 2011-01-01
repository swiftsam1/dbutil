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
package org.wsm.database.tools.common.ui.table.renderer;


import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import org.wsm.database.tools.editor.ui.TableSorter;

public class BaseColumnRenderer extends JLabel implements TableCellRenderer, Serializable {

    public BaseColumnRenderer() {
        super();
        setOpaque(true);
        setBorder(noFocusBorder);
    }

    public void setForeground(Color c) {
        super.setForeground(c);
        unselectedForeground = c;
    }

    public void setBackground(Color c) {
        super.setBackground(c);
        unselectedBackground = c;
    }

    public void updateUI() {
        super.updateUI();
    setForeground(null);
    setBackground(null);
    }




    /**
     * Returns the component used for drawing the cell.  This method is
     * used to configure the renderer appropriately before drawing.
     *
     * @param table      the <code>JTable</code> that is asking the
     *                   renderer to draw; can be <code>null</code>
     * @param value      the value of the cell to be rendered.  It is
     *                   up to the specific renderer to interpret
     *                   and draw the value.  For example, if
     *                   <code>value</code>
     *                   is the string "true", it could be rendered as a
     *                   string or it could be rendered as a check
     *                   box that is checked.  <code>null</code> is a
     *                   valid value
     * @param isSelected true if the cell is to be rendered with the
     *                   selection highlighted; otherwise false
     * @param hasFocus   if true, render cell appropriately.  For
     *                   example, put a special border on the cell, if
     *                   the cell can be edited, render in the color used
     *                   to indicate editing
     * @param row        the row index of the cell being drawn.  When
     *                   drawing the header, the value of
     *                   <code>row</code> is -1
     * @param column     the column index of the cell being drawn
     */
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        if (isSelected) {
           super.setForeground(table.getSelectionForeground());
           super.setBackground(table.getSelectionBackground());
        }
        else {
            super.setForeground((unselectedForeground != null) ? unselectedForeground
                                                               : table.getForeground());
            super.setBackground((unselectedBackground != null) ? unselectedBackground
                                                               : table.getBackground());
        }

        setFont(table.getFont());

        if (hasFocus) {
            setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
            if (table.isCellEditable(row, column)) {
                super.setForeground( UIManager.getColor("Table.focusCellForeground") );
                super.setBackground( UIManager.getColor("Table.focusCellBackground") );
            }
        } else {
            setBorder(noFocusBorder);
        }
            setValue(value);
        if(table.isCellEditable(row,column)) {
            super.setBackground(Color.LIGHT_GRAY);
        }
        if(((TableSorter)table.getModel()).getTableModel().valueChanged(row,column) ) {
            super.setBackground(Color.ORANGE);
        }

        return this;
    }

    public boolean isOpaque() {
    Color back = getBackground();
    Component p = getParent();
    if (p != null) {
        p = p.getParent();
    }
    // p should now be the JTable.
    boolean colorMatch = (back != null) && (p != null) &&
        back.equals(p.getBackground()) &&
            p.isOpaque();
    return !colorMatch && super.isOpaque();
    }


    protected void setValue(Object value) {
    setText((value == null) ? "" : value.toString());
    }


    private Color unselectedForeground;
    private Color unselectedBackground;

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    // We need a place to store the color the JLabel should be returned
    // to after its foreground and background colors have been set
    // to the selection background color.
    // These ivars will be made protected when their names are finalized.

    public static class UIResource extends BaseColumnRenderer
        implements javax.swing.plaf.UIResource
    {
    }


}
