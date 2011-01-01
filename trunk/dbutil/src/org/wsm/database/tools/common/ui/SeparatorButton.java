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
package org.wsm.database.tools.common.ui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class SeparatorButton extends JButton {
    /**
     * Creates a button with no set text or icon.
     */
    public SeparatorButton() {
        super();
    }

    /**
     * Creates a button with text.
     *
     * @param text the text of the button
     */
    public SeparatorButton(String text) {
        super(text);
    }

    /**
     * Creates a button where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     * @since 1.3
     */
    public SeparatorButton(Action a) {
        super(a);
    }

    /**
     * Creates a button with an icon.
     *
     * @param icon the Icon image to display on the button
     */
    public SeparatorButton(Icon icon) {
        super(icon);
    }

    /**
     * Creates a button with initial text and an icon.
     *
     * @param text the text of the button
     * @param icon the Icon image to display on the button
     */
    public SeparatorButton(String text, Icon icon) {
        super(text, icon);
    }
    

}
