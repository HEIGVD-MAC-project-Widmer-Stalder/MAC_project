package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public abstract class Action {
    private boolean onGoing = true;
    public boolean onGoing() {
        return onGoing;
    }
    protected void setActionAsCompleted() {
        onGoing = false;
    }
    abstract public SendMessage processMessage(Message message);
}
