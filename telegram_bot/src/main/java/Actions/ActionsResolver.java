package Actions;

import org.telegram.telegrambots.meta.api.objects.Message;

public class ActionsResolver {

    public static Action getAction(Message message) {
        Action action = null;

        String s = message.getText();
        if (s.charAt(0) == '/') {
            //defines actions keywords
            if (s.equals("/add")) {
                action = new Add();
            } else if (s.equals("/stop")) {
                action = DefaultAction.DefaultAction();
            } else if (s.equals("/simple_game")) {
                action = new SimpleGame();
            } else if (s.equals("/like")) {
                action = new Like();
            }
        }

        return action;
    }

}
