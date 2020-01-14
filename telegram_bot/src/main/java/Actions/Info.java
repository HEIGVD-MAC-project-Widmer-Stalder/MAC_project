package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;

public class Info extends Action {

    private Date version = null;

    {
        try {
            version = new Date(new File(getClass().getClassLoader().getResource(getClass().getCanonicalName().replace('.', '/') + ".class").toURI()).lastModified());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public SendMessage processMessage(Message message) {
        // the reply we'll send to the user
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        return reply.setText("Compile time of Info.java class: " + version.toString());
    }

}
