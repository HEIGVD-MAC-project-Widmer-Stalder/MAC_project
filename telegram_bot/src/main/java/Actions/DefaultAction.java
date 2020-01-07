package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class DefaultAction extends Action {

    private static DefaultAction da = new DefaultAction();

    private DefaultAction(){}

    public static DefaultAction DefaultAction() {
        return da;
    }

    public SendMessage processMessage(Message message) {
        return null;
    }
}
