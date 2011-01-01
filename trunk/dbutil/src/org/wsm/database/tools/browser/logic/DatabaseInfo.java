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
package org.wsm.database.tools.browser.logic;

import java.util.LinkedList;

public class DatabaseInfo extends BaseInfo implements IDatabaseInfo {

    public LinkedList getTablesInfo() {
        return tablesInfo;
    }

    public void addTableInfo(ITableInfo info) {
        LinkedList temp = tablesInfo;
        if (this.tablesInfo == null) {
            this.tablesInfo = new LinkedList();
        }
        this.tablesInfo.add(info);
        firePropertyChange("NewTableInfoAdded", temp, this.tablesInfo);
    }

    public boolean updateTableInfo(int index, ITableInfo info) {
        boolean updated = false;
        if (tablesInfo == null || tablesInfo.size() < index) return updated;
        else {
            ITableInfo temp = (ITableInfo)tablesInfo.get(index);
            tablesInfo.set(index, info);
            firePropertyChange("TableInfoUpdated", temp, tablesInfo.get(index));
            updated = true;
        }
        return updated;
    }

    public void setTablesInfo(LinkedList tablesInfo) {
        LinkedList temp = this.tablesInfo;
        this.tablesInfo = tablesInfo;
        firePropertyChange("tablesInfo", temp, this.tablesInfo);
    }

    private LinkedList tablesInfo;
}
