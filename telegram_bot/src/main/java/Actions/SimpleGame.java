package Actions;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class SimpleGame implements Action {

    private boolean onGoing = true;
    private int secret = (int)(Math.random() * 100) + 1;

    public boolean onGoing() {
        return onGoing;
    }

    public SendMessage processMessage(Message message) {
        String s = message.getText();
        SendMessage reply = new SendMessage().setChatId(message.getChatId());
        if(s.equals("/simple_game")) {
                    return reply.setText("guess the number (between 1 and 100 (including 1 and 100))");
        } else {
            try {
                int n = Integer.parseInt(s);
                if(n < secret) {
                    return reply.setText("too small");
                } else if (n > secret) {
                    return reply.setText("too big");
                } else {
                    onGoing = false;
                    return reply.setText("you won");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
