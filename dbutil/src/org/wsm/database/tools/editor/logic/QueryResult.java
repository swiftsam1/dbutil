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
import java.util.Vector;

import javax.swing.event.SwingPropertyChangeSupport;

import org.wsm.database.tools.util.RandomStringGenerator;

public class QueryResult {

    public QueryResult() {
        header = new Vector(1);
        data = new Vector(5);
        RandomStringGenerator rsg = new RandomStringGenerator();
        firingPropName = rsg.generateRandomString();
        dataFiringPropName = firingPropName + "_data";
        headerFiringPropName = firingPropName + "_header";
        dataRowAddedFiringPropName = dataFiringPropName + "_row_added";
    }

    public Vector getHeader() {
        return header;
    }

    public void setHeader(Vector header) {
        Vector old = (Vector)this.header.clone();
        this.header = header;
        firePropertyChange(headerFiringPropName,old,this.header);
    }

    public Vector getData() {
        return data;
    }

    public void addDataRow(Vector row) {
        this.data.add(row);
        //firePropertyChange(dataRowAddedFiringPropName, null, row);
    }

    public void setData(Vector data) {
        Vector old = (Vector)this.data.clone();
        this.data = data;
        firePropertyChange(dataFiringPropName, old, this.data );
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

    public String getDataFiringPropName() {
        return dataFiringPropName;
    }

    public void setDataFiringPropName(String dataFiringPropName) {
        this.dataFiringPropName = dataFiringPropName;
    }

    public String getHeaderFiringPropName() {
        return headerFiringPropName;
    }

    public void setHeaderFiringPropName(String headerFiringPropName) {
        this.headerFiringPropName = headerFiringPropName;
    }

    public String getDataRowAddedFiringPropName() {
        return dataRowAddedFiringPropName;
    }

    public void setDataRowAddedFiringPropName(String dataRowAddedFiringPropName) {
        this.dataRowAddedFiringPropName = dataRowAddedFiringPropName;
    }


    /**
     * The Resultset Metadata header of result set for the query
     * Each element in the header vector represents the column name of the resultset.
     * This represents the Alias name mentioned in the query.
     */
    private Vector header;

    /**
     * Data is a vector of vector of colum data.
     * A vector of column data represents a Row
     * A vector of vector or column data = Vector of Rows = Resulset data
     */
    private Vector data;

    /**
     * Holds the resultset values as obtained from executing the query.
     */
    //private ResultSet rs;

    private String firingPropName;
    private String dataFiringPropName;
    private String headerFiringPropName;
    private String dataRowAddedFiringPropName;
}
