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
    }

    public static Action getAction(Message message) {
        Action action = null;
        SendMessage reply =  new SendMessage().setChatId(message.getChatId());

        String s = message.getText();
        if (s.charAt(0) == '/') {
            if(s.equals("/cancel")) {
                action = DefaultAction.DefaultAction();
            }
            else {
                try {
                    Class actionClass = actions.get(s);
                    // If the action class is not found, then it means the user entered an invalid command
                    if (actionClass != null) {
                        // Ensure the user is registered (this check does not apply to the /start, of course)
                        Integer id = message.getFrom().getId();
                        StatementResult results = Neo4jUtils.readingQuery("MATCH (u:User {telegramId: " + id + "}) RETURN u LIMIT 1");
                        if (!results.hasNext() && !s.equals("/start")) {
                            action = (Action) UnregisteredUserException.class.newInstance();
                        } else {
                            action = (Action) actionClass.newInstance();
                        }
                    // If an incorrect function was called, display an error message
                    } else {
                        action = (Action) IncorrectFunctionException.class.newInstance();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return action;
    }

}
