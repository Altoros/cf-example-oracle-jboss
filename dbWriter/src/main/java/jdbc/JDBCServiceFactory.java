package jdbc;

import application.properties.JDBCConnectionProperties;
import application.properties.PropertyUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import exception.ApplicationException;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.Properties;

/**
 * @author: Andrey Kozlov
 */
public final class JDBCServiceFactory {

    private static String tableName = "MESSAGE_DATA";
    private static String resetSchema = "resetSchema";
    private static String generatorPropertiesPath = "generator.properties";

    private static  Logger logger = Logger.getRootLogger();
    private static volatile JDBCService jdbcService;

    private JDBCServiceFactory() {
    }

    public static JDBCService getJdbcService(){
        if (jdbcService==null){
            synchronized (JDBCService.class){
                if (jdbcService==null){
                    jdbcService = createJDBCService();
                }
            }
        }
        return jdbcService;
    }

    private static JDBCService createJDBCService(){

        JDBCConnectionProperties connectionProperties = PropertyUtil.getJDBCConnectionData();

        try {
            logger.info("JDBC connection data: " + connectionProperties.toString());

            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass("oracle.jdbc.driver.OracleDriver");
            cpds.setJdbcUrl(connectionProperties.getUrl());
            cpds.setUser(connectionProperties.getUserName());
            cpds.setPassword(connectionProperties.getUserPassword());

            cpds.setMinPoolSize(5);
            cpds.setMaxPoolSize(20);
            cpds.setMaxStatements(180);
            cpds.setIdleConnectionTestPeriod(60);

            initializeSchema(cpds);

            JDBCService jdbcService = new JDBCService(cpds);
            return jdbcService;

        } catch (PropertyVetoException e) {
            ApplicationException.fail("can't set JDBC driver");
        }
        return null;
    }

    private static void initializeSchema(ComboPooledDataSource cpds) {

        if(isResetSchema()){
            checkAndResetTable(cpds);
        }   else {
            checkAndCreateTable(cpds);   
        }
    }

    private static boolean isResetSchema() {
        Properties generatorProperties = PropertyUtil.readProperty(generatorPropertiesPath);
        boolean isReset = Boolean.parseBoolean(generatorProperties.getProperty(resetSchema));
        return isReset;
    }

    private static void checkAndResetTable(ComboPooledDataSource cpds) {
        if (tableExists(cpds)){
            deleteTable(cpds);
        }
        createTable(cpds);
    }

    private static boolean tableExists(ComboPooledDataSource cpds) {
        try (Connection connection = cpds.getConnection()) {
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet tables = dbmd.getTables(null, null, tableName, null);
            if (tables.next()) {
                return true;
            }
        } catch (SQLException e) {
            ApplicationException.fail("Can't get database metadata on current connection", e);
        }
        return false;
    }

    private static void deleteTable(ComboPooledDataSource cpds) {
        logger.info("Start deleting table " + tableName);
        try (Connection connection = cpds.getConnection()){
            connection.prepareStatement("drop table " + tableName).executeUpdate();
            connection.prepareStatement("drop sequence SEQ_MESSAGE_DATA_ID").executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            ApplicationException.fail("Error during deleting Table" + tableName, e);
        }
        logger.info("Table " + tableName + " with all components was deleted");
    }

    private static void createTable(ComboPooledDataSource cpds) {
        try(Connection connection = cpds.getConnection()){
            connection.prepareStatement("CREATE TABLE " + tableName + " (id integer NOT NULL, message integer NOT NULL," +
                    " CONSTRAINT pk_message_data PRIMARY KEY (id))").executeUpdate();
            connection.prepareStatement("create sequence seq_message_data_id start with 10 nocycle").executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            ApplicationException.fail("Error during creation table " + tableName, e);
        }
        logger.info("Table " + tableName + " was created");
    }

    private static void checkAndCreateTable(ComboPooledDataSource cpds) {
        if (tableExists(cpds)){
            logger.info(tableName + " table already exists");
        }else {
            createTable(cpds);
        }
    }

}
