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
package org.wsm.database.tools.query;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.wsm.database.tools.StartMe;

import com.smartworks.dbprofiler.qc.QueryCatalogDocument;

/**
 * This class is responsible to catalog each Queries for this project.
 * When a project is kicked off, the catalog manager loads all the information from a catalog if one exists or
 * creates a new catalog for this project.
 * <p/>
 * The information is stored in a XML file. Commons Digester is used to parse the QueryCatalog file and also to store
 * any new information to the catalog.
 */
public class CatalogManager {
    private static final Logger log = Logger.getLogger(CatalogManager.class);
    private static File catalogFile;

    static {
        log.debug("Initializing catalog file");
        log.debug("The Current project properties are " + StartMe.getContainer().getCurrentProjectProperties());
        catalogFile = new File(StartMe.getContainer().getCurrentProjectProperties().getJustProjectDir(), "qc.xml");
        log.debug("CatalogFile path is " + catalogFile.getAbsolutePath());
        if (!catalogFile.exists()) {
            log.debug("Catalog file does not exist");
            log.debug("Write permissions acquired to create catalog file");
            try {
                catalogFile.createNewFile();
                createNewQueryCatalog();
                //parseCatalog();
            } catch (IOException e) {
                log.error("Error opening catalog file", e);
            }
        } else {
            parseCatalog();
        }
    }

    private static QueryCatalogDocument.QueryCatalog createNewQueryCatalog() {
        qcDoc = QueryCatalogDocument.Factory.newInstance();
        log.debug("Create new catalog called");
        XmlOptions opts = (new XmlOptions()).setSavePrettyPrint();
        log.debug("adding new Query Catalog root element");
        catalog = qcDoc.addNewQueryCatalog();
        log.debug("Setting Project Name " + StartMe.getContainer().getCurrentProjectProperties().getProjectName());
        catalog.setProjectName(StartMe.getContainer().getCurrentProjectProperties().getProjectName());
        try {
            qcDoc.save(catalogFile, opts);
            log.debug(catalog.xmlText(opts));
        } catch (IOException e) {
             log.error("Error Saving the newQueryCatalog to file ", e);
        }
        return catalog;
    }

    private static void parseCatalog() {
        try {
            log.debug("Before parsing Query Catalog");
            qcDoc = QueryCatalogDocument.Factory.parse(catalogFile);
            catalog = qcDoc.getQueryCatalog();
            log.debug("Catalog Parsed ");
            log.debug("Catalog value is " + catalog.xmlText());
        } catch (XmlException e) {
            log.error("Error parsing catalog", e);
        } catch (IOException e) {
            log.error("Error parsing catalog", e);
        }
    }

    private static QueryCatalogDocument qcDoc;
    private static QueryCatalogDocument.QueryCatalog catalog;


    public static QueryCatalogDocument.QueryCatalog getCatalog() {
        return catalog;
    }

    public static void setCatalog(QueryCatalogDocument.QueryCatalog catalog) {
        CatalogManager.catalog = catalog;
    }
}

