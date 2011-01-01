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

public class DBConnectionPropertiesPane extends BaseWizardPane {

    public DBConnectionPropertiesPane() {
        GridBagLayout gbl = new GridBagLayout();
        JLabel dbUNameLabel = SwingUtils.constructLabel("User Name", Color.WHITE, true);
        gbl.setConstraints(
                dbUNameLabel,
                SwingUtils.getGridBagConstraints(0,0)
        );
        this.add(dbUNameLabel);

        dbUNameText = new JTextField(15);
        gbl.setConstraints(dbUNameText, SwingUtils.getGridBagConstraints(1,0));
        this.add(dbUNameText);

        JLabel dbPwdLabel = SwingUtils.constructLabel("Password", Color.WHITE, true);
        gbl.setConstraints(dbPwdLabel, SwingUtils.getGridBagConstraints(0,1));
        this.add(dbPwdLabel);

        dbPwdText = new JPasswordField(15);
        gbl.setConstraints(dbPwdText,  SwingUtils.getGridBagConstraints(1,1));
        this.add(dbPwdText);

        JLabel dbHostLabel = SwingUtils.constructLabel("Host", Color.WHITE, true);
        gbl.setConstraints(dbHostLabel, SwingUtils.getGridBagConstraints(0,2));
        this.add(dbHostLabel);

        dbHostText = new JTextField(15);
        gbl.setConstraints(dbHostText,  SwingUtils.getGridBagConstraints(1,2));
        this.add(dbHostText);

        JLabel dbPortLabel = SwingUtils.constructLabel("Port", Color.WHITE, true);
        gbl.setConstraints(dbPortLabel, SwingUtils.getGridBagConstraints(0,3));
        this.add(dbPortLabel);

        dbPortText = new JTextField(15);
        gbl.setConstraints(dbPortText,  SwingUtils.getGridBagConstraints(1,3));
        this.add(dbPortText);

        JLabel dbDatabaseNameLabel = SwingUtils.constructLabel("Database Name", Color.WHITE, true);
        gbl.setConstraints(dbDatabaseNameLabel, SwingUtils.getGridBagConstraints(0,4));
        this.add(dbDatabaseNameLabel);

        dbDatabaseNameText = new JTextField(15);
        gbl.setConstraints(dbDatabaseNameText,  SwingUtils.getGridBagConstraints(1,4));
        this.add(dbDatabaseNameText);
        this.setLayout(gbl);
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

    public JTextField getDbHostText() {
        return dbHostText;
    }

    public void setDbHostText(JTextField dbHostText) {
        this.dbHostText = dbHostText;
    }

    public JTextField getDbPortText() {
        return dbPortText;
    }

    public void setDbPortText(JTextField dbPortText) {
        this.dbPortText = dbPortText;
    }

    public JTextField getDbDatabaseNameText() {
        return dbDatabaseNameText;
    }

    public void setDbDatabaseNameText(JTextField dbDatabaseNameText) {
        this.dbDatabaseNameText = dbDatabaseNameText;
    }

    JTextField dbUNameText,dbPwdText, dbHostText, dbPortText, dbDatabaseNameText;
}
