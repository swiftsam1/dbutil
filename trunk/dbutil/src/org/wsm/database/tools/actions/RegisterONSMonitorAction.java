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
package org.wsm.database.tools.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import oracle.ons.Subscriber;

import org.apache.log4j.Logger;
import org.wsm.database.tools.util.OracleONSCallbackImpl;

class RegisterONSMonitorAction extends AbstractAction implements ItemListener {

    private static final Logger log = Logger.getLogger(RegisterONSMonitorAction.class);
    Subscriber s;
    public RegisterONSMonitorAction() {
        super();
        s = new Subscriber("", "");
        /*try {
            Class clazz = Class.forName("oracle.ons.Subscriber");
            if(clazz != null) {
                Class[] ctParams = new Class[2];
                ctParams[0] = String.class;
                ctParams[1] = String.class;
                Constructor ct = clazz.getConstructor(ctParams);
                Object[] paramVals = new String[2];
                paramVals[0] = "";
                paramVals[1] = "";
                Object o = ct.newInstance(paramVals);
                Class[] callbackMethodParamsClasses = new Class[2];
                callbackMethodParamsClasses[0] =
                Method m = new Method()
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        */

    }

    public RegisterONSMonitorAction(String name) {
        super(name);
        s = new Subscriber("", "");

    }

    public RegisterONSMonitorAction(String name, Icon icon) {
        super(name, icon);
        s = new Subscriber("", "");
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {

    }

    /**
     * Invoked when an item has been selected or deselected by the user.
     * The code written for this method performs the operations
     * that need to occur when an item is selected (or deselected).
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            log.debug("Starting ONS Monitor");
            s.register_callback(new OracleONSCallbackImpl(), 1);
        }
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            log.debug("Stopping ONS Monitor");
            s.cancel_callback();
        }
    }

}

