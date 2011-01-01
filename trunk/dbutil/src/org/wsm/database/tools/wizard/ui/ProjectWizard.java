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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.wsm.database.tools.DBUtilContainer;
import org.wsm.database.tools.DbUtilConstants;
import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.UIConstants;
import org.wsm.database.tools.browser.ui.BodyBrowserPane;
import org.wsm.database.tools.config.parsers.RecentProjectsParser;
import org.wsm.database.tools.util.ConnectionManager;
import org.wsm.database.tools.util.DBConnectionFailedException;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;
import org.wsm.database.tools.util.NumericValidator;
import org.wsm.database.tools.wizard.logic.DBConnectionProperties;

public class ProjectWizard extends JDialog implements ActionListener {

    public ProjectWizard() {
        super(StartMe.getContainer(), true);
        this.setTitle("New Project Wizard");
        topDetailsPane = new JPanel();
        //topDetailsPane.setBackground(new Color(0, 39, 147));
        topDetailsPane.setBackground(new Color(13, 32, 12));

        JLabel topLabel = new JLabel("Database Profiler: New Project Setup");
        topLabel.setFont(UIConstants.LABEL_FONT_HEADING1);
        topLabel.setForeground(Color.WHITE);
        getTopDetailsPane().add(topLabel);
        getTopDetailsPane().setSize(300, 200);
        getContentPane().add(getTopDetailsPane(), BorderLayout.NORTH);

        projPropsPane = new ProjectPropertiesPane(this);
        dbdtpp = new DBDriverTypePropertyPane();
        dbtnsepp = new DBTnsEntryPropertyPane();
        dbcpp = new DBConnectionPropertiesPane();
        wizPane = new JPanel();
        wizPane.setLayout(new CardLayout());
        wizPane.add(projPropsPane, CL_KEY_PROJECT_PROPERTIES);
        wizPane.add(dbdtpp, CL_KEY_DRIVER_TYPE);
        wizPane.add(dbtnsepp, CL_KEY_TNS_ENTRY);
        wizPane.add(dbcpp, CL_KEY_DB_PROPERTIES);
        getContentPane().add(wizPane, BorderLayout.CENTER);

        backButton = new JButton(new BackButton());

        cancelButton = new JButton(new CancelButton());

        nextButton = new JButton(new NextButton());

        buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(13, 32, 12));
        buttonsPanel.add(backButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(nextButton);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        this.setBounds(100, 100, 500, 350);
    }

