import Actions.Action;
import Actions.ActionsResolver;
import Actions.DefaultAction;
import graph_db.Neo4jDriver;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class Bot extends TelegramLongPollingBot {

    // stores the ongoing action for each chat (Long is for chatId)
    private HashMap<Long, Action> chatCurrentAction = new HashMap<Long, Action>();

    private final static String propertyFileName = "telegram_bot.properties";

    private String botUsername;
    private String botSecretToken;

    public Bot() throws Exception {
        // Configure the bot according to properties files
        Properties prop = new Properties();
        InputStream inputStream = Bot.class.getClassLoader().getResourceAsStream(propertyFileName);
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            throw new FileNotFoundException("telegram_bot.properties file is missing");
        }
        botUsername = prop.getProperty("bot_username");
        botSecretToken = prop.getProperty("bot_secret_token");
        if(botUsername == null || botSecretToken == null || botUsername.equals("") || botSecretToken.equals("")) {
            throw new Exception("missing properties in " + propertyFileName + " (maybe a missing bot_username and bot_secret_token?)");
        }
    }

    /**
     * This method is automatically called when we receive an update
     * @param update may contain a message of other stuff
     */
    public void onUpdateReceived(Update update) {
        // if the update is about a non empty message
        if (update.hasMessage() && update.getMessage().hasText()) {
            // take appropriate action
            manageMessages(update.getMessage());
        }
    }

    /**
     * (Method needed to extends TelegramBot)
     * @return the unsername of the bor
     */
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * (Method needed to extends TelegramBot)
     * @return the telegram secret token needed to control the bot
     */
    public String getBotToken() {
        return botSecretToken;
    }

    /**
     * here is decided which action will be taken for messages
     * @param message
     */
    public void manageMessages(Message message) {
        System.out.println(message.getText());

        // if the message aims to start a new action, we change currentAction accordingly
        Action a = ActionsResolver.getAction(message);
        if (a != null) {
            chatCurrentAction.put(message.getChatId(), a);
        }

        // The processing of the reply is delegated to the current ongoing action
        Action currentAction = chatCurrentAction.get(message.getChatId());
        currentAction = currentAction != null ? currentAction : DefaultAction.DefaultAction();
        SendMessage reply = currentAction.processMessage(message);
        if (reply != null) {
            try {
                execute(reply); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        // When an action is no more ongoing, we replace it with a default action
        if(!currentAction.onGoing()) chatCurrentAction.put(message.getChatId(), DefaultAction.DefaultAction());

    }
}
