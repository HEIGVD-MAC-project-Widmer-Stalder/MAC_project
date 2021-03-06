package Actions;

import exceptions.IncorrectFunctionException;
import exceptions.UnregisteredUserException;
import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.StatementResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;

public class ActionsResolver {

    private static HashMap<String, Class> actions = new HashMap<String, Class>();

    static {
        // defines mapping between commands and actinos
        actions.put("/start", Start.class);
        actions.put("/add", Add.class);
        actions.put("/like", Like.class);
        actions.put("/comment", Comment.class);
        actions.put("/tag", Tag.class);
        actions.put("/info", Info.class);
        actions.put("/users", Users.class);
        actions.put("/tags", Tags.class);
        actions.put("/documents", Documents.class);
        actions.put("/user", User.class);
        actions.put("/get_users_by_tag", GetUsersByTag.class);
        actions.put("/most_frequent_contradictor", MostFrequentContradictor.class);
        actions.put("/most_personal_contradictor", MostPersonalContradictor.class);
        actions.put("/curious_fact1", CuriousFact1.class);
        actions.put("/document", Document.class);
    }

    public static Action getAction(Message message) {
        Action action = null;

        String s = message.getText();
        if (s.charAt(0) == '/') {
            if(s.equals("/cancel")) {
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
