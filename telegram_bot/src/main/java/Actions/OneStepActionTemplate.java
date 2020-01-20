package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class OneStepActionTemplate extends Action {

    private enum State {BEGINNING, END}

    private State state = State.BEGINNING;

    public SendMessage processMessage(Message message) {

        // we extract tokens from the message sent by the user
        String s = message.getText();

        // the reply we'll send to the user soon
        SendMessage reply = new SendMessage().setChatId(message.getChatId());

        try {
            // TODO
            reply.setText("blablabla");
            setActionAsCompleted();
            return reply;
        } catch (Exception e) {
            // something when wrong: we tell the user and print some logging
            e.printStackTrace();
            state = State.END;
            return reply.setText("an error occurred." +
                    "we are sorry for the inconvenience.");
        }

        // TODO: 3 add your command in ActionsResolver.java (so that the bot knows this command exists and maps it)
    }

}