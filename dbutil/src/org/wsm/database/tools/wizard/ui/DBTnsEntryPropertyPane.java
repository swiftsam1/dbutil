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
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.wsm.database.tools.util.SwingUtils;

public class DBTnsEntryPropertyPane extends BaseWizardPane{

    public DBTnsEntryPropertyPane() {
        GridBagLayout gbl = new GridBagLayout();

        JLabel dbUNameLabel = SwingUtils.constructLabel("User Name", Color.WHITE, true);
        gbl.setConstraints(dbUNameLabel, SwingUtils.getGridBagConstraints(0,0));
        this.add(dbUNameLabel);

        dbUNameText = new JTextField(15);
        gbl.setConstraints(dbUNameText, SwingUtils.getGridBagConstraints(1,0));
        this.add(dbUNameText);

        JLabel dbPwdLabel = SwingUtils.constructLabel("Password", Color.WHITE, true);
        gbl.setConstraints(dbPwdLabel, SwingUtils.getGridBagConstraints(0,1));
        this.add(dbPwdLabel);

        dbPwdText = new JPasswordField(15);
        gbl.setConstraints(dbPwdText, SwingUtils.getGridBagConstraints(1,1));
        this.add(dbPwdText);


        JLabel tnsEntryLabel = SwingUtils.constructLabel("TNS Entry", Color.WHITE, true);
        gbl.setConstraints(tnsEntryLabel, SwingUtils.getGridBagConstraints(0,2));
        this.add(tnsEntryLabel);

        tnsEntryNameText = new JTextField(15);
        gbl.setConstraints(tnsEntryNameText, SwingUtils.getGridBagConstraints(1,2));
        this.add(tnsEntryNameText);
        this.setLayout(gbl);
    }


    public JTextField getTnsEntryNameText() {
        return tnsEntryNameText;
    }

    public void setTnsEntryNameText(JTextField tnsEntryNameText) {
        this.tnsEntryNameText = tnsEntryNameText;
    }

    public JTextField getDbUNameText() {
        return dbUNameText;
    }

    public void setDbUNameText(JTextField dbUNameText) {
        this.dbUNameText = dbUNameText;
    }

    public JTextField getDbPwdText() {
        return dbPwdText;
    }

    public void setDbPwdText(JTextField dbPwdText) {
        this.dbPwdText = dbPwdText;
    }


    JTextField tnsEntryNameText, dbUNameText,dbPwdText;
}
