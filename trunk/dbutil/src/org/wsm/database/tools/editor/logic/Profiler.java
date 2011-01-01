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
package org.wsm.database.tools.editor.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.wsm.database.tools.DbUtilConstants;
import org.wsm.database.tools.ILoadTestTypesConstants;
import org.wsm.database.tools.StartMe;
import org.wsm.database.tools.browser.logic.DataRetrievalException;
import org.wsm.database.tools.editor.ui.GenericTableDataModel;
import org.wsm.database.tools.util.BaseLoadTestConnectionManager;
import org.wsm.database.tools.util.ConnectionManager;
import org.wsm.database.tools.util.DBConnectionFailedException;
import org.wsm.database.tools.util.EmptyDataSourceException;
import org.wsm.database.tools.util.EmptyOrNullStringValidator;
import org.wsm.database.tools.util.FANConnectionManager;
import org.wsm.database.tools.util.MysqlBaseLoadTestConnectionManager;
import org.wsm.database.tools.util.OracleBaseLoadTestConnectionManager;
import org.wsm.database.tools.util.RandomStringGenerator;
import org.wsm.database.tools.util.SwingWorker;
import org.wsm.database.tools.util.TAFConnectionManager;

public class Profiler {

	private static final Logger log = Logger.getLogger(Profiler.class);

	public Profiler(String query, int threadCount, String testType,
			QueryExecStatsInfo qesi, GenericTableDataModel gtdm) {
		this(query, threadCount, testType, qesi, gtdm, false);
	}

	public Profiler(String query, int threadCount, String testType,
			QueryExecStatsInfo qesi, GenericTableDataModel gtdm,
			boolean isConcurrent) {
		this.query = query;
		this.qt = new QueryTiming();
		this.testType = testType;
		this.requestedNumOfThreads = threadCount;
		this.qesi = qesi;
		this.gtdm = gtdm;
		runConcurrently = (isConcurrent) ? Boolean.TRUE : Boolean.FALSE;
	}

	public void runQuery() {
		SwingWorker sw = new ProfileAwayThread();
		sw.start();
	}

	public void runLoadTest() throws EmptyDataSourceException {

		if (requestedNumOfThreads > 1) {
			log.debug("Load Test Thread count is " + requestedNumOfThreads);
			RandomStringGenerator rsg = new RandomStringGenerator();
			String randomStr = rsg.generateRandomString();
			this.randomDsString = randomStr;
			log.debug("Using random string " + randomDsString
					+ " for using as Key in the DataSource Bucket");
			determineConnectionManager();
			cm.setNumberOfConnections(requestedNumOfThreads);
			cm.setupDataSource(randomStr);
			resetLoadTestProps();
			if (!runConcurrently.booleanValue()) {
				startThreads = true;
			}
			profilerThreads = new ProfilerThread[requestedNumOfThreads];
			for (int i = 0; i < requestedNumOfThreads; i++) {
				QueryTiming qt = new QueryTiming();
				qt.setTimeBeforeConnection(System.currentTimeMillis());
				Connection con = null;
				try {
					con = cm.getConnection(randomStr);
					if (con != null) {
						qt.setTimeAfterConnection(System.currentTimeMillis());
						ProfilerThread profThread = new ProfilerThread(i + 1,
								this, con, qt);
						profilerThreads[i] = profThread;
						log.debug("Thread " + (i + 1) + ", Ready to run ");
						profThread.start();
					}
				} catch (DBConnectionFailedException e) {
					BaseLoadTestConnectionManager
							.removeConnectionPoolDataSourceFromBucket(randomDsString);
					e.printStackTrace();
				}
			}
			StartThreads();
		}
	}

	private void resetLoadTestProps() {
		log.debug("Resetting the Loadtest initialization properties");
		concurrentThreadCount = 0;
		startThreads = false;
	}

	public ProfilerThread[] getProfilerThreads() {
		return profilerThreads;
	}

	public void setProfilerThreads(ProfilerThread[] profilerThreads) {
		this.profilerThreads = profilerThreads;
	}

	private ProfilerThread[] profilerThreads;

	public synchronized BaseLoadTestConnectionManager getConnectionManager() {
		return cm;
	}

