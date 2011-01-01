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
package org.wsm.database.tools.editor.logic;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.event.SwingPropertyChangeSupport;

import org.wsm.database.tools.util.RandomStringGenerator;


public class QueryExecStatsInfo {

    public QueryExecStatsInfo() {
        statsList = new LinkedList();
        RandomStringGenerator rsg = new RandomStringGenerator();
        firingPropName = rsg.generateRandomString();
        ltrAddedFiringPropName = "ltrAdded" + firingPropName;
        ltrRemovedFiringPropName = "ltrRemoved" + firingPropName;
        ltrEditFiringPropName = "ltrEdited" + firingPropName;
        statListSetFiringPropName = "statsListSet" + firingPropName;
    }

    public LinkedList getStatsList() {
        return statsList;
    }

    public void setStatsList(LinkedList statsList) {
        LinkedList tempOld;
        if(statsList != null) {
        tempOld = (LinkedList)statsList.clone();
        } else {
            tempOld = null;
        }
        this.statsList = statsList;
        firePropertyChange(statListSetFiringPropName, tempOld, this.statsList);
    }

    public synchronized void addToStatsList(LoadTestResult ltr) {
        if(statsList == null)
            statsList = new LinkedList();
        statsList.add(ltr);
        firePropertyChange(ltrAddedFiringPropName,
                null, ltr);
    }


    public synchronized void removeFromStatsList(LoadTestResult ltr) {
        if(statsList.contains(ltr)) {
            statsList.remove(ltr);
            firePropertyChange(ltrRemovedFiringPropName,
                    null, ltr);
        }
    }


    private SwingPropertyChangeSupport changeSupport;

    private void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
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

    public String getFiringPropName() {
        return firingPropName;
    }

    public void setFiringPropName(String firingPropName) {
        this.firingPropName = firingPropName;
    }

    public SwingPropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }

    public void setChangeSupport(SwingPropertyChangeSupport changeSupport) {
        this.changeSupport = changeSupport;
    }

    public String getLtrAddedFiringPropName() {
        return ltrAddedFiringPropName;
    }

    public void setLtrAddedFiringPropName(String ltrAddedFiringPropName) {
        this.ltrAddedFiringPropName = ltrAddedFiringPropName;
    }

    public String getLtrRemovedFiringPropName() {
        return ltrRemovedFiringPropName;
    }

    public void setLtrRemovedFiringPropName(String ltrRemovedFiringPropName) {
        this.ltrRemovedFiringPropName = ltrRemovedFiringPropName;
    }

    public String getLtrEditFiringPropName() {
        return ltrEditFiringPropName;
    }

    public void setLtrEditFiringPropName(String ltrEditFiringPropName) {
        this.ltrEditFiringPropName = ltrEditFiringPropName;
    }

    public String getStatListSetFiringPropName() {
        return statListSetFiringPropName;
    }

    public void setStatListSetFiringPropName(String statListSetFiringPropName) {
        this.statListSetFiringPropName = statListSetFiringPropName;
    }


    private LinkedList statsList;
    private String firingPropName;
    private String ltrAddedFiringPropName;
    private String ltrRemovedFiringPropName;
    private String ltrEditFiringPropName;
    private String statListSetFiringPropName;
}
