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
/**
 * This is ADI Propreitary information and should n.
 * User: smovva
 * Date: Apr 6, 2007
 * Time: 2:54:45 PM
 */

package org.wsm.database.tools.browser.ui;

import javax.swing.JPanel;
import javax.swing.JTree;

public class QueryCatalogTab extends JPanel{

    public QueryCatalogTab() {
        super();

    }

    public JTree getQueryTree() {
        return queryTree;
    }

    public void setQueryTree(JTree queryTree) {
        this.queryTree = queryTree;
    }

    private JTree queryTree;

}
