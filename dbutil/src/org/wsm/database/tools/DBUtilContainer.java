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
package org.wsm.database.tools;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.wsm.database.tools.browser.logic.DatabaseInformationUtil;
import org.wsm.database.tools.browser.logic.IDatabaseInfo;
import org.wsm.database.tools.browser.ui.BodyBrowserPane;
import org.wsm.database.tools.menus.MenuManager;
import org.wsm.database.tools.toolbars.ToolbarManager;
import org.wsm.database.tools.util.ConnectionManager;
import org.wsm.database.tools.util.ConnectionPoolDataSourceCreationException;
import org.wsm.database.tools.util.DBConnectionFailedException;
import org.wsm.database.tools.util.Destroyer;
import org.wsm.database.tools.util.FileNotLoadbleException;
import org.wsm.database.tools.util.PropertiesLoaderUtil;
import org.wsm.database.tools.wizard.logic.DBConnectionProperties;
import org.wsm.database.tools.wizard.logic.ProjectProperties;

public class DBUtilContainer extends JFrame implements Chain, WindowListener {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(DBUtilContainer.class);

    public DBUtilContainer(String home) {
        dbUtilHome = home;
        BorderLayout borderLayout = new BorderLayout();
        getContentPane().setLayout(borderLayout);

        log.debug("Initializing DBUtil Container");
        log.debug("Adding DBUtil container to the destruction Chain");
        Destroyer.addToGlobalDesctructionChain(this);
        Destroyer.addToGlobalDesctructionChain(new ConnectionManager());
        try {
            log.debug("Setting the default Look and Feel of the current Environment "
                    + UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            log.fatal(UIManager.getCrossPlatformLookAndFeelClassName() + " could not be found when loading he look and feel");
            e.printStackTrace();
        } catch (InstantiationException e) {
            log.fatal("Error trying to instantiate the Look and Feel");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            log.fatal("Illegal access when insantiating the Look and Feel");
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            log.fatal(UIManager.getCrossPlatformLookAndFeelClassName() + " Look and Feel is not supported in the environment ");
            e.printStackTrace();
        }
        Dimension d = getToolkit().getScreenSize();
        Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration());
        this.pack();
        d.setSize(d.getWidth() - (screenInsets.left + screenInsets.right), d.getHeight() - (screenInsets.bottom + screenInsets.top));
        this.setSize(d);
        this.setTitle("DB Profiler");
    }

    public void initialize() {
        menuHolder = new MenuManager();
        toolbarHolder = new ToolbarManager();
        toolbarHolder.setVisible(true);

        desktop = new JDesktopPane();
        desktop.setLayout(new BorderLayout());
        statusPanel = new JPanel();

        statusMessageLbl = new JLabel("Welcome to Profiler", JLabel.LEFT);
        statusMessageLbl.setVerticalAlignment(JLabel.BOTTOM);
        statusMessageLbl.setFont(UIConstants.STATUS_FONT);

        BoxLayout bl = new BoxLayout(statusPanel, BoxLayout.X_AXIS);
        statusPanel.add(statusMessageLbl);
        statusPanel.setLayout(bl);
        statusPanel.setVisible(true);

        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(menuHolder, BorderLayout.NORTH);
        topPanel.add(new JSeparator(), BorderLayout.CENTER);
        topPanel.add(toolbarHolder, BorderLayout.SOUTH);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        //getContentPane().add(toolbarHolder,BorderLayout.NORTH);
        getContentPane().add(desktop, BorderLayout.CENTER);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);

        //addToDesktop(toolbarHolder, BorderLayout.NORTH);

    }


    public void clearDesktopComponents() {
        if ((getDesktop().getComponentCount() > 0)) {
            for (int i = 0; i < getDesktop().getComponents().length; i++) {
                getDesktop().remove(i);
            }
        }

    }

    public void repaint() {
        super.repaint();
    }


    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void setDesktop(JDesktopPane dtop) {
        desktop = dtop;
    }

    public void addToDesktop(Component c, Object constraints) {
        if (desktop == null) {
            desktop = new JDesktopPane();
            desktop.setLayout(new BorderLayout());
        }
        desktop.invalidate();
        desktop.add(c, constraints);
        desktop.validate();
    }

    private void newProjectKickOff() {
        menuHolder = new MenuManager();
        toolbarHolder = new ToolbarManager();
        this.databaseInfo = DatabaseInformationUtil.getDatabaseInfo();
        bbp = new BodyBrowserPane();
        StartMe.getContainer().addToDesktop(bbp, BorderLayout.CENTER);
        bbp.setVisible(true);
        setProjectActive(true);
    }

