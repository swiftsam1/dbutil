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

public class ColumnInfo extends BaseInfo implements IColumnInfo{


    public String getType() {
        return type;
    }

    public void setType(String type) {
        String temp = this.type;
        this.type = type;
        firePropertyChange("Type", temp, type);
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        boolean temp = this.nullable;
        this.nullable = nullable;
        firePropertyChange("Nullable", new Boolean(temp), new Boolean(this.nullable));
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        boolean temp = this.key;
        this.key = key;
        firePropertyChange("Key",new Boolean(temp), new Boolean(this.key));
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        String temp = this.defaultValue;
        this.defaultValue = defaultValue;
        firePropertyChange("defaultValue", temp, this.defaultValue);
    }

    public String toString() {
        return getName();
    }

    private String type;
    private boolean nullable;
    private boolean key;
    private String defaultValue;

}
