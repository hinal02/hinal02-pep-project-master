package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    // Inserts a new message into the database and returns the inserted message with
    // the generated ID.
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?,? ,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            // execute the query
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int generated_id = (int) resultSet.getLong(1);
                message.setMessage_id(generated_id);
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }

        return null;

    }

    // Retrieves all messages from the database and returns them as a list.
    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();

        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = " select * from message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // preparedStatement.setString(1, sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
        return messages;

    }
    // Retrieves a message from the database based on its ID and returns it.

    public Message getMessageById(int id) {

        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = " select * from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Message message = new Message(resultSet.getInt("message_id"), resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"), resultSet.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
        return null;

    }

    public Message deleteMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "delete from message where message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);

            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0)
                // Return the deleted message
                return getMessageById(message_id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
        return null;

    }

    // Update message text from message id.
    public Message updatemessage(int message_id, Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message.getMessage_text());

            preparedStatement.setInt(2, message_id);
          
            int resultSet = preparedStatement.executeUpdate();
            if (resultSet > 0) {
                // in getMessageById it will return the new message with all information
                Message updatedMessage = getMessageById(message_id);

                return updatedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
        return null;
    }

    public List<Message> getmessageByUser(int accId){
        List<Message> messages = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = " select * from message where posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setInt(1, accId);

             ResultSet resultSet = preparedStatement.executeQuery();
             while (resultSet.next()) {
            int messageId = resultSet.getInt("message_id");
            int postedBy = resultSet.getInt("posted_by");
            String messageText = resultSet.getString("message_text");
            long timePostedEpoch = resultSet.getLong("time_posted_epoch");

            Message message = new Message(messageId, postedBy, messageText, timePostedEpoch);
            messages.add(message);
        }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
        return messages;
        

    }
}
