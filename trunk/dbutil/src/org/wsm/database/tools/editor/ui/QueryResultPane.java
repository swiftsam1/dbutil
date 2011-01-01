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

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.editor.logic.Profiler;

public class QueryResultPane extends JPanel {

    public QueryResultPane(final String query, final int numOfThreads, final String testType, final boolean runConcurrently) {
        this.testType = testType;
        graphPane = new GraphPane();
        resultsPane = new ResultsPane();
        try {
            profiler = new Profiler(query, numOfThreads, testType, graphPane.getQesi(), resultsPane.getGtdm(), runConcurrently);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(StartMe.getContainer(),
                    "The Following error occured while retrieving data : " + ex.getMessage(),
                    "Invalid Query",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }
        this.setLayout(new BorderLayout());
        JTabbedPane profile = new JTabbedPane();
        profile.addTab("Results", null, getResultsPane(), "Results for Query");
        profile.addTab("Analysis", null, getGraphsTab(), "Graphical analysis of Query");
        profile.addChangeListener(graphPane);
        this.add(profile, BorderLayout.CENTER);
        this.setVisible(true);
        profiler.runQuery();
    }


    public QueryResultPane(String query) {
        this(query, 1, "", false);
    }


    public void runLoadTest() {
        try {
           profiler.runLoadTest();
       } catch (Exception e) {
           e.printStackTrace();
           JOptionPane.showMessageDialog(StartMe.getContainer(),
                   "The Following error occured while retrieving data : " + e.getMessage(),
                   "Invalid Query",
                   JOptionPane.ERROR_MESSAGE);

        }

    }
    private JPanel getGraphsTab() {
        return graphPane;
    }


    public Profiler getProfiler() {
        return profiler;
    }

    public void setProfiler(Profiler profiler) {
        this.profiler = profiler;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public GraphPane getGraphPane() {
        return graphPane;
    }

    public void setGraphPane(GraphPane graphPane) {
        this.graphPane = graphPane;
    }

    public ResultsPane getResultsPane() {
        return resultsPane;
    }

    public void setResultsPane(ResultsPane resultsPane) {
        this.resultsPane = resultsPane;
    }

    private Profiler profiler;
    private String testType;
    private GraphPane graphPane;
    private ResultsPane resultsPane;
}
