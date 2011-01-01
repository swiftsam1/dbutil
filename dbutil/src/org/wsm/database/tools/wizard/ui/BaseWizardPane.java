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
package org.wsm.database.tools.wizard.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class BaseWizardPane extends JPanel {


    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        //Color color1 = new Color(104, 140, 228);
        Color color1 = new Color(61, 127, 177);
        //Color color2 = new Color(71, 102 , 209);
        Color color2 = new Color(23, 138 , 198);

        Rectangle bounds = getBounds();
        // Set Paint for filling Shape
        Paint gradientPaint = new GradientPaint(0, 0, color1, bounds.width, bounds.height, color2);
        g.setPaint(gradientPaint);
        g.fillRect(0, 0, bounds.width, bounds.height);
    }



    public boolean isOpaque() {
        return true;
    }

}
