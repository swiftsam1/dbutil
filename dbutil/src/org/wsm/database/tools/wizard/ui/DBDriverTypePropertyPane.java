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

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.wsm.database.tools.DbUtilConstants;
import org.wsm.database.tools.util.SwingUtils;

public class DBDriverTypePropertyPane extends BaseWizardPane {

    public DBDriverTypePropertyPane() {

        GridBagLayout gbl = new GridBagLayout();
        JLabel dbDriverTypeLabel = SwingUtils.constructLabel("Driver Type", Color.WHITE, true);
        gbl.setConstraints(dbDriverTypeLabel, SwingUtils.getGridBagConstraints(0,0));
        this.add(dbDriverTypeLabel);

        dbDriveComboBox = new JComboBox(driverTypes);
        dbDriveComboBox.setEditable(false);
        gbl.setConstraints(dbDriveComboBox, SwingUtils.getGridBagConstraints(1,0));
        this.add(dbDriveComboBox);
        this.setLayout(gbl);
    }

    public String[] getDriverTypes() {
        return driverTypes;
    }

    public void setDriverTypes(String[] driverTypes) {
        this.driverTypes = driverTypes;
    }


    public JComboBox getDbDriveComboBox() {
        return dbDriveComboBox;
    }

    public void setDbDriveComboBox(JComboBox dbDriveComboBox) {
        this.dbDriveComboBox = dbDriveComboBox;
    }


    String[] driverTypes = {DbUtilConstants.SELECT_ONE, DbUtilConstants.ORACLE_OCI_DRIVER, DbUtilConstants.ORACLE_THIN_DRIVER};

    JComboBox dbDriveComboBox;
}
