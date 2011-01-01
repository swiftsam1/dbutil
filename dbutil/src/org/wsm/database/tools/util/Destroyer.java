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


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.wsm.database.tools.Chain;

public class Destroyer {

    public static void executeDestruction() {
        synchronized (globalDestructionChain) {
            destroyChain(globalDestructionChain);
        }
    }

    public static void destroyChain(Collection chainCollection) {
        if ((chainCollection == null) || (chainCollection.size() == 0)) {
            return;
        }
        Iterator it = chainCollection.iterator();
        while (it.hasNext()) {
            Chain c = (Chain) it.next();
            try {
                Method m = c.getClass().getMethod("destroy", null);
                m.invoke(c, null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        chainCollection.clear();
        System.gc();

    }

    public static void addToGlobalDesctructionChain(Chain c) {
        globalDestructionChain.add(c);
    }

    public static Collection getGlobalDestructionChain() {
        return globalDestructionChain;
    }


    private static Collection globalDestructionChain = new Vector();
}
