package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class Add implements Action {

    private boolean onGoing = true;

    public boolean onGoing() {
        return onGoing;
    }

    public SendMessage processMessage(Message message) {
        if(message.getText().contains("/add")){
            return new SendMessage()
                    .setChatId(message.getChatId())
                    .setText("enter the url of the document");
        } else {
            //add document to the db
            onGoing = false;
            return null;
        }
    }
}