/*
    private void newProjectKickOff(File propsFile) {
        DBConnectionProperties dbProps = null;
        try {
            dbProps = new DBConnectionProperties(PropertiesLoaderUtil.loadProperties(propsFile));
        } catch (FileNotLoadbleException e) {
            e.printStackTrace();
        }
        if (dbProps != null) {
            try {
                ConnectionManager.loadProperties(dbProps);
            } catch (DBConnectionFailedException e) {
                e.printStackTrace();
            } catch (ConnectionPoolDataSourceCreationException e) {
                e.printStackTrace();
            }
        }
        newProjectKickOff();
    }
*/

    public void openProject(ProjectProperties properties) {
        log.debug("Loading Project " + properties);
        File connectionProperties = new File(properties.getProjectPath());
        if (!connectionProperties.exists()) {
            log.debug("Error opening properties file with path " + properties.getProjectPath());
            JOptionPane.showMessageDialog(StartMe.getContainer(),
                    "Error Opening connection Properties File",
                    "Error Opening Connection Properties File",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean openNew;
        Properties prts;
        try {
            prts = PropertiesLoaderUtil.loadProperties(connectionProperties);
        } catch (FileNotLoadbleException e) {
            log.error("Error Opening connectionProperties ", e);
            JOptionPane.showMessageDialog(StartMe.getContainer(),
                    e.getMessage(),
                    "Error Opening Connection Properties",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        DBConnectionProperties dbcp = new DBConnectionProperties(prts);
        log.debug("Loaded connection properties " + dbcp);
        try {
            if (isProjectActive()) {
                log.debug("Project is active");
                openNew = ConnectionManager.isConnectionPropertiesValid(dbcp);
            } else {
                log.debug("Project is not active");
                openNew = true;
            }
        } catch (DBConnectionFailedException e1) {
            log.error("Error connecting to database using the properties " + dbcp);
            JOptionPane.showMessageDialog(StartMe.getContainer(),
                    e1.getMessage(),
                    "Connection Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (openNew) {
            log.debug("Open New is true");
            try {
                ConnectionManager.loadProperties(dbcp);
            } catch (DBConnectionFailedException e) {
                log.error("Error connecting to database using the properties " + dbcp);
                JOptionPane.showMessageDialog(StartMe.getContainer(),
                        e.getMessage(),
                        "Connection Failed",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            } catch (ConnectionPoolDataSourceCreationException e) {
                log.error("Error connecting to database using the properties " + dbcp);
                JOptionPane.showMessageDialog(StartMe.getContainer(),
                        e.getMessage(),
                        "Datasource Creation Failed",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
            if (isProjectActive()) {
                log.debug("Project is active");
                Destroyer.addToGlobalDesctructionChain(StartMe.getContainer());
                Destroyer.addToGlobalDesctructionChain(new ConnectionManager());
                Destroyer.executeDestruction();
                setProjectActive(false);
            }
            setCurrentProjectProperties(properties);
            newProjectKickOff();
        }
    }


    public void destroy() {
        log.debug("Destroy in DBUtil Container is called");
        if ((getDesktop().getComponentCount() > 0)) {
            for (int i = 0; i < getDesktop().getComponents().length; i++) {
                getDesktop().remove(i);
            }
            getDesktop().invalidate();
            bbp.invalidate();
            bbp = null;
            Component[] allComponents = StartMe.getContainer().getRootPane().getContentPane().getComponents();
            for (int j = 0; j < allComponents.length; j++) {
                if (allComponents[j] instanceof JDesktopPane) {
                    ((JDesktopPane) allComponents[j]).removeAll();
                }
            }
        }
    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     */
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     */
    public void windowClosed(WindowEvent e) {

    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.  If the program does not
     * explicitly hide or dispose the window while processing
     * this event, the window close operation will be cancelled.
     */
    public void windowClosing(WindowEvent e) {
        Destroyer.executeDestruction();
    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     */
    public void windowDeactivated(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     */
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @see java.awt.Frame#setIconImage
     */
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Invoked the first time a window is made visible.
     */
    public void windowOpened(WindowEvent e) {

    }


    public void setStatusMessageLbl(String message) {
        statusMessageLbl.setText(message);
    }

    public JMenuBar getMenuHolder() {
        return menuHolder;
    }

    public void setMenuHolder(JMenuBar menuHolder) {
        this.menuHolder = menuHolder;
    }

    public JToolBar getToolbarHolder() {
        return toolbarHolder;
    }

    public void setToolbarHolder(JToolBar toolbarHolder) {
        this.toolbarHolder = toolbarHolder;
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }

    public void setStatusPanel(JPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public void setTopPanel(JPanel topPanel) {
        this.topPanel = topPanel;
    }

    public JLabel getStatusMessageLbl() {
        return statusMessageLbl;
    }

    public void setStatusMessage(JLabel statusMessage) {
        this.statusMessageLbl = statusMessage;
    }

    public String getDbUtilHome() {
        return dbUtilHome;
    }

    public void setDbUtilHome(String dbUtilHome) {
        this.dbUtilHome = dbUtilHome;
    }

    public BodyBrowserPane getBbp() {
        return bbp;
    }

    public void setBbp(BodyBrowserPane bbp) {
        this.bbp = bbp;
    }

    public boolean isProjectActive() {
        return projectActive;
    }

    public IDatabaseInfo getDatabaseInfo() {
        return databaseInfo;
    }

    public void setDatabaseInfo(IDatabaseInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }


    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        this.statusMessageLbl.setText(statusMessage);
    }

    public void setProjectActive(boolean projectActive) {
        log.debug("Setting project status to " + projectActive);
        boolean oldValue = this.projectActive;
        this.projectActive = projectActive;
        String title = currentProjectProperties.getProjectName() + " - " + currentProjectProperties.getProjectPath();
        log.debug("Setting the Frame title to " + title);
        this.setTitle(title);
        firePropertyChange(PROJECT_STATUS, oldValue, this.projectActive);
    }


    public ProjectProperties getCurrentProjectProperties() {
        return currentProjectProperties;
    }

    public void setCurrentProjectProperties(ProjectProperties currentProjectProperties) {
        this.currentProjectProperties = currentProjectProperties;
    }

    private BodyBrowserPane bbp;
    private JMenuBar menuHolder;
    private JToolBar toolbarHolder;
    private JDesktopPane desktop;
    private JPanel statusPanel, topPanel;
    private JLabel statusMessageLbl;
    private String statusMessage;
    private String dbUtilHome;
    private boolean projectActive;
    public static final String PROJECT_STATUS = "projectStatus";

    /**
     * The Full information about the database will be stored in this object. THis will be the source (or model) for the
     * rest of the project
     */
    public IDatabaseInfo databaseInfo;

    /**
     * The current project in use.
     */
    private ProjectProperties currentProjectProperties;
}
