import Actions.Action;
import Actions.ActionsResolver;
import Actions.DefaultAction;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    Action currentAction = DefaultAction.DefaultAction();

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
            currentAction = a;
        }

        // what should be done is delegated to the current ongoing action
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
        return "niko_test_bot";
    }

    public String getBotToken() {
        return "995989805:AAFP_phbrvTsA5a5sj3BpUdQmdzO7ZLU5hA";
    }
}
