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
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.wsm.database.tools.DbUtilConstants;
import org.wsm.database.tools.UIConstants;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;
import org.wsm.database.tools.util.SwingUtils;
import org.wsm.database.tools.wizard.logic.ProjectProperties;

public class ProjectPropertiesPane extends BaseWizardPane {

    public ProjectPropertiesPane(ActionListener l) {

        GridBagLayout gbl = new GridBagLayout();

        JLabel projNameLabel = SwingUtils.constructLabel("Project Name", Color.WHITE, true);
        gbl.setConstraints(projNameLabel, SwingUtils.getGridBagConstraints(0,0));
        this.add(projNameLabel);

        projNameText = new JTextField(15);
        gbl.setConstraints(projNameText, SwingUtils.getGridBagConstraints(1,0));
        this.add(projNameText);

        JLabel projPathLabel = SwingUtils.constructLabel("Path", Color.WHITE, true);
        gbl.setConstraints(projPathLabel, SwingUtils.getGridBagConstraints(0,1));
        this.add(projPathLabel);

        projPathText = new JTextField(15);
        gbl.setConstraints(projPathText, SwingUtils.getGridBagConstraints(1,1));
        this.add(projPathText);

        projPropsPathFileChooserButton = new JButton("...");
        projPropsPathFileChooserButton.setSize(new Dimension(3, 1));
        projPropsPathFileChooserButton.setFont(UIConstants.BUTTON_FONT);
        gbl.setConstraints(projPropsPathFileChooserButton, SwingUtils.getGridBagConstraints(2,1));
        projPropsPathFileChooserButton.addActionListener(l);
        this.add(projPropsPathFileChooserButton);

        JLabel projDatabaseLabel = SwingUtils.constructLabel("Database Type", Color.WHITE, true);
        gbl.setConstraints(projDatabaseLabel, SwingUtils.getGridBagConstraints(0,2));
        this.add(projDatabaseLabel);

        projDatabaseComboBox = new JComboBox(databaseTypes);
        gbl.setConstraints(projDatabaseComboBox, SwingUtils.getGridBagConstraints(1,2));
        this.add(projDatabaseComboBox);

        this.setLayout(gbl);
    }

    public ProjectProperties getProjectProperties() {
        return projectProperties;
    }

    public void setProjectProperties(ProjectProperties projectProperties) {
        this.projectProperties = projectProperties;
    }

    private ProjectProperties projectProperties = new ProjectProperties();
    public String[] databaseTypes = {DbUtilConstants.SELECT_ONE,
            DbUtilConstants.DATABASE_TYPE_ORACLE,
            DbUtilConstants.DATABASE_TYPE_MYSQL};

    public String getProjName() {
        if (!EmptyOrNullStringValidator.isEmpty(projNameText.getText())) {
            return projNameText.getText().trim();
        } else
            return null;
    }

    public String getProjPath() {
        if (!EmptyOrNullStringValidator.isEmpty(projPathText.getText())) {
            return projPathText.getText().trim();
        } else
            return null;
    }

    public JTextField getProjNameText() {
        return projNameText;
    }

    public void setProjNameText(JTextField projNameText) {
        this.projNameText = projNameText;
    }

    public JTextField getProjPathText() {
        return projPathText;
    }

    public void setProjPathText(JTextField projPathText) {
        this.projPathText = projPathText;
    }

    private JTextField projNameText, projPathText;
    public JComboBox projDatabaseComboBox;
    public JButton projPropsPathFileChooserButton;

}
