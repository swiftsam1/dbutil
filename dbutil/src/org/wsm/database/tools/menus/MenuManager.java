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
package org.wsm.database.tools.menus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.UIConstants;
import org.wsm.database.tools.actions.ActionsHolder;
import org.wsm.database.tools.config.business.RecentProject;
import org.wsm.database.tools.config.parsers.RecentProjectsParser;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;
import org.wsm.database.tools.wizard.logic.ProjectProperties;

public class MenuManager extends JMenuBar {

    private static final Logger log = Logger.getLogger(MenuManager.class);

    public MenuManager() {
        //Destroyer.addToGlobalDesctructionChain(new ActionsHolder());

        log.debug("Initializing Menus ");
        file = new JMenu("File");

        file.setMnemonic(KeyEvent.VK_F);
        file.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
        if (!ActionsHolder.isInitialized()) ActionsHolder.init();

        fileNew = new JMenu("New");
        fileNew.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);

        fileMenuItemNew = new JMenuItem(ActionsHolder.getNewProjectAction());
        fileMenuItemNew.setMnemonic(KeyEvent.VK_N);
        fileMenuItemNew.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
        //fileMenuItemNew.addActionListener(this);
        newEditorMI = new JMenuItem(ActionsHolder.getNewEditorAction());
        newEditorMI.setMnemonic(KeyEvent.VK_T);
        newEditorMI.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);

        fileNew.add(fileMenuItemNew);
        fileNew.add(newEditorMI);

        fileMenuItemOpen = new JMenuItem(ActionsHolder.getOpenProjectAction());
        fileMenuItemOpen.setMnemonic(KeyEvent.VK_O);
        //fileMenuItemOpen.addActionListener(this);
        fileMenuItemOpen.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);


        file.add(fileNew);
        file.add(fileMenuItemOpen);

        file.add(getRecentFileMenuItem());

        tools = new JMenu("Tools");
        tools.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
        tools.setMnemonic(KeyEvent.VK_T);

        toolsMenuItemRun = new JMenuItem(ActionsHolder.getRunQueryAction());
        toolsMenuItemRun.setMnemonic(KeyEvent.VK_R);
        toolsMenuItemRun.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
        tools.add(toolsMenuItemRun);

        toolsMenuItemLoadTest = new JMenuItem(ActionsHolder.getLoadTestAction());
        toolsMenuItemLoadTest.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
        toolsMenuItemLoadTest.addMouseListener(ActionsHolder.getLoadTestAction());
        tools.add(toolsMenuItemLoadTest);

        /*RegisterONSMonitorAction roma = new RegisterONSMonitorAction("ONS Monitor");
      toolsMenuItemONSMonitor = new JCheckBoxMenuItem(roma);
      toolsMenuItemONSMonitor.addItemListener(roma);
      toolsMenuItemONSMonitor.setState(false);
      toolsMenuItemONSMonitor.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);

      tools.add(toolsMenuItemONSMonitor);
        */

        /*GraphicsEnvironment f = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = f.getAvailableFontFamilyNames();
        for(int i=0; i< fonts.length; i++) {
            JMenuItem ji = new JMenuItem(fonts[i]);
            ji.setFont(new Font(fonts[i],Font.PLAIN,11));
            file.add(ji);
        }*/


        this.add(file);
        this.add(tools);

        this.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));

    }


    private JMenu getRecentFileMenuItem() {

        log.debug("Loading menu item for reopening recent Projects.");

        JMenu rfListMain = new JMenu("Reopen");
        rfListMain.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
        if (!EmptyOrNullStringValidator.isEmpty(StartMe.getContainer().getDbUtilHome())) {
            RecentProjectsParser rpr = new RecentProjectsParser();
            Vector rpVec = rpr.getRecentProjectsDetails();
            if (rpVec.size() <= 0) {
                JMenuItem rpmi = new JMenuItem("--No Projects--");
                rpmi.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
                rpmi.setEnabled(false);
                rfListMain.add(rpmi);
                rfListMain.addSeparator();
            } else {
                JMenuItem topMenu = new JMenuItem("--Recent Projects--");
                topMenu.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
                topMenu.setEnabled(false);
                rfListMain.add(topMenu);
                rfListMain.addSeparator();
                Iterator it = rpVec.iterator();
                while (it.hasNext()) {
                    final RecentProject rp = (RecentProject) it.next();
                    JMenuItem rpmi = new JMenuItem(rp.getPath());
                    rpmi.setFont(UIConstants.MENU_FONT_COURIER_PLAIN_12);
                    rpmi.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String path = ((JMenuItem) e.getSource()).getText();
                            File f = new File(path);
                            if (f.exists()) {
                                StartMe.getContainer().openProject(new ProjectProperties(rp.getName(), rp.getPath()));
                            }
                            if (!f.exists()) {
                                log.debug("Error opening properties file with path " + path);
                                JOptionPane.showMessageDialog(StartMe.getContainer(),
                                        "Properties File does not exist",
                                        "Properties file does not exist",
                                        JOptionPane.ERROR_MESSAGE);                                
                            }

                        }
                    });
                    rfListMain.add(rpmi);
                }
            }
        }
        return rfListMain;
    }

    /*private void openProject(File propertiesFile) {
        log.debug("Loading Project " + propertiesFile);
        Properties prts = new Properties();
        boolean openNew = true;
        try {
            FileInputStream fis = new FileInputStream(propertiesFile);
            prts.load(fis);
        } catch (FileNotFoundException e1) {
            openNew = false;
            e1.printStackTrace();
        } catch (IOException ioe) {
            openNew = false;
            ioe.printStackTrace();
        }
        DBConnectionProperties dbcp = new DBConnectionProperties();
        dbcp.setUserName(prts.getProperty(DbUtilConstants.DBUSER));
        dbcp.setPassword(prts.getProperty(DbUtilConstants.DBPASSWORD));
        dbcp.setHost(prts.getProperty(DbUtilConstants.DBHOST));
        dbcp.setPort(prts.getProperty(DbUtilConstants.DBPORT));
        dbcp.setDatabaseName(prts.getProperty(DbUtilConstants.DBDATABASENAME));
        dbcp.setDatabaseType(prts.getProperty(DbUtilConstants.DBUTIL_DATABASE_TYPE));
        if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(dbcp.getDatabaseType())) {
            dbcp.setDriverType(prts.getProperty(DbUtilConstants.DBUTIL_DRIVER_TYPE));
            if (DbUtilConstants.ORACLE_OCI_DRIVER.equals(dbcp.getDriverType())) {
                dbcp.setTnsEntry(prts.getProperty(DbUtilConstants.DBUTIL_TNS_ENTRY));
            }
        }
        log.debug("Loaded connection properties " + dbcp);
        try {
            ConnectionManager.getConnection(dbcp);
        } catch (DBConnectionFailedException e1) {
            log.error("Error connecting to database using the properties " + dbcp);
            openNew = false;
            JOptionPane.showMessageDialog(StartMe.container,
                    e1.getMessage(),
                    "Connection Failed",
                    JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        } catch (ConnectionPoolDataSourceCreationException e) {
            openNew = false;
            JOptionPane.showMessageDialog(StartMe.container,
                    e.getMessage(),
                    "Datasource Creation Failed",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        if (openNew) {
            Destroyer.executeDestruction();
            DBUtilContainer.newProjectKickOff();
        }
    }
    */

    public JMenu getFile() {
        return file;
    }

    public void setFile(JMenu file) {
        this.file = file;
    }

    public JMenu getTools() {
        return tools;
    }

    public void setTools(JMenu tools) {
        this.tools = tools;
    }

    public JMenuItem getFileMenuItemNew() {
        return fileMenuItemNew;
    }

    public void setFileMenuItemNew(JMenuItem fileMenuItemNew) {
        this.fileMenuItemNew = fileMenuItemNew;
    }

    public JMenuItem getFileMenuItemOpen() {
        return fileMenuItemOpen;
    }

    public void setFileMenuItemOpen(JMenuItem fileMenuItemOpen) {
        this.fileMenuItemOpen = fileMenuItemOpen;
    }

    public JMenuItem getToolsMenuItemRun() {
        return toolsMenuItemRun;
    }

    public void setToolsMenuItemRun(JMenuItem toolsMenuItemRun) {
        this.toolsMenuItemRun = toolsMenuItemRun;
    }

    public JMenuItem getToolsMenuItemLoadTest() {
        return toolsMenuItemLoadTest;
    }

    public void setToolsMenuItemLoadTest(JMenuItem toolsMenuItemLoadTest) {
        this.toolsMenuItemLoadTest = toolsMenuItemLoadTest;
    }

    public JCheckBoxMenuItem getToolsMenuItemONSMonitor() {
        return toolsMenuItemONSMonitor;
    }

    public void setToolsMenuItemONSMonitor(JCheckBoxMenuItem toolsMenuItemONSMonitor) {
        this.toolsMenuItemONSMonitor = toolsMenuItemONSMonitor;
    }

    public JMenu getFileNew() {
        return fileNew;
    }

    public void setFileNew(JMenu fileNew) {
        this.fileNew = fileNew;
    }

    public JMenuItem getNewEditorMI() {
        return newEditorMI;
    }

    public void setNewEditorMI(JMenuItem newEditorMI) {
        this.newEditorMI = newEditorMI;
    }

    private JMenu file, tools, fileNew;
    private JMenuItem fileMenuItemNew, newEditorMI, fileMenuItemOpen, toolsMenuItemRun, toolsMenuItemLoadTest;

    private JCheckBoxMenuItem toolsMenuItemONSMonitor;

    //private JFrame parentFrame;

}
