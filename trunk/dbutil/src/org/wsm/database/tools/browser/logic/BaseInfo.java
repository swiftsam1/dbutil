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

import java.beans.PropertyChangeListener;

import javax.swing.event.SwingPropertyChangeSupport;

public abstract class BaseInfo {


    /**
     * The name of the information holder
     */
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        String temp = this.name;
        this.name = name;
        firePropertyChange("Name",temp,name);

    }

    public SwingPropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }

    public void setChangeSupport(SwingPropertyChangeSupport changeSupport) {
        this.changeSupport = changeSupport;
    }

    private SwingPropertyChangeSupport changeSupport;

    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        //System.out.println("Fire property change called");
        if(changeSupport != null) {
            //System.out.println("Just before firing change event");
            changeSupport.firePropertyChange(propertyName, oldValue,newValue);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listner) {
        //System.out.println("Adding listner for property change");
        if(changeSupport == null) {
            changeSupport = new SwingPropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listner);
    }

    public void firePropertyChangeEvent(String property) {

    }

    //public boolean isE

}
