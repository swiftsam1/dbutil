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
package org.wsm.database.tools.browser.ui;


import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.wsm.database.tools.Chain;
import org.wsm.database.tools.util.Destroyer;

public class BrowserPane extends JPanel implements Chain {
    public BrowserPane() {
        super();
        Destroyer.addToGlobalDesctructionChain(this);
        this.setLayout(new BorderLayout());
        tabPane = new JTabbedPane();
        tableTab = new TableBrowserTab();
        tabPane.addTab("Table", null, tableTab, "Tables for current Owner");
        this.add(tabPane);
        this.setVisible(true);
    }




    public void destroy() {
        this.tabPane.remove(tableTab);
        this.remove(tabPane);
    }

    private JTabbedPane tabPane;
    private TableBrowserTab tableTab;
    //private ViewBrowserTab viewTab;
}
