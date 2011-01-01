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
package org.wsm.database.tools.util;

import oracle.ons.CallBack;
import oracle.ons.Notification;
import oracle.ons.NotificationProperty;

import org.apache.log4j.Logger;

public class OracleONSCallbackImpl implements CallBack{
    Logger log = Logger.getLogger(OracleONSCallbackImpl.class);
    public void notification_callback(Notification n) {
        log.debug("ONS Notification Received: ");
        log.debug("**Message Start**");
        NotificationProperty[] nps = n.getAllProperties();
        if(nps != null) {
            for(int i=0; i< nps.length; i++) {
                NotificationProperty np = nps[i];
                log.debug("    " + np.name + " : " + np.value);
            }
        }
        log.debug("**Message Ended**" );
    }



}
