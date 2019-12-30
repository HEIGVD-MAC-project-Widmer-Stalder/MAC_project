package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Action {
    boolean onGoing();
    SendMessage processMessage(Message message);
}
