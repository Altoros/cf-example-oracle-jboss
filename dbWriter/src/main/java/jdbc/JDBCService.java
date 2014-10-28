package jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import exception.ApplicationException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: Andrey Kozlov
 */
public class JDBCService {

    Logger logger = Logger.getRootLogger();
    
    private static final String insertRabbitmqSQL = "INSERT INTO message_data (id, message) VALUES (seq_message_data_id.nextval, ?)";
    private static final String lastMessageSQL = "SELECT *  FROM message_data where id = (select max(id) from message_data)";
    private static final String countMessagesSQL = "SELECT count(*) FROM message_data";

    private ComboPooledDataSource cpds;

    JDBCService(ComboPooledDataSource cpds) {
        this.cpds = cpds;
    }

    public void createMessage(int message) {
        try (Connection connection = cpds.getConnection();
             PreparedStatement insertPreparedStatement = connection.prepareStatement(insertRabbitmqSQL)) {
            connection.setAutoCommit(false);
            insertPreparedStatement.setInt(1, message);
            insertPreparedStatement.addBatch();
            insertPreparedStatement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            ApplicationException.fail(e);
        }
    }

    public int getLastMessage() {
        try (Connection connection = cpds.getConnection();
             PreparedStatement lastMessagePrepareStatement = connection.prepareStatement(lastMessageSQL);
             ResultSet rs = lastMessagePrepareStatement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(2);
            }
            return 0;
        } catch (SQLException e) {
            ApplicationException.fail(e);
            return 0;
        }
    }

    public int getMessageCount() {
        try (Connection connection = cpds.getConnection();
             PreparedStatement countMessagesPrepareStatement = connection.prepareStatement(countMessagesSQL);
             ResultSet rs = countMessagesPrepareStatement.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            ApplicationException.fail(e);
            return 0;
        }
    }

    public MessageData getMessageData() {
        try (Connection connection = cpds.getConnection();
             PreparedStatement lastMessagePrepareStatement = connection.prepareStatement(lastMessageSQL);
             PreparedStatement countMessagesPrepareStatement = connection.prepareStatement(countMessagesSQL);
             ResultSet rsLastMsg = lastMessagePrepareStatement.executeQuery();
             ResultSet rsCountMsgs = countMessagesPrepareStatement.executeQuery()) {
            if (rsLastMsg.next() && rsCountMsgs.next()) {
                MessageData messageData = new MessageData();
                messageData.setCount(rsCountMsgs.getInt(1));
                messageData.setLastMessage(rsLastMsg.getInt(2));
                return messageData;
            }
            return null;
        } catch (SQLException e) {
            ApplicationException.fail(e);
            return null;
        }
    }

}
