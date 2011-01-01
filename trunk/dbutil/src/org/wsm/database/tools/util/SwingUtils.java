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
package org.wsm.database.tools.util;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JLabel;

import org.wsm.database.tools.UIConstants;

public class SwingUtils {

    public static GridBagConstraints getGridBagConstraints(int x, int y) {
        Insets i = new Insets(0, 3, 3, 6);

        return getGridBagConstraints(x, y, i);
    }


    public static GridBagConstraints getGridBagConstraints(int x, int y, Insets i) {
        return getGridBagConstraints(x, y, 1, 1, i);

    }

    public static GridBagConstraints getGridBagConstraints(int x, int y, int width, int height) {
        Insets i = new Insets(0, 3, 3, 6);
        return getGridBagConstraints(x, y, width, height, i);

    }

    public static GridBagConstraints getGridBagConstraints(int x, int y, int width, int height, Insets i) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.insets = i;
        gbc.fill = GridBagConstraints.BOTH;
        return gbc;

    }

    public static JLabel constructLabel(String title) {
        return constructLabel(title,Color.BLACK);
    }
    public static JLabel constructLabel(String title, Color color) {
        return constructLabel(title, color, false);
    }

    public static JLabel constructLabel(String title, Color color, boolean isRequired) {
        JLabel label = new JLabel(title);
        label.setFont( isRequired?UIConstants.LABEL_FONT_REQUIRED:UIConstants.LABEL_FONT);
        label.setForeground(color);
        return label;
    }
    
}
