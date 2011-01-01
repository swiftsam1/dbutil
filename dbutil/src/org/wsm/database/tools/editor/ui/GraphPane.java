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


import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.wsm.database.tools.UIConstants;
import org.wsm.database.tools.editor.logic.LoadTestResult;
import org.wsm.database.tools.editor.logic.QueryExecStatsInfo;
import org.wsm.database.tools.util.SwingUtils;


public class GraphPane extends JPanel implements PropertyChangeListener, ChangeListener, ActionListener {
    public GraphPane() {
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        this.qesi = new QueryExecStatsInfo();
        this.qesi.addPropertyChangeListener(this);
        dcd = new DefaultCategoryDataset();
        JFreeChart chart = getChart(CHART_TYPE_BAR_3D);

        chart.setBackgroundPaint(Color.white);

        cp = new ChartPanel(chart);
        cp.setBorder(BorderFactory.createTitledBorder(
                null,
                "Graph",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                UIConstants.LABEL_FONT));

        cp.setMouseZoomable(true, true);
        //cp.setPreferredSize(new Dimension(700, 500));
        gbl.setConstraints(cp, SwingUtils.getGridBagConstraints(0, 0, 2, 1));

        this.add(cp);
        JPanel graphTypeSelectionPanel = new JPanel();
        viewBarGraph = new JRadioButton("View Bar Graph", true);
        viewLineGraph = new JRadioButton("View Line Graph", false);
        viewLineGraph.setEnabled(false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(viewBarGraph);
        bg.add(viewLineGraph);

        GridBagLayout graphTypeGBL = new GridBagLayout();
        graphTypeSelectionPanel.setLayout(graphTypeGBL);

        graphTypeGBL.setConstraints(viewBarGraph, SwingUtils.getGridBagConstraints(0, 0));
        graphTypeGBL.setConstraints(viewLineGraph, SwingUtils.getGridBagConstraints(1, 0));
        graphTypeSelectionPanel.add(viewBarGraph);
        graphTypeSelectionPanel.add(viewLineGraph);
        viewBarGraph.addActionListener(this);
        viewLineGraph.addActionListener(this);

        graphTypeSelectionPanel.setBorder(BorderFactory.createTitledBorder(
                null,
                "Graph Type",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                UIConstants.LABEL_FONT));

        gbl.setConstraints(graphTypeSelectionPanel, SwingUtils.getGridBagConstraints(0, 1, 2, 1));

        this.add(graphTypeSelectionPanel);
        //this.setBounds(50, 100, 100, 200);
        this.setBackground(Color.WHITE);
    }

    public static final String CHART_TYPE_BAR_3D = "Bar";
    public static final String CHART_TYPE_LINE_3D = "Line";

    public JFreeChart getChart(String chartType) {
        if (CHART_TYPE_BAR_3D.equals(chartType))
            return ChartFactory.createBarChart3D("Load Test Results",
                    "Thread Id",
                    "Time taken in Seconds",
                    dcd,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    true);
        else if (CHART_TYPE_LINE_3D.equals(chartType))
            return ChartFactory.createLineChart3D("Load Test Results",
                    "Thread Id",
                    "Time taken in Seconds",
                    dcd,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    true);

        else
            return null;
    }


    public void paintComponent(Graphics g) {
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewBarGraph) {
            JFreeChart chart = getChart(CHART_TYPE_BAR_3D);
            chart.setBackgroundPaint(Color.white);
            cp.setChart(chart);
        }
        if (e.getSource() == viewLineGraph) {
            JFreeChart chart = getChart(CHART_TYPE_LINE_3D);
            chart.setBackgroundPaint(Color.white);
            cp.setChart(chart);
        }

    }

    public DefaultCategoryDataset dcd;

    public void propertyChange(PropertyChangeEvent evt) {
        if (qesi.getLtrAddedFiringPropName().equals(evt.getPropertyName())) {
            LoadTestResult ltr = (LoadTestResult) evt.getNewValue();
            dcd.addValue(new Double(ltr.getQt().getQueryExecutionTime()).doubleValue() / 1000
                    , "Time for query execution",
                    String.valueOf(ltr.getThreadNumber()));
            dcd.addValue(new Double(ltr.getQt().getConnectionCreationTime()).doubleValue() / 1000
                    , "Time to create connection",
                    String.valueOf(ltr.getThreadNumber()));
            if (dcd.getColumnCount() > 1) {
                if (!viewLineGraph.isEnabled()) viewLineGraph.setEnabled(true);
            }
        }
    }


    public QueryExecStatsInfo getQesi() {
        return qesi;
    }

    public void setQesi(QueryExecStatsInfo qesi) {
        this.qesi = qesi;
    }


    public JLabel getGraph() {
        return graph;
    }

    public void setGraph(JLabel graph) {
        this.graph = graph;
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    public void stateChanged(ChangeEvent e) {
        repaint();
    }

    public JRadioButton getViewBarGraph() {
        return viewBarGraph;
    }

    public void setViewBarGraph(JRadioButton viewBarGraph) {
        this.viewBarGraph = viewBarGraph;
    }

    public JRadioButton getViewLineGraph() {
        return viewLineGraph;
    }

    public void setViewLineGraph(JRadioButton viewLineGraph) {
        this.viewLineGraph = viewLineGraph;
    }

    public ChartPanel getCp() {
        return cp;
    }

    public void setCp(ChartPanel cp) {
        this.cp = cp;
    }

    private QueryExecStatsInfo qesi;

    private JLabel graph;
    private JRadioButton viewBarGraph, viewLineGraph;
    private ChartPanel cp;

}
