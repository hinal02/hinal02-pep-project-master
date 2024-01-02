package Controller;

import static org.mockito.ArgumentMatchers.notNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.Accountservice;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {

    private Accountservice accountservice;
    private MessageService messageService;

    public SocialMediaController() {
        this.accountservice = new Accountservice();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::registrationHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::messagehandler);
        app.get("/messages", this::getMessageHandler);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("accounts/{account_id}/messages", this::messageByUser);

        return app;
    }

    /**
     * This is an registration handler for an register endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    private void registrationHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(context.body(), Account.class);

        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            // username or password is incorrect
            context.status(400);
            return;
        }
        Account newacc = accountservice.newAccount(account);
        if (newacc == null) {
            // account already exists
            context.status(400);
            return;
        }
        context.status(200).json(newacc);

    }
    /*
     * this is a login Handle for a login
     */

    private void loginHandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper op = new ObjectMapper();
        Account account = op.readValue(context.body(), Account.class);

        Account autheAccount = accountservice.authenticateAccount(account.getUsername(), account.getPassword());
        if (autheAccount == null) {
            context.status(401);
            return;
        }
        context.status(200).json(autheAccount);
    }

    // able to process the creation of new messages.
    private void messagehandler(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper op = new ObjectMapper();

        Message message = op.readValue(context.body(), Message.class);

        // if message is blank and size of message is more than 255 and the account does
        // not exist than it gives 400.
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() >= 255) {
            context.status(400);
            return;
        }
        Message messageCreate = messageService.creatMessage(message);

        // message create successfully
        if (messageCreate != null) {
            context.status(200).json(messageCreate);
        } else
            context.status(400);

    }

    private void getMessageHandler(Context context) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessagee();
        ObjectMapper op = new ObjectMapper();
        String responseBody = op.writeValueAsString(messages);

        context.status(200).json(responseBody);

    }

    private void getMessageById(Context context) throws JsonProcessingException {

        // convert from string to Integer to set the path
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message);
        } else
            // If message not found it will return emty string message
            context.status(200).json("");

    }

    private void deleteMessage(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            // Delete the message from the database
            messageService.delMessage(messageId);
            context.status(200).json(message);
        } else
            // If message not found it will return emty string message
            context.status(200);

    }

    private void updateMessage(Context context) throws JsonMappingException, JsonProcessingException {
        ObjectMapper op = new ObjectMapper();

        Message message = op.readValue(context.body(), Message.class);

        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message newMessage = messageService.updatMessage(messageId, message);

            if (newMessage.toString() == null) {
                context.status(400);
                return;
            } else {
                context.status(200).json(newMessage);
            }
        } catch (Exception e) {
            // message not update successfully
            System.out.println(e.getMessage());
            context.status(400);
            // TODO: handle exception
        }

    }
    private void  messageByUser(Context context){
        try {
            int accountId = Integer.parseInt(context.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByUser(accountId);
            context.status(200).json(messages);
        } catch (Exception e) {
            // Invalid account_id parameter
            context.status(400);
        }
    }

}
