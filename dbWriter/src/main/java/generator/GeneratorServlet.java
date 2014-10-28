package generator;

import application.properties.PropertyUtil;
import exception.ApplicationException;
import jdbc.JDBCServiceFactory;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import java.util.Properties;

/**
 * @author: Andrey Kozlov
 */
public class GeneratorServlet extends HttpServlet {

    private static final long serialVersionUID = 1686949367194754087L;
    private static final String generatorProperties = "generator.properties";
    private static final String generatePeriod = "generatePeriod";
    private final int generatePeriodVal = readGeneratePeriod(generatorProperties);

    static Logger logger = Logger.getRootLogger();

    @Override
    public void init(){
        initGenerator();
    }

    public void initGenerator() {
        logger.info("start generator initialization");
        MessageGenerator messageGenerator = createMessageGenerator();
        startMessageGenerator(messageGenerator);
        logger.info("generator initialized");
    }

    private MessageGenerator createMessageGenerator() {
        MessageGenerator messageGenerator = new MessageGenerator(generatePeriodVal);
        messageGenerator.setJdbcService(JDBCServiceFactory.getJdbcService());
        return messageGenerator;
    }

    private static void startMessageGenerator(MessageGenerator messageGenerator) {
        Thread generatorThred = new Thread(messageGenerator);
        generatorThred.start();
        logger.info("generator thread was started");
    }

    private static int readGeneratePeriod(String generatorProperties) {
        Properties properties = PropertyUtil.readProperty(generatorProperties);
        if (properties.getProperty(generatePeriod)!=null){
            return Integer.parseInt(properties.getProperty(generatePeriod));
        }else {

            ApplicationException.fail("Can't read generator properties file");
            return 0;
        }
    }

}
