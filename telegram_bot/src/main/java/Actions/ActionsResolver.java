package Actions;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;

public class ActionsResolver {

    private static HashMap<String, Class> actions = new HashMap<String, Class>();

    static {
        // defines mapping between commands and actinos
        actions.put("/add", Add.class);
        actions.put("/comment", Comment.class);
        actions.put("/simple_game", SimpleGame.class);
        actions.put("/start", Start.class);
    }

    public static Action getAction(Message message) {
        Action action = null;

        String s = message.getText();
        if (s.charAt(0) == '/') {
            if(s.equals("/stop")) {
                action = DefaultAction.DefaultAction();
            }
            else {
                try {
                    Class actionClass = actions.get(s);
                    if(actionClass != null) action = (Action) actionClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return action;
    }

}
