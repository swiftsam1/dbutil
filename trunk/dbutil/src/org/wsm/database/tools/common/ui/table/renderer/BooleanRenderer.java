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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.wsm.database.tools.editor.ui.GenericTableDataModel;

public class BooleanRenderer extends JCheckBox implements TableCellRenderer {

    public BooleanRenderer() {
        super();
        setHorizontalAlignment(JLabel.CENTER);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setSelected((value != null && ((Boolean) value).booleanValue()));

        if(table.isCellEditable(row,column)) {
            super.setBackground(Color.LIGHT_GRAY);
        }

        if( ((GenericTableDataModel)table.getModel()).valueChanged(row,column) ) {
            super.setBackground(Color.ORANGE);
        }

        return this;
    }


}
