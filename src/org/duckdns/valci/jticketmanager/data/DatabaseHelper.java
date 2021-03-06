package org.duckdns.valci.jticketmanager.data;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;

public class DatabaseHelper implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(DatabaseHelper.class);

    // SINGLETON PATTERN
    private static DatabaseHelper instance = null;

    private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
    private FileResource dbFile = new FileResource(new File(basepath + "/WEB-INF/resources/"
            + TicketsSQLContainer.TABLE + ".sql"));

    private JDBCConnectionPool connectionPool = null;

    //@formatter:off
    private String dropTableCMD = "DROP TABLE "+ TicketsSQLContainer.TABLE;
    private String createTableCMD = "CREATE TABLE IF NOT EXISTS " + TicketsSQLContainer.TABLE + " ("
            + "ID INTEGER GENERATED BY DEFAULT AS IDENTITY, "
            + "ticketCategory varchar(10), "
            + "ticketStatus varchar(10), "
            + "ticketPriority varchar(10), "
            + "ticketSubject longvarchar, "
            + "ticketAssignee varchar(32), "
            + "ticketUpdate varchar(32), "
            + "ticketAuthor varchar(32), "
            + "version INTEGER DEFAULT 0 NOT NULL, "
            + "primary key (ID))";  
    private String insertSampleDataCMD = 
              "INSERT INTO " + TicketsSQLContainer.TABLE + " VALUES(NULL,'BUG', 'OPEN', 'NORMAL', 'Sample open bug ticket', 'Samuel', '06/11/2014 11:25','Samuel',0);"
            + "INSERT INTO " + TicketsSQLContainer.TABLE + " VALUES(NULL,'BUG', 'ONGOING', 'NORMAL', 'Sample ongoing bug ticket', 'Samuel', '06/11/2014 12:25','Samuel',0);"
            + "INSERT INTO " + TicketsSQLContainer.TABLE + " VALUES(NULL,'BUG', 'ONGOING', 'HIGH', 'Sample ongoing high prio bug ticket', 'Samuel', '06/11/2014 14:25','Samuel',0);"
            + "INSERT INTO " + TicketsSQLContainer.TABLE + " VALUES(NULL,'FEATURE', 'OPEN', 'LOW', 'Sample open low prio feature ticket', 'Samuel', '06/11/2014 15:25','Samuel',0);"
            + "INSERT INTO " + TicketsSQLContainer.TABLE + " VALUES(NULL,'FEATURE', 'CLOSED', 'NORMAL', 'Sample closed medium prio feature ticket', 'Samuel', '06/11/2014 15:26','Samuel',0);";
    private String testTableCMD = "SELECT * FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'";
    private String testTableDataCMD = "SELECT * FROM " + TicketsSQLContainer.TABLE;
    private String deleteAllDataCMD = "delete from " + TicketsSQLContainer.TABLE;

    //@formatter:on

    private DatabaseHelper() {
        initConnectionPool();
        initDatabase();
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            LOG.trace("Instantiating DatabaseHelper");
            instance = new DatabaseHelper();
        }
        LOG.trace("Returning already instantiated DatabaseHelper");
        return instance;
    }

    public void fillTestData() {
        LOG.trace("Deleting all data");
        executeSQLCommand(deleteAllDataCMD);
        LOG.trace("Loading sample data");
        executeSQLCommand(insertSampleDataCMD);
        LOG.trace("Selecting data");
        // executeSQLQuerry(testTableCMD);
        executeSQLQuerry(testTableDataCMD);
    }

    private JDBCConnectionPool initConnectionPool() {
        try {
            String dbPath = dbFile.getSourceFile().getCanonicalPath();
            LOG.trace("Creating Connection Pool " + dbPath);
            // connectionPool = new SimpleJDBCConnectionPool("org.sqlite.JDBC",
            // "jdbc:sqlite:" + dbPath, "", "", 2, 5);
            // connectionPool = new
            // SimpleJDBCConnectionPool("org.hsqldb.jdbc.JDBCDriver",
            // "jdbc:hsqldb:mem:sqlcontainer" + dbPath, "", "", 2, 5);
            connectionPool = new SimpleJDBCConnectionPool("org.hsqldb.jdbc.JDBCDriver", "jdbc:hsqldb:" + dbPath, "",
                    "", 2, 5);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connectionPool;
    }

    public void initDatabase() {
        LOG.trace("Initializing db");
        // executeSQLCommand(dropTableCMD, createTableCMD);
        executeSQLCommand(createTableCMD);
    }

    private void executeSQLCommand(String... commands) {
        try {
            Connection conn = connectionPool.reserveConnection();
            Statement statement = conn.createStatement();
            for (String command : commands) {
                LOG.trace("sqlcmd: " + command);
                statement.execute(command);
            }
            statement.close();
            conn.commit();
            connectionPool.releaseConnection(conn);
        } catch (SQLException e) {
            LOG.trace("ExecuteSQLCommand failed");
            e.printStackTrace();
        }
    }

    static class PrintColumnTypes {

        public static void printColTypes(ResultSetMetaData rsmd) throws SQLException {
            int columns = rsmd.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                int jdbcType = rsmd.getColumnType(i);
                String name = rsmd.getColumnTypeName(i);
                LOG.trace("Column " + i + " is JDBC type " + jdbcType);
                LOG.trace(", which the DBMS calls " + name);
            }
        }
    }

    private void executeSQLQuerry(String... commands) {
        try {
            Connection conn = connectionPool.reserveConnection();
            Statement statement = conn.createStatement();
            for (String command : commands) {
                ResultSet rs = statement.executeQuery(command);
                ResultSetMetaData rsmd = rs.getMetaData();
                PrintColumnTypes.printColTypes(rsmd);
                int numberOfColumns = rsmd.getColumnCount();

                for (int i = 1; i <= numberOfColumns; i++) {
                    if (i > 1)
                        LOG.trace(",  ");
                    String columnName = rsmd.getColumnName(i);
                    LOG.trace(columnName);
                }
                LOG.trace("");

                while (rs.next()) {
                    for (int i = 1; i <= numberOfColumns; i++) {
                        if (i > 1)
                            LOG.trace(",  ");
                        String columnValue = rs.getString(i);
                        LOG.trace(columnValue);
                    }
                    LOG.trace("");
                }
                rs.close();
            }
            statement.close();
            conn.commit();
            connectionPool.releaseConnection(conn);
        } catch (SQLException e) {
            LOG.trace("ExecuteSQLCommand failed");
            e.printStackTrace();
        }
    }

    public JDBCConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void releaseConnections() {
        LOG.trace("Connection pool destroy connections");
        connectionPool.destroy();
    }

    public static void main(String[] args) {
        DatabaseHelper dh = new DatabaseHelper();
    }
}
