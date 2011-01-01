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
package org.wsm.database.tools.editor.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.wsm.database.tools.DbUtilConstants;
import org.wsm.database.tools.ILoadTestTypesConstants;
import org.wsm.database.tools.UIConstants;
import org.wsm.database.tools.util.ConnectionManager;
import org.wsm.database.tools.util.DisplayUtil;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;
import org.wsm.database.tools.util.NumericValidator;
import org.wsm.database.tools.util.SwingUtils;
import org.wsm.database.tools.util.SwingWorker;
import org.wsm.database.tools.wizard.ui.BaseWizardPane;

public class LoadTestPropertiesDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	/**
     * Creates a non-modal dialog without a title and without a specified
     * <code>Frame</code> owner.  A shared, hidden frame will be
     * set as the owner of the dialog.
     * <p/>
     * This constructor sets the component's locale property to the value
     * returned by <code>JComponent.getDefaultLocale</code>.
     *
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     *                           returns true.
     * @see GraphicsEnvironment#isHeadless
     * @see JComponent#getDefaultLocale
     * @param parent parent frame
     */
    public LoadTestPropertiesDialog(JFrame parent) throws HeadlessException {
        super(parent, true);
        JPanel propsPane = new BaseWizardPane();

        //setTitle("Load Test Properties");


        GridBagLayout gbl = new GridBagLayout();

        numOfThreadsLabel = SwingUtils.constructLabel("Number Of Concurrent Threads", Color.WHITE, true);

        gbl.setConstraints(numOfThreadsLabel, SwingUtils.getGridBagConstraints(0,0));
        propsPane.add(numOfThreadsLabel);

        numOfThreadsText = new JTextField(UIConstants.TEXT_FIELD_COLUMNS);
        gbl.setConstraints(numOfThreadsText, SwingUtils.getGridBagConstraints(1,0));
        propsPane.add(numOfThreadsText);

        if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(ConnectionManager.currentDbProps.getDatabaseType())) {
            typeOfLoadTestLabel = new JLabel("Type Of Load Test");
            typeOfLoadTestLabel.setFont(UIConstants.LABEL_FONT);
            gbl.setConstraints(typeOfLoadTestLabel, SwingUtils.getGridBagConstraints(0,1));
            propsPane.add(typeOfLoadTestLabel);

            typeOfLoadTestCombo = new JComboBox(loadTestTypes);
            typeOfLoadTestCombo.setEditable(false);
            gbl.setConstraints(typeOfLoadTestCombo, SwingUtils.getGridBagConstraints(1,1));
            propsPane.add(typeOfLoadTestCombo);
        }

        isConcurrent = new JCheckBox("Run Threads Concurrently", true);
        isConcurrent.setForeground(Color.WHITE);
        isConcurrent.setOpaque(false);
        isConcurrent.setBackground(propsPane.getBackground());
        gbl.setConstraints(isConcurrent,SwingUtils.getGridBagConstraints(0,2,2,1));
        propsPane.add(isConcurrent);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(13, 32, 12));

        cancelLoadTestButton = new JButton("Cancel");
        cancelLoadTestButton.setFont(UIConstants.BUTTON_FONT);
        cancelLoadTestButton.addActionListener(this);
        buttonsPanel.add(cancelLoadTestButton);

        runLoadTestButton = new JButton("Run");
        runLoadTestButton.setFont(UIConstants.BUTTON_FONT);
        runLoadTestButton.addActionListener(this);
        buttonsPanel.add(runLoadTestButton);


        propsPane.setLayout(gbl);
        getContentPane().add(propsPane, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

        Rectangle r = DisplayUtil.getCenterOfTheWindow(parent);
        r.width = 500;
        r.height = 200;
        this.setBounds(r);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelLoadTestButton) {
            this.setVisible(false);
            this.dispose();
        } else if (e.getSource() == runLoadTestButton) {
            if (EmptyOrNullStringValidator.isEmpty(getNumOfThreadsText().getText())) {
                JOptionPane.showMessageDialog(this.getParent(),
                        "Please enter Number of Threads for Load Test",
                        "Required Fields",
                        JOptionPane.OK_OPTION);
                return;
            }
            if (!NumericValidator.isNumeric(
                    getNumOfThreadsText().getText(),
                    NumericValidator.INTEGER_TYPE)) {
                JOptionPane.showMessageDialog(this.getParent(), "Please enter Numeric Values Only",
                        "Required Fields", JOptionPane.OK_OPTION);
                return;
            }

            final String command = EditorPane.getCurrentEditorTab().getCommandPane().getCommandTextPane().getText();


            if (EmptyOrNullStringValidator.isEmpty(command)) {
                JOptionPane.showMessageDialog(this.getParent(),
                        "Please enter the qurey to be profiled",
                        "Required Fields",
                        JOptionPane.OK_OPTION);
                return;
            }
            String testType = "";
            if (DbUtilConstants.DATABASE_TYPE_ORACLE.equals(ConnectionManager.currentDbProps.getDatabaseType())) {
                testType = (String) getTypeOfLoadTestCombo().getSelectedItem();
            }
            final String testTypeFinal = testType;
            final QueryResultPane erp = new QueryResultPane(command,
                                Integer.parseInt(getNumOfThreadsText().getText()),
                                testTypeFinal,
                                isConcurrent.isSelected());

            EditorPane.getCurrentEditorTab().setResultsPane(erp);

            SwingWorker sw = new SwingWorker() {

                /**
                 * Compute the value to be returned by the <code>get</code> method.
                 */
                public Object construct() {
                    erp.runLoadTest();
                    return null;
                }
            };
            sw.start();
//            SwingUtilities.invokeLater(
//                    new Runnable() {
//                        public void run() {
//
//                            QueryResultPane erp = new QueryResultPane(command,
//                                    Integer.parseInt(getNumOfThreadsText().getText()),
//                                    testTypeFinal);
//                            EditorPane.getCurrentEditorTab().setResultsPane(erp);
//                        }
//                    }
//            );
            this.dispose();
        }
    }

    public JTextField getNumOfThreadsText() {
        return numOfThreadsText;
    }

    public void setNumOfThreadsText(JTextField numOfThreadsText) {
        this.numOfThreadsText = numOfThreadsText;
    }

    public JLabel getNumOfThreadsLabel() {
        return numOfThreadsLabel;
    }

    public void setNumOfThreadsLabel(JLabel numOfThreadsLabel) {
        this.numOfThreadsLabel = numOfThreadsLabel;
    }

    public JComboBox getTypeOfLoadTestCombo() {
        return typeOfLoadTestCombo;
    }

    public void setTypeOfLoadTestCombo(JComboBox typeOfLoadTestCombo) {
        this.typeOfLoadTestCombo = typeOfLoadTestCombo;
    }

    public JLabel getTypeOfLoadTestLabel() {
        return typeOfLoadTestLabel;
    }

    public void setTypeOfLoadTestLabel(JLabel typeOfLoadTestLabel) {
        this.typeOfLoadTestLabel = typeOfLoadTestLabel;
    }

    public JButton getRunLoadTestButton() {
        return runLoadTestButton;
    }

    public void setRunLoadTestButton(JButton runLoadTestButton) {
        this.runLoadTestButton = runLoadTestButton;
    }

    public JButton getCancelLoadTestButton() {
        return cancelLoadTestButton;
    }

    public void setCancelLoadTestButton(JButton cancelLoadTestButton) {
        this.cancelLoadTestButton = cancelLoadTestButton;
    }

    public JCheckBox getConcurrent() {
        return isConcurrent;
    }

    public void setConcurrent(JCheckBox concurrent) {
        isConcurrent = concurrent;
    }

    private String[] loadTestTypes = {ILoadTestTypesConstants.THIN_DRIVER,
            ILoadTestTypesConstants.OCI_DRIVER,
            ILoadTestTypesConstants.TAF_THIN_DRIVER,
            ILoadTestTypesConstants.TAF_OCI_DRIVER,
            ILoadTestTypesConstants.FAN_OCI_DRIVER};

    private JTextField numOfThreadsText;
    private JLabel numOfThreadsLabel, typeOfLoadTestLabel;
    private JComboBox typeOfLoadTestCombo;
    private JButton runLoadTestButton, cancelLoadTestButton;
    private JCheckBox isConcurrent;
}
