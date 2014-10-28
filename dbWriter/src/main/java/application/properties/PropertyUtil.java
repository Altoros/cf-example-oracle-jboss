package application.properties;

import exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: Andrey Kozlov
 */
public final class PropertyUtil {

    private static final String userName = "username";
    private static final String password = "password";
    private static final String url = "url";

    private static final String vcapServices="VCAP_SERVICES";

    private static Logger logger = Logger.getRootLogger();

    private PropertyUtil() {
    }

    public static Properties readProperty(String propertyPath) {
        InputStream inputStream = null;
        Properties prop = null;
        try {
            inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyPath);
            prop = new Properties();
            prop.load(inputStream);
        } catch (FileNotFoundException e) {
            ApplicationException.fail("Queue properties file is not found");
        } catch (IOException e) {
            ApplicationException.fail("Can't read queue properties file");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ApplicationException.fail(e);
                }
            }
        }
        return prop;
    }

    public static JDBCConnectionProperties getJDBCConnectionData(){
        if (jdbcUserPropertiesAvailable()){
            JDBCConnectionProperties connectionData = loadJdbcUserConnectionData();
            logger.info("user properties are available");
            return connectionData;
        }
        if (jdbcSystemPropertiesAvailable()){
            JDBCConnectionProperties connectionData = loadJdbcSystemConnectionData();
            logger.info("system provided properties are available");
            return connectionData;
        }
        ApplicationException.fail("JDBC properties are not available");
        return null;
    }

    private static boolean jdbcUserPropertiesAvailable() {
        return  System.getenv(userName)!=null&&
                System.getenv(password)!=null&&
                System.getenv(url)!=null;
    }

    private static JDBCConnectionProperties loadJdbcUserConnectionData() {
        JDBCConnectionProperties connectionData = new JDBCConnectionProperties();
        connectionData.setUserName(System.getenv(userName));
        connectionData.setUserPassword(System.getenv(password));
        connectionData.setUrl(System.getenv(url));
        return connectionData;
    }

    private static boolean jdbcSystemPropertiesAvailable() {
        try {
            if (StringUtils.isNotBlank(System.getenv(vcapServices))) {
                logger.info(vcapServices + ": " + System.getenv(vcapServices));
                JSONObject daas = getCredentialFromSystemVCAP();
                return StringUtils.isNotBlank(daas.getString(url))
                        && StringUtils.isNotBlank(daas.getString(userName))
                        && StringUtils.isNotBlank(daas.getString(password));
            }
            logger.info(vcapServices + " is blank");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static JSONObject getCredentialFromSystemVCAP() {
        String jsonString = System.getenv(vcapServices);
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getJSONArray("DaaS")
                .getJSONObject(0).getJSONObject("credentials");
    }

    private static JDBCConnectionProperties loadJdbcSystemConnectionData(){
        JDBCConnectionProperties connectionData = new JDBCConnectionProperties();
        JSONObject daas = getCredentialFromSystemVCAP();
        connectionData.setUrl(daas.getString(url));
        connectionData.setUserName(daas.getString(userName));
        connectionData.setUserPassword(daas.getString(password));
        return connectionData;
    }

}
