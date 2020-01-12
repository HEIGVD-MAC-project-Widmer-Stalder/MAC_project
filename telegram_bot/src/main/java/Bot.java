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

    HashMap<Long, Action> chatCurrentAction = new HashMap<Long, Action>();

    //Action currentAction = DefaultAction.DefaultAction();

    private final static String propertyFileName = "telegram_bot.properties";

    String botUsername;
    String botSecretToken;

    public Bot() throws Exception {
        // Configure the bot and the neo4j driver according to properties files
        Properties prop = new Properties();
        InputStream inputStream = Neo4jDriver.class.getClassLoader().getResourceAsStream(propertyFileName);
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

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            manageMessages(update.getMessage());
        }
    }

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

        if(!currentAction.onGoing()) currentAction = DefaultAction.DefaultAction();

    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botSecretToken;
    }
}
