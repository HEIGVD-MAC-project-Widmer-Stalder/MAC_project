package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class Documents extends Action {

    public SendMessage processMessage(Message message) {
        String s = message.getText();
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        //TODO
        return null;
    }
}