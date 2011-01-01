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
package org.wsm.database.tools.util.classloaders;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;


/**
 * <p/>
 * The GenericDbUtilClassLoaderFactory builds ClassLoader for DBUtil.
 * By specifying the directory, the factory will build a class loader with the files in
 * the directory added to the repository.
 */
public class GenericDbUtilClassLoaderFactory {


    public static ClassLoader getClassLoader(File[] directories) throws Exception {

        ClassLoader cl = null;
        if (directories == null || directories.length == 0) {
            System.out.println("Directory is null or 0 length");
            return cl;
        }

        ArrayList urlList = new ArrayList();

        for (int i = 0; i < directories.length; i++) {
            File directory = directories[i];
            if (directory == null || !directory.exists() || !directory.isDirectory() || !directory.canRead()) {
                System.out.println("Is not a directory");
                continue;
            }
            String filesList[] = directory.list();
            if ((filesList != null) && (filesList.length > 0)) {
                for (int j = 0; j < filesList.length; j++) {
                    String fileName = filesList[j].toLowerCase();
                    if (fileName.endsWith(".jar")) {
                        System.out.println("fileName is " + fileName);
                        if (!"dbutil.jar".toLowerCase().equals(fileName)) {
                            File file = new File(directory, filesList[j]);
                            URL url = new URL("file", null, file.getCanonicalPath());
                            System.out.println("url added is " + url.toExternalForm());
                            urlList.add(url);
                        }
                    }

                }
            }

        }
        URL arrayUrlList[] = (URL[]) urlList.toArray(new URL[urlList.size()]);
        System.out.println("The final array of url size is " + arrayUrlList);
        cl = new URLClassLoader(arrayUrlList);
        return cl;
    }

}
