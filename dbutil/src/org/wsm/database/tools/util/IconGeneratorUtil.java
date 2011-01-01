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


import java.awt.Image;

import javax.swing.ImageIcon;

import org.wsm.database.tools.DbUtilConstants;

public class IconGeneratorUtil {
    
    public static ImageIcon getImageIcon(String fileName,int width, int height) {
        ImageIcon img = new ImageIcon(DbUtilConstants.ICON_IMAGES_LOCATION+ fileName);
        img.setImage(img.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
        return img;
    }
}
