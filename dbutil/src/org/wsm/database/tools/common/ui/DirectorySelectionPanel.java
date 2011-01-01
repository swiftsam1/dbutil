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
package org.wsm.database.tools.common.ui;


import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.wsm.database.tools.UIConstants;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;

public class DirectorySelectionPanel extends JPanel{

    public DirectorySelectionPanel() {
        this.assembleStructure();
    }

    public DirectorySelectionPanel(String title) {
        this.title = title;
        this.assembleStructure();
    }

    public DirectorySelectionPanel(String title, String topHeader) {
        this.title = title;
        this.topHeader = topHeader;
        this.assembleStructure();
    }

    public DirectorySelectionPanel(String title, String topHeader, String description) {
        this.title = title;
        this.topHeader = topHeader;
        this.description = description;
        this.assembleStructure();
    }

    private void assembleStructure() {
        //Add the default title for the panel if not provided.
        if(EmptyOrNullStringValidator.isEmpty(this.title)) {
            this.setTitle(DEFAULT_FILE_SELECTION_TITLE);
        } else {
            this.setTitle(this.title);
        }

        JLabel headerLabel = null;
        if(EmptyOrNullStringValidator.isEmpty(this.topHeader)) {
            headerLabel = new JLabel(wrapAsHeader(DEFAULT_TOP_HEADER_DESCRIPTION, 1));
        } else {
            headerLabel = new JLabel(wrapAsHeader(this.topHeader, 1));
        }
        headerLabel.setFont(UIConstants.LABEL_FONT);

        JLabel descriptionLabel = null;
        if(EmptyOrNullStringValidator.isEmpty(this.description)) {
            descriptionLabel = new JLabel(wrapAsHeader(DEFAULT_DESCRIPTION,2));
        } else {
            descriptionLabel = new JLabel(wrapAsHeader(this.description,2));
        }
        descriptionLabel.setFont(UIConstants.LABEL_FONT);


        topPanel = new JPanel();
        topPanel.add(headerLabel);
        topPanel.add(descriptionLabel);
        topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.PAGE_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        directoryChooserPanel = new JPanel();
        directorySelector = new JFileChooser(System.getProperty("user.key"));
        directorySelector.setControlButtonsAreShown(true);
        directorySelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directorySelector.setSize(directoryChooserPanel.getSize());

        directoryChooserPanel.add(directorySelector);
        this.add(topPanel);

        this.add(directoryChooserPanel);
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));


    }

    private String wrapAsHeader(String val, int type) {
        StringBuffer sb = new StringBuffer();
        sb.append("<H" + type);
        sb.append(">");
        sb.append(val);
        sb.append("</H" + type);
        return sb.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopHeader() {
        return topHeader;
    }

    public void setTopHeader(String topHeader) {
        this.topHeader = topHeader;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public void setTopPanel(JPanel topPanel) {
        this.topPanel = topPanel;
    }

    public JPanel getDirectoryChooserPanel() {
        return directoryChooserPanel;
    }

    public void setDirectoryChooserPanel(JPanel directoryChooserPanel) {
        this.directoryChooserPanel = directoryChooserPanel;
    }

    public JFileChooser getDirectorySelector() {
        return directorySelector;
    }

    public void setDirectorySelector(JFileChooser directorySelector) {
        this.directorySelector = directorySelector;
    }

    private String title;
    private String topHeader;
    private String description;
    private JPanel topPanel;
    private JPanel directoryChooserPanel;
    private JFileChooser directorySelector;
    public static final String DEFAULT_FILE_SELECTION_TITLE = "Select Path";
    public static final String DEFAULT_TOP_HEADER_DESCRIPTION = "Select Directory";
    public static final String DEFAULT_DESCRIPTION= "Selected Directory will be used.";
}
