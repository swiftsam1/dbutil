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

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class DisplayUtil {

    public static Rectangle getCenterOfTheWindow(JFrame frame) {
        Dimension d = frame.getSize();
        double x = d.getWidth()/2.0 - 150;
        double y = d.getHeight()/2.0 - 50;
        Rectangle r = new Rectangle((int)x,(int)y,0,0);
        return r;
    }
}
