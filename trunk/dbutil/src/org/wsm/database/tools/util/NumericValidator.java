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

public class NumericValidator {

    public static final int INTEGER_TYPE = 1;

    public static final int LONG_TYPE = 2;
    public static final int FLOAT_TYPE = 3;

    public static final int DOUBLE_TYPE = 4;

    public static boolean isNumeric(String o, int type) {


        try {
            switch(type) {
                case 1:
                    int i = Integer.parseInt(o);
                    break;
                case 2:
                    long l = Long.parseLong(o);
                    break;
                case 3:
                    float f = Float.parseFloat(o);
                    break;
                case 4:
                    double d = Double.parseDouble(o);
                    break;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