    public class NextButton extends AbstractAction {
        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public NextButton() {
            this("Next");
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         */
        public NextButton(String name) {
            super(name);
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         */
        public NextButton(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == nextButton) {
                if (projPropsPane.isVisible()) {
                    if (projPropsPane.getProjName() == null) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please provide Project Name",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        projPropsPane.getProjNameText().setFocusable(true);
                    } else if (projPropsPane.getProjPath() == null) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please provide Project Path",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        projPropsPane.getProjPathText().setFocusable(true);

                    } else if (EmptyOrNullStringValidator.isEmpty(
                            (String) projPropsPane.projDatabaseComboBox.getSelectedItem())
                            || "Select One...".equals((String) projPropsPane.projDatabaseComboBox.getSelectedItem())) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please Select Database Type",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);

                    } else {
                        props.setDatabaseType((String) projPropsPane.projDatabaseComboBox.getSelectedItem());
                        CardLayout cl = (CardLayout) wizPane.getLayout();
                        if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(props.getDatabaseType())) {
                            dbdtpp.dbDriveComboBox.setFocusable(true);
                            cl.show(wizPane, CL_KEY_DRIVER_TYPE);
                        } else if (DbUtilConstants.DATABASE_TYPE_MYSQL.equals(props.getDatabaseType())) {
                            cl.show(wizPane, CL_KEY_DB_PROPERTIES);
                        }

                    }
                } else if (dbdtpp.isVisible()) {
                    int selectedIndex = dbdtpp.getDbDriveComboBox().getSelectedIndex();
                    if (selectedIndex <= 0) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please provide Driver Type",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        dbdtpp.dbDriveComboBox.setFocusable(true);
                    } else {
                        String selectedDriver = (String) dbdtpp.dbDriveComboBox.getSelectedItem();
                        if (DbUtilConstants.ORACLE_OCI_DRIVER.equalsIgnoreCase(selectedDriver)) {
                            props.setDriverType(DbUtilConstants.ORACLE_OCI_DRIVER);
                            CardLayout cl = (CardLayout) wizPane.getLayout();
                            dbtnsepp.tnsEntryNameText.setFocusable(true);
                            cl.show(wizPane, CL_KEY_TNS_ENTRY);
                        }
                        if (DbUtilConstants.ORACLE_THIN_DRIVER.equalsIgnoreCase(selectedDriver)) {
                            props.setDriverType(DbUtilConstants.ORACLE_THIN_DRIVER);
                            CardLayout cl = (CardLayout) wizPane.getLayout();
                            dbcpp.dbUNameText.setFocusable(true);
                            cl.show(wizPane, CL_KEY_DB_PROPERTIES);
                        }
                    }
                } else if (dbtnsepp.isVisible()) {
                    if (EmptyOrNullStringValidator.isEmpty(dbtnsepp.getDbUNameText().getText())) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please Enter User Name",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (EmptyOrNullStringValidator.isEmpty(dbtnsepp.getDbPwdText().getText())) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please Enter Password",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (EmptyOrNullStringValidator.isEmpty(dbtnsepp.getTnsEntryNameText().getText())) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please Enter TNS Entry",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        props.setUserName(dbtnsepp.dbUNameText.getText());
                        props.setPassword(dbtnsepp.dbPwdText.getText());
                        props.setTnsEntry(dbtnsepp.tnsEntryNameText.getText());
                        boolean savetoFile = true;
                        try {
                            ConnectionManager.isConnectionPropertiesValid(props);
                        } catch (DBConnectionFailedException e1) {
                            savetoFile = false;
                            e1.printStackTrace();
                        }
                        if (savetoFile) {
                            savePropertiesToFile(props);
                            // }
                        } else {
                            JOptionPane.showMessageDialog(wizPane,
                                    "Connection Could not be established Please check the values and try again.",
                                    "Connection Failed",
                                    JOptionPane.ERROR_MESSAGE);

                        }
                    }
                } else if (dbcpp.isVisible()) {
                    if (EmptyOrNullStringValidator.isEmpty(dbcpp.getDbHostText().getText())) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please Enter Database host",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (EmptyOrNullStringValidator.isEmpty(dbcpp.getDbPortText().getText()) ||
                            !NumericValidator.isNumeric(dbcpp.getDbPortText().getText(), NumericValidator.INTEGER_TYPE)) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please Enter a valid port",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (EmptyOrNullStringValidator.isEmpty(dbcpp.getDbUNameText().getText())) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please Enter User Name",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    } else if (EmptyOrNullStringValidator.isEmpty(dbcpp.getDbDatabaseNameText().getText())) {
                        JOptionPane.showMessageDialog(wizPane,
                                "Please Enter the database name",
                                "Required Fields",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    props.setHost(dbcpp.dbHostText.getText());
                    props.setPort(dbcpp.dbPortText.getText());
                    props.setDatabaseName(dbcpp.dbDatabaseNameText.getText());
                    props.setUserName(dbcpp.dbUNameText.getText());
                    props.setPassword(dbcpp.dbPwdText.getText());
                    boolean savetoFile = true;
                    try {
                        savetoFile = ConnectionManager.isConnectionPropertiesValid(props);
                    } catch (DBConnectionFailedException e1) {
                        savetoFile = false;
                        JOptionPane.showMessageDialog(wizPane,
                                "Connection Could not be established Please check the values and try again. " +
                                        "The error message is " + e1.getMessage(),
                                "Connection Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    if (savetoFile) {
                        savePropertiesToFile(props);
                    }
                }
            }
            changeNextButtonTitle();
        }

    }

    public void changeNextButtonTitle() {
        if (dbcpp.isVisible() || dbtnsepp.isVisible()) {
            nextButton.getAction().putValue(Action.NAME, "Finish");
        } else {
            nextButton.getAction().putValue(Action.NAME, "Next");
        }
    }

    public class BackButton extends AbstractAction {

        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public BackButton() {
            this("Back");
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         */
        public BackButton(String name) {
            super(name);
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         */
        public BackButton(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == backButton) {
                CardLayout cl = (CardLayout) wizPane.getLayout();
                if (dbdtpp.isVisible()) {
                    cl.show(wizPane, CL_KEY_PROJECT_PROPERTIES);
                } else if (dbtnsepp.isVisible()) {
                    cl.show(wizPane, CL_KEY_DRIVER_TYPE);
                } else if (dbcpp.isVisible()) {
                    if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(props.getDatabaseType()))
                        cl.show(wizPane, CL_KEY_DRIVER_TYPE);
                    else if (DbUtilConstants.DATABASE_TYPE_MYSQL.equals(props.getDatabaseType()))
                        cl.show(wizPane, CL_KEY_PROJECT_PROPERTIES);
                }
            }
            changeNextButtonTitle();
        }
    }

    public class CancelButton extends AbstractAction {
        /**
         * Defines an <code>Action</code> object with a default
         * description string and default icon.
         */
        public CancelButton() {
            this("Cancel");
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a default icon.
         * @param name Name of the cancel button.
         */
        public CancelButton(String name) {
            super(name);
        }

        /**
         * Defines an <code>Action</code> object with the specified
         * description string and a the specified icon.
         * @param name Name of the cancel button
         * @param icon icon to be used for cancel button
         */
        public CancelButton(String name, Icon icon) {
            super(name, icon);
        }

        /**
         * Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelButton) {
                setWizSuccessful(false);
                dispose();
            }
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == projPropsPane.projPropsPathFileChooserButton) {
            JFileChooser directorySelector = new JFileChooser(System.getProperty("user.key"));
            directorySelector.setControlButtonsAreShown(true);
            directorySelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int openVal = directorySelector.showOpenDialog(this);
            if (openVal == JFileChooser.APPROVE_OPTION) {
                projPropsPane.getProjPathText().setText(directorySelector.getSelectedFile().getAbsolutePath());
            }
        }


    }

    private void savePropertiesToFile(DBConnectionProperties dbcpProps) {
        projPropsPane.getProjectProperties().setProjectName(projPropsPane.getProjName());
        String path = projPropsPane.getProjPathText().getText() + File.separator + projPropsPane.getProjName();
        String childName = projPropsPane.getProjName() + ".dbp";
        File pFile = new File(path);
        try {
            if (!pFile.exists()) {
                pFile.mkdirs();
            }
            File propsFile = new File(pFile, childName);
            projPropsPane.getProjectProperties().setProjectPath(propsFile.getAbsolutePath());

            if (!propsFile.exists())
                propsFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(propsFile);
            Properties conProperties;
            conProperties = new Properties();
            if (dbcpProps != null) {
                conProperties.setProperty(DbUtilConstants.DBUSER, dbcpProps.getUserName());
                conProperties.setProperty(DbUtilConstants.DBPASSWORD, dbcpProps.getPassword());
                if (!EmptyOrNullStringValidator.isEmpty(dbcpProps.getDatabaseName()))
                    conProperties.setProperty(DbUtilConstants.DBDATABASENAME, dbcpProps.getDatabaseName());
                if (!EmptyOrNullStringValidator.isEmpty(dbcpProps.getHost()))
                    conProperties.setProperty(DbUtilConstants.DBHOST, dbcpProps.getHost());
                if (!EmptyOrNullStringValidator.isEmpty(dbcpProps.getPort()))
                    conProperties.setProperty(DbUtilConstants.DBPORT, dbcpProps.getPort());
                if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(dbcpProps.getDatabaseType())) {
                    conProperties.setProperty(DbUtilConstants.DBUTIL_DRIVER_TYPE, dbcpProps.getDriverType());
                    if (DbUtilConstants.ORACLE_OCI_DRIVER.equals(dbcpProps.getDriverType())) {
                        conProperties.setProperty(DbUtilConstants.DBUTIL_TNS_ENTRY, dbcpProps.getTnsEntry());
                    }
                }
                conProperties.setProperty(DbUtilConstants.DBUTIL_DATABASE_TYPE, dbcpProps.getDatabaseType());
            }
            conProperties.store(fos, "DB Connection Properties");
            fos.close();
            setWizSuccessful(true);
            RecentProjectsParser rpr = new RecentProjectsParser();
            rpr.writeToRecentProjects(projPropsPane.getProjectProperties().getProjectName(),
                    propsFile.getAbsolutePath(),
                    "yes");
            dispose();
            StartMe.getContainer().openProject(projPropsPane.getProjectProperties());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSED) {
            if (isWizSuccessful()) {
                ((DBUtilContainer) this.getParent()).clearDesktopComponents();
                BodyBrowserPane bbp = new BodyBrowserPane();
                bbp.setVisible(true);
                StartMe.getContainer().addToDesktop(bbp, BorderLayout.CENTER);
            }
        }
    }

    public ProjectPropertiesPane getProjPropsPane() {
        return projPropsPane;
    }

    public void setProjPropsPane(ProjectPropertiesPane projPropsPane) {
        this.projPropsPane = projPropsPane;
    }

    public DBConnectionPropertiesPane getDbcpp() {
        return dbcpp;
    }

    public void setDbcpp(DBConnectionPropertiesPane dbcpp) {
        this.dbcpp = dbcpp;
    }

    public DBDriverTypePropertyPane getDbdtpp() {
        return dbdtpp;
    }

    public void setDbdtpp(DBDriverTypePropertyPane dbdtpp) {
        this.dbdtpp = dbdtpp;
    }

    public DBTnsEntryPropertyPane getDbtnsepp() {
        return dbtnsepp;
    }

    public void setDbtnsepp(DBTnsEntryPropertyPane dbtnsepp) {
        this.dbtnsepp = dbtnsepp;
    }

    public JPanel getWizPane() {
        return wizPane;
    }

    public void setWizPane(JPanel wizPane) {
        this.wizPane = wizPane;
    }

    public boolean isWizSuccessful() {
        return wizSuccessful;
    }

    public void setWizSuccessful(boolean wizSuccessful) {
        this.wizSuccessful = wizSuccessful;
    }

    public JPanel getTopDetailsPane() {
        return topDetailsPane;
    }

    public void setTopDetailsPane(JPanel topDetailsPane) {
        this.topDetailsPane = topDetailsPane;
    }

    public DBConnectionProperties getProps() {
        return props;
    }

    public void setProps(DBConnectionProperties props) {
        this.props = props;
    }

    public JPanel getButtonsPanel() {
        return buttonsPanel;
    }

    public void setButtonsPanel(JPanel buttonsPanel) {
        this.buttonsPanel = buttonsPanel;
    }

    public JButton getNextButton() {
        return nextButton;
    }

    public void setNextButton(JButton nextButton) {
        this.nextButton = nextButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void setBackButton(JButton backButton) {
        this.backButton = backButton;
    }

    public JButton getFinishButton() {
        return finishButton;
    }

    public void setFinishButton(JButton finishButton) {
        this.finishButton = finishButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(JButton cancelButton) {
        this.cancelButton = cancelButton;
    }

    private ProjectPropertiesPane projPropsPane;
    private DBConnectionPropertiesPane dbcpp;
    private DBDriverTypePropertyPane dbdtpp;
    private DBTnsEntryPropertyPane dbtnsepp;
    private JPanel wizPane, topDetailsPane, buttonsPanel;
    private boolean wizSuccessful;
    private JButton nextButton, backButton, finishButton, cancelButton;
    DBConnectionProperties props = new DBConnectionProperties();

    protected static final String CL_KEY_PROJECT_PROPERTIES = "Project Properties";
    protected static final String CL_KEY_DRIVER_TYPE = "Driver Type";
    protected static final String CL_KEY_TNS_ENTRY = "TNS Entry";
    protected static final String CL_KEY_DB_PROPERTIES = "DB Properties";
}