	public synchronized final String getRandomDsString() {
		return randomDsString;
	}

	private BaseLoadTestConnectionManager cm;

	private void determineConnectionManager() {
		if (DbUtilConstants.DATABASE_TYPE_MYSQL
				.equals(ConnectionManager.currentDbProps.getDatabaseType())) {
			log.debug("The Database Type is "
					+ DbUtilConstants.DATABASE_TYPE_MYSQL);
			cm = new MysqlBaseLoadTestConnectionManager();
		} else if (DbUtilConstants.DATABASE_TYPE_ORACLE
				.equals(ConnectionManager.currentDbProps.getDatabaseType())) {
			log.debug("The Database Type is "
					+ DbUtilConstants.DATABASE_TYPE_ORACLE);
			if (!EmptyOrNullStringValidator.isEmpty(testType)) {
				log.debug("The test type is " + testType);
				if ((ILoadTestTypesConstants.THIN_DRIVER
						.equalsIgnoreCase(testType))
						|| (ILoadTestTypesConstants.OCI_DRIVER
								.equalsIgnoreCase(testType))) {
					cm = new OracleBaseLoadTestConnectionManager();
				} else if (ILoadTestTypesConstants.TAF_THIN_DRIVER
						.equalsIgnoreCase(testType)
						|| (ILoadTestTypesConstants.TAF_OCI_DRIVER
								.equalsIgnoreCase(testType))) {
					cm = new TAFConnectionManager();
				} else if (ILoadTestTypesConstants.FAN_OCI_DRIVER
						.equalsIgnoreCase(testType)) {
					cm = new FANConnectionManager();
				}
			} else {
				cm = new OracleBaseLoadTestConnectionManager();
			}
		}
	}

	/**
     *
     */
	private static int concurrentThreadCount = 0;
	private static boolean startThreads = false;

	/**
	 * @param increment
	 */
	public synchronized void changeThreadCount(boolean increment) {

		if (increment) {
			concurrentThreadCount++;
		} else {
			concurrentThreadCount--;
		}
	}

	public synchronized void StartThreads() {
		startThreads = true;
	}

	public synchronized void StopThreads() {
		startThreads = false;
	}

	public synchronized boolean canStartThreads() {
		return startThreads;
	}

	/**
	 * @return int
	 */
	public synchronized int getThreadCount() {
		return concurrentThreadCount;
	}

	/**
     *
     */
	public synchronized void printResults(int threadId, int currentThreadCount,
			long conTimeTaken, long timeTaken) {

		// Write the Reults to a file.
		log.debug("Thread Id: " + threadId + " Con Time Taken:" + conTimeTaken
				+ " taken: " + timeTaken + " currentThreadCount: "
				+ currentThreadCount);
	}

	/**
     *
     */

	private class ProfileAwayThread extends SwingWorker {

		public ProfileAwayThread() {
		}

