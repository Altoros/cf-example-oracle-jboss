package generator;

import jdbc.JDBCService;
import org.apache.log4j.Logger;

/**
 * @author: Andrey Kozlov
 */
public class MessageGenerator implements Runnable{

    Logger logger = Logger.getRootLogger();
    private final int generatePeriod;
    JDBCService jdbcService;

    public MessageGenerator(int generatePeriod){
        this.generatePeriod=generatePeriod;
    }

    public void setJdbcService(JDBCService jdbcService) {
        this.jdbcService = jdbcService;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            generateAndSendMessage();
            try {
                Thread.sleep(generatePeriod);
            } catch (InterruptedException e) {
                logger.error("Message Generator Thread was Interrupted");
            }
        }
    }

    private void generateAndSendMessage() {
        int message = GenerateMessageUtil.generateMessage();
        jdbcService.createMessage(message);
        logger.info(message);
    }
}
