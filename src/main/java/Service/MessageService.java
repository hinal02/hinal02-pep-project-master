package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message creatMessage(Message message) {
        // create message
        return messageDAO.insertMessage(message);

    }

    public List<Message> getAllMessagee() {
        // Return all messages from the database
        return messageDAO.getMessages();

    }

    public Message getMessageById(int messageId) {
        // Retrieve the message from the database using the message_id
        Message message = messageDAO.getMessageById(messageId);
        if (message == null)
            return null;
        // Return the message or null if it doesn't exist
        return message;
    }

    public Message delMessage(int message_id) {
        return messageDAO.deleteMessage(message_id);
    }

    public Message updatMessage(int message_id, Message message) {
        // verifying existing message
        Message existingMessage = getMessageById(message_id);
        if (existingMessage == null) {
            return null;
        }
        // Check if the new message_text is blank or exceeds 255 characters
        String newMessageText = message.getMessage_text();
        if (newMessageText.isBlank() || newMessageText.length() >= 255) {
            return null;
        }
        return messageDAO.updatemessage(message_id, message);
    }
    public List<Message> getMessagesByUser(int accId) {
    return messageDAO.getmessageByUser(accId);
}

}
