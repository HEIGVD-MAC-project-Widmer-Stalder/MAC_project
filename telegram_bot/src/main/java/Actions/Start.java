package Actions;

import graph_db.Neo4jUtils;
import org.neo4j.driver.v1.StatementResult;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import  org.telegram.telegrambots.meta.api.objects.User;

import static org.neo4j.driver.v1.Values.parameters;

public class Start extends Action {

    public SendMessage processMessage(Message message) {

        User u = message.getFrom();
        Integer id = u.getId();

        // (sometimes username is null for some reason...)
        String username = u.getUserName() != null ? u.getUserName() : u.getFirstName();
        if(username == null) username = "unkown";
        try {
            // If the user is already registered, then show a message saying it is already registered
            StatementResult results = Neo4jUtils.readingQuery("MATCH (u:User {telegramId: " + id + "}) RETURN u LIMIT 1");
            if (results.hasNext()) {
                return new SendMessage().setChatId(message.getChatId()).setText("You are already registered!");
            }

            Neo4jUtils.writingQuery("MERGE (user:User{telegramId: $id, username: $username})",
                    parameters("id", id, "username", username));
            setActionAsCompleted();
            return new SendMessage().setChatId(message.getChatId()).setText("You can now use the bot. Welcome and enjoy!");
        } catch (Exception e) {
            e.printStackTrace();
            setActionAsCompleted();
            return new SendMessage().setChatId(message.getChatId()).setText("an error occured when trying to register" +
                    " you as a users");
        }
    }
}
