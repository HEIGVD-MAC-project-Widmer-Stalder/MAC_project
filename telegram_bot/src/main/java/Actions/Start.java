package Actions;

import graph_db.Neo4jUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import  org.telegram.telegrambots.meta.api.objects.User;

import static org.neo4j.driver.v1.Values.parameters;

public class Start extends Action {

    public SendMessage processMessage(Message message) {

        User u = message.getFrom();
        Integer id = u.getId();
        String username = u.getUserName();

        try {
            Neo4jUtils.writingQuery("MERGE (user:User{telegramId: $id, username: $username})",
                    parameters("id", id, "username", username));
            setActionAsCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            setActionAsCompleted();
            return new SendMessage().setChatId(message.getChatId()).setText("an error occured when trying to register" +
                    " you as a users");
        }

        return null;

    }
}
