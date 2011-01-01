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
package org.wsm.database.tools.config.parsers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.wsm.database.tools.DbUtilConstants;
import org.wsm.database.tools.config.business.RecentProject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class RecentProjectsParser extends BaseParser {

    private static String currentTag = "";
    private static final String PROJECT = "project";
    private static final String PROJECTS = "projects";
    private static final String PATH = "path";
    private static final String ACTIVE = "active";
    private static final String NAME = "name";
    private static final String LAST_USED = "lastUsed";
    private static final String DATE_CREATED = "dateCreated";

    private static final Logger log = Logger.getLogger(RecentProjectsParser.class);

    public void writeToRecentProjects(String projectName, String path, String isActive ) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            //create a Document Builder using the DocumentBuilderFactory
            DocumentBuilder db = dbf.newDocumentBuilder();
            File f = new File(DbUtilConstants.RECENT_FILES_LOCATION, "RecentProjects.xml");
            if(!f.exists()) {
                f.createNewFile();
            }
            //FileWriter fw = new FileWriter(f);
            //Parser the XML file to create new Document using the DocumentBuilder
            Document doc = db.parse(f);
            //Now the doument is an XML Document.
            //XmlDocument d = (XmlDocument)doc;

            OutputStream os = new FileOutputStream(f);
            Writer w = new OutputStreamWriter(os);


            Element prjElement = doc.createElement(PROJECT);
            Attr prjAttr = doc.createAttribute(NAME);
            prjAttr.setValue(projectName);

            Element prjPathElement = doc.createElement(PATH);
            prjPathElement.setTextContent(path);
/*            Node prjPathTxt = new DefaultNode();
            prjPathTxt.setNodeValue(path);
*/
            //prjPathElement.appendChild(prjPathTxt);



            Element prjActiveElement = doc.createElement(ACTIVE);
            prjActiveElement.setTextContent(isActive);
/*            prjActiveElement
            prjActiveElement.setNodeValue(isActive);
*/
            //prjActiveElement.appendChild(prjActiveText);



            prjElement.setAttributeNode(prjAttr);
            prjElement.appendChild(prjPathElement);

            prjElement.appendChild(prjActiveElement);
            NodeList projectsNl = doc.getElementsByTagName(PROJECTS);
            NodeList projectNl = doc.getElementsByTagName(PROJECT);
            Node top = projectsNl.item(0);
            top.insertBefore(prjElement,projectNl.item(0));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result =  new StreamResult(w);
            transformer.transform(source, result);
            
            //doc.write(w);

            /*Node tempTopParentNode = null;
            for(int i=0; i< prjNl.getLength(); i++) {
                Node tNode =  prjNl.item(i);
                NodeList children = tNode.getChildNodes();
                for(int j = 0; j< children.getLength(); j++) {
                    Node tChild = children.item(j);
                    if(PROJECT.equals(tChild.getNodeName())) {
                       tempTopParentNode = tChild;
                        break;
                    }
                }
            }
            NodeList nl  = d.getElementsByTagName(PROJECT);
            for(int k = 0; k< nl.getLength(); k++) {
                tempTopParentNode = nl.item(k);
                break;
            }
            if(tempTopParentNode != null) {
                d.insertBefore(prjElement, tempTopParentNode);
            }  */
            //Element tempElement = (Element)d.getParentNode();
            //d.insertBefore(prjElement,tempElement);
            //d.writeChildrenXml(xwc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public RecentProjectsParser() {
    }

    public Vector getRecentProjectsDetails() {
        try {
            log.debug("Trying to load Recent projects File ");

            File f = new File(DbUtilConstants.RECENT_FILES_LOCATION, "RecentProjects.xml");
            if (f.exists()) {
                log.debug("Recent Projects file Exists ");
                SAXParserFactory pf = SAXParserFactory.newInstance();
                pf.setValidating(false);
                SAXParser parser = pf.newSAXParser();
                parser.parse(f, this);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (FactoryConfigurationError factoryConfigurationError) {
            factoryConfigurationError.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return rpVec;

    }

    public void startDocument() {
        currentTag = "";
    }

    public void startElement(String namespaceUri, String lname, String qname, Attributes attr) {
        currentTag = qname;
        if (PROJECT.equals(qname)) {
            rp = new RecentProject();
            for (int i = 0; i < attr.getLength(); i++) {
                String attrName = attr.getQName(i);
                if (NAME.equals(attrName)) {
                    rp.setName(attr.getValue(attrName));
                }
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (PATH.equals(currentTag)) {
            rp.setPath(String.valueOf(ch, start, length));
        }
        if(ACTIVE.equals(currentTag)) {
            rp.setActive(String.valueOf(ch,start,length));
        }

    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (PROJECT.equals(qName)) {
            rpVec.add(rp);
        }
    }

    private RecentProject rp;

    private Vector rpVec = new Vector(1);
}