		public void profileAway() throws DBConnectionFailedException,
				DataRetrievalException {
			Connection con = null;
			Statement stmt = null;
			try {
				qt.setTimeBeforeConnection(System.currentTimeMillis());
				con = ConnectionManager.getConnection();
				qt.setTimeAfterConnection(System.currentTimeMillis());
				qt.setTimeBeforeCreatingStatement(System.currentTimeMillis());
				stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				qt.setTimeAfterCreatingStatement(System.currentTimeMillis());
				qt.setTimeBeforeExecutingQuery(System.currentTimeMillis());
				boolean resSet = stmt.execute(query);
				qt.setTimeAfterExecutingQuery(System.currentTimeMillis());
				if (requestedNumOfThreads == 1) {
					LoadTestResult ltr = new LoadTestResult();
					ltr.setQuery(query);
					ltr.setThreadNumber(1);
					ltr.setQt(qt);
					addLoadTestAnalysisToResults(ltr);
				}
				if (resSet) {
					ResultSet rs = stmt.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
					String[] header = new String[rsmd.getColumnCount()];
					for (int i = 0; i < rsmd.getColumnCount(); i++) {
						/*
						 * System.out.println("The column info is " +
						 * "Catalog Name: " +rsmd.getCatalogName(i+1) +
						 * "Column Class Name: " + rsmd.getColumnClassName(i+1)
						 * + "Column Display Size: " +
						 * rsmd.getColumnDisplaySize(i+1) + "Column  Lable: " +
						 * rsmd.getColumnLabel(i+1) + "Column Name: " +
						 * rsmd.getColumnName(i+1) + "Column type: " +
						 * rsmd.getColumnType(i+1) + "Column Type Name: " +
						 * rsmd.getColumnTypeName(i+1) + "Column Precision: " +
						 * rsmd.getPrecision(i+1) + "Column Schema: " +
						 * rsmd.getSchemaName(i+1) + "Column Table Name: " +
						 * rsmd.getTableName(i+1) + "Column Scale: " +
						 * rsmd.getScale(i+1) + "Auto increment: " +
						 * rsmd.isAutoIncrement(i+1) + "Is currency: " +
						 * rsmd.isCurrency(i+1) + "Is Definetely writable: " +
						 * rsmd.isDefinitelyWritable(i+1) + "Is Nullable: " +
						 * rsmd.isNullable(i+1) + "Read Only: " +
						 * rsmd.isReadOnly(i+1) + "Searchable: " +
						 * rsmd.isSearchable(i+1) + "Signed: " +
						 * rsmd.isSigned(i+1) + "Writable: " +
						 * rsmd.isWritable(i+1) );
						 */
						header[i] = rsmd.getColumnLabel(i + 1);
					}
					gtdm.setColumns(header);
					if (rs != null) {
						rs.last();
						rs.beforeFirst();
						while (rs.next()) {
							final int columnCount = gtdm.getColumnCount();
							Object[] rowData = new Object[columnCount];
							Object[] originalRowData = new Object[columnCount];
							for (int k = 0; k < rsmd.getColumnCount(); k++) {
								rowData[k] = rs.getObject(k + 1);
								originalRowData[k] = rs.getObject(k + 1);
							}
							gtdm.addOriginalRow(originalRowData);
							gtdm.addRow(rowData);
						}
					}
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new DataRetrievalException(e.getMessage());
			} finally {
				try {
					if (stmt != null)
						stmt.close();
					if (con != null)
						con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}

		/**
		 * When an object implementing interface <code>Runnable</code> is used
		 * to create a thread, starting the thread causes the object's
		 * <code>run</code> method to be called in that separately executing
		 * thread.
		 * <p/>
		 * The general contract of the method <code>run</code> is that it may
		 * take any action whatsoever.
		 * 
		 * @see Thread#run()
		 */
		public Object construct() {
			try {
				profileAway();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(StartMe.getContainer(),
						"The Following error occured while retrieving data : "
								+ e.getMessage(), "Invalid Query",
						JOptionPane.ERROR_MESSAGE);
				System.out.println("" + e.toString());
				return null;
			}

			return null;
		}

		public void finished() {
			gtdm.fireTableStructureChanged();
		}
	}

	public synchronized void addLoadTestAnalysisToResults(LoadTestResult ltr) {
		qesi.addToStatsList(ltr);
		if (qesi.getStatsList().size() >= requestedNumOfThreads) {
			resetLoadTestProps();
			BaseLoadTestConnectionManager
					.removeConnectionPoolDataSourceFromBucket(randomDsString);
		}
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public QueryTiming getQt() {
		return qt;
	}

	public void setQt(QueryTiming qt) {
		this.qt = qt;
	}

	/**
	 * The query to be profiled.
	 */
	private String query;

	/**
	 * This holds the system time at diffent stages of execution of query.
	 */
	private QueryTiming qt;

	public int getRequestedNumOfThreads() {
		return requestedNumOfThreads;
	}

	public void setRequestedNumOfThreads(int requestedNumOfThreads) {
		this.requestedNumOfThreads = requestedNumOfThreads;
	}

	public GenericTableDataModel getGtdm() {
		return gtdm;
	}

	public void setGtdm(GenericTableDataModel gtdm) {
		this.gtdm = gtdm;
	}

	public static long numOfThreads;
	public static long totalTime;
	private int requestedNumOfThreads;
	private String testType;
	private String randomDsString;
	private QueryExecStatsInfo qesi;
	private GenericTableDataModel gtdm;
	private Boolean runConcurrently;
}
