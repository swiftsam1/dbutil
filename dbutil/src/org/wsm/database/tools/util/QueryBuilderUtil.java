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

public class QueryBuilderUtil {

    public static String buildQuery(String table, String[] columns) {

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        boolean first = true;
        if(columns != null) {
            if(columns.length > 0) {
                for(int i=0; i<columns.length; i++) {
                    if(first) {
                        sb.append(" ");
                        first = false;
                    }else{
                        sb.append(",\n ");
                    }
                    sb.append(columns[i] );
                }
            }
        }
        sb.append(" \nFROM " + table);
        return sb.toString();
    }
}
